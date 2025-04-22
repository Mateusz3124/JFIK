import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.stream.IntStream;

import org.antlr.v4.runtime.ParserRuleContext;

enum VarType {
   INT, INT64, REAL, FLOAT32, FLOAT64, ANY, STRUCT, CLASS, VOID, UNKNOWN,
}

class Value {
   public String name;
   public VarType type;
   public boolean isMalloc = false;
   public String structID = null;

   public Value(String name, VarType type) {
      this.name = name;
      this.type = type;
   }

   public Value(String name, VarType type, boolean isAny) {
      this.name = name;
      this.type = type;
      this.isMalloc = isAny;
   }

   @Override
   public int hashCode() {
      return name.hashCode();
   }

   @Override
   public boolean equals(Object obj) {
      return obj.equals(this.name);
   }

}

enum VariableScope {
   main, function, global
}

class Variables {
   HashMap<String, VariableI> mainVariables = new HashMap<>();
   HashMap<String, VariableI> functionVariables = new HashMap<>();
   HashMap<String, VariableI> globalVariables = new HashMap<>();
   VariableScope scope = VariableScope.main;

   void error(int line, String msg) {
      System.err.println("Error, line " + line + ": " + msg);
      System.exit(1);
   }

   public VariableI get(String id, ParserRuleContext ctx) {
      VariableI val = null;
      switch (scope) {
         case VariableScope.main:
            if (!mainVariables.containsKey(id)) {
               val = globalVariables.get(id);
            } else {
               val = mainVariables.get(id);
            }
            break;

         case VariableScope.function:
            if (!functionVariables.containsKey(id)) {
               val = globalVariables.get(id);
            } else {
               val = functionVariables.get(id);
            }
            break;

         default:
            System.err.println("Scope is incorrect" + scope);
            System.exit(1);
      }
      if (val == null) {
         error(ctx.getStart().getLine(), "No value: " + id);
      }
      return val;
   }

   public void put(VariableI variable) {
      Value val = variable.getValue();
      String temp = val.name;
      switch (scope) {
         case VariableScope.main:
            val.name = "%" + val.name;
            variable.setValue(val);
            mainVariables.put(temp, variable);
            break;

         case VariableScope.function:
            val.name = "%" + val.name;
            variable.setValue(val);
            functionVariables.put(temp, variable);
            break;

         case VariableScope.global:
            val.name = "@" + val.name;
            variable.setValue(val);
            globalVariables.put(temp, variable);
            break;

         default:
            System.err.println("Scope is incorrect" + scope);
            System.exit(1);
      }
   }

   public void clearFunctionVariables() {
      functionVariables.clear();
   }

}

public class LLVMActions extends LangXBaseListener {

   Variables variables = new Variables();
   HashMap<String, VarType> functions = new HashMap<>();
   HashMap<String, ArrayList<Value>> classFunctions = new HashMap<>();
   HashMap<String, ArrayList<Value>> structures = new HashMap<>();
   Stack<Value> stack = new Stack<>();
   Stack<Value> structValues = new Stack<>();
   boolean isReturn = false;
   Value function;
   Value classVal;

   void error(int line, String msg) {
      System.err.println("Error, line " + line + ": " + msg);
      System.exit(1);
   }

   public int findTypeIdx(String value) {
      for (int i = 0; i < Const.typesLang.length; i++) {
         if (Const.typesLang[i].equals(value)) {
            return i;
         }
      }
      return -1;
   }

   @Override
   public void exitAssignType(LangXParser.AssignTypeContext ctx) {
      String ID = ctx.ID().getText();
      String type = ctx.type().getText();

      if (type.equals("any")) {
         Value val = new Value(ID, VarType.ANY);
         VariableI variable = new Any(val);
         variables.put(variable);
         LLVMGenerator.declare(ID, "ptr");
         return;
      }

      if (type.equals("struct")) {
         Value val = new Value(ID, VarType.STRUCT);
         VariableI variable = new Struct(val);
         variables.put(variable);
         LLVMGenerator.declare(ID, "ptr");
         return;
      }

      int i = findTypeIdx(type);
      Value val = new Value(ID, VarType.values()[i]);
      Variable variable = new Variable(val);
      variables.put(variable);
      LLVMGenerator.declare(ID, Const.typesLLVM[i]);
   }

   private void initStructMalloc(String id, String idTarget) {
      ArrayList<Value> list = structures.get(idTarget);
      int typeSize = 4;
      for (Value val : list) {
         if (val.type == VarType.FLOAT64 || val.type == VarType.FLOAT32) {
            typeSize = 8;
         }
      }
      LLVMGenerator.malloc(id, typeSize * list.size());
      return;
   }

   @Override
   public void exitAssignTypedStruct(LangXParser.AssignTypedStructContext ctx) {
      String ID = ctx.ID(0).getText();
      String IDTARGET = ctx.ID(1).getText();
      ArrayList<Value> list = structures.get(IDTARGET);
      VariableI structure = new Struct(ID, list, IDTARGET);
      variables.put(structure);
      LLVMGenerator.declare(ID, "ptr");
      initStructMalloc("%" + ID, IDTARGET);
   }

   @Override
   public void exitAssignTyped(LangXParser.AssignTypedContext ctx) {
      String ID = ctx.ID().getText();
      String type = ctx.type().getText();

      Value val = stack.pop();

      if (type.equals("any")) {
         Value valAny = new Value(ID, VarType.ANY);
         Any any = new Any(valAny);
         variables.put(any);
         LLVMGenerator.declare(ID, "ptr");
         any.store(val);
         return;
      }

      int i = findTypeIdx(type);

      if (val.type != VarType.values()[i]) {
         error(ctx.getStart().getLine(), "Left type is: " + type + "but right side type is: " + Const.typesLLVM[i]);
      }

      LLVMGenerator.declare(ID, Const.typesLLVM[i]);

      Value curr = new Value(ID, val.type);
      Variable var = new Variable(curr);
      variables.put(var);
      var.store(val);
   }

   @Override
   public void exitAssignTypeGlobal(LangXParser.AssignTypeGlobalContext ctx) {
      String ID = ctx.ID().getText();
      String type = ctx.type().getText();

      int i = findTypeIdx(type);

      if (type.equals("any")) {
         VariableScope curr = variables.scope;
         variables.scope = VariableScope.global;
         Any val = new Any(ID, VarType.ANY);
         variables.put(val);
         variables.scope = curr;
         LLVMGenerator.declareGlobal(ID, Const.typesLLVM[i], "null");
         return;
      }

      VariableScope curr = variables.scope;
      variables.scope = VariableScope.global;
      Variable val = new Variable(ID, VarType.values()[i]);
      variables.put(val);
      variables.scope = curr;

      String defaultVal = "0";
      if (VarType.values()[i] != VarType.INT && VarType.values()[i] != VarType.INT64)
         defaultVal = "0.0";
      LLVMGenerator.declareGlobal(ID, Const.typesLLVM[i], defaultVal);
   }

   @Override
   public void exitAssignTypedGlobal(LangXParser.AssignTypedGlobalContext ctx) {
      String ID = ctx.ID().getText();
      String type = ctx.type().getText();

      if (type.equals("any")) {
         error(ctx.getStart().getLine(), "Global can't be any");
      }

      int i = findTypeIdx(type);

      Value val = stack.pop();
      LLVMGenerator.declareGlobal(ID, Const.typesLLVM[i], val.name);

      VariableScope curr = variables.scope;
      variables.scope = VariableScope.global;
      Variable var = new Variable(ID, VarType.values()[i]);
      variables.put(var);
      variables.scope = curr;
   }

   @Override
   public void exitAssign(LangXParser.AssignContext ctx) {
      String ID = ctx.ID().getText();
      Value v = stack.pop();
      VariableI val = variables.get(ID, ctx);

      if (!(val instanceof Any) && val.getValue().type != v.type) {
         error(ctx.getStart().getLine(), "Incompatible types: expected " + val.getValue().type + ", got " + v.type);
         return;
      }
      if (v.type == VarType.STRUCT && val instanceof Any) {
         Struct struct = (Struct) variables.get(v.structID, ctx);
         ((Any) val).storeStruct(struct);
         return;
      }
      val.store(v);
      val.getValue().type = v.type;
   }

   @Override
   public void exitAssignStructKey(LangXParser.AssignStructKeyContext ctx) {
      String ID = ctx.ID(0).getText();
      String KEY = ctx.ID(1).getText();
      Value v = stack.pop();
      VariableI variable = variables.get(ID, ctx);
      Struct val;
      if (variable instanceof Any) {
         val = ((Any) variable).geStruct();
      } else {
         val = (Struct) variable;
      }
      val.store(v, KEY);
   }

   public void exitStructValue(LangXParser.StructValueContext ctx) {
      String ID = ctx.ID(0).getText();
      String KEY = ctx.ID(1).getText();
      VariableI variable = variables.get(ID, ctx);
      Struct val;
      if (variable instanceof Any) {
         val = (((Any) variable).geStruct());
      } else {
         val = (Struct) variable;
      }
      String idKey = val.loadKeyToValue(KEY);
      Value newVal = new Value(idKey, val.getKeyType(KEY));
      stack.push(newVal);
   }

   @Override
   public void exitProg(LangXParser.ProgContext ctx) {
      LLVMGenerator.freeAtEnd();
      LLVMGenerator.close_main();
      System.out.println(LLVMGenerator.generate());
   }

   @Override
   public void exitIdStat(LangXParser.IdStatContext ctx) {
      String ID = ctx.ID().getText();
      VariableI val = variables.get(ID, ctx);
      String id = val.load();
      Value newVal = new Value(id, val.getValue().type);
      if (newVal.type == VarType.STRUCT) {
         newVal.structID = ID;
      }
      stack.push(newVal);
   }

   private void binaryOp(ParserRuleContext ctx, String op) {
      Value v1 = stack.pop();
      Value v2 = stack.pop();
      if (v1.type != v2.type) {
         error(ctx.getStart().getLine(), op + " type mismatch");
         return;
      }

      switch (v1.type) {
         case INT:
            callOp(op + "_i32", v2.name, v1.name);
            break;
         case INT64:
            callOp(op + "_i64", v2.name, v1.name);
            break;
         case REAL:
            callOp(op + "_double", v2.name, v1.name);
            break;
         case FLOAT32:
            callOp(op + "_float32", v2.name, v1.name);
            break;
         case FLOAT64:
            callOp(op + "_float64", v2.name, v1.name);
            break;
         default:
            error(ctx.getStart().getLine(), "unsupported type in operation");
      }

      stack.push(new Value("%" + (LLVMGenerator.reg - 1), v1.type));
   }

   private void callOp(String methodName, String v1, String v2) {
      try {
         LLVMGenerator.class.getMethod(methodName, String.class, String.class).invoke(null, v1, v2);
      } catch (Exception e) {
         throw new RuntimeException("Missing LLVM method: " + methodName, e);
      }
   }

   private void callOp(String methodName, String v1) {
      try {
         LLVMGenerator.class.getMethod(methodName, String.class).invoke(null, v1);
      } catch (Exception e) {
         throw new RuntimeException("Missing LLVM method: " + methodName, e);
      }
   }

   @Override
   public void exitPrint(LangXParser.PrintContext ctx) {
      Value val = stack.pop();
      switch (val.type) {
         case INT:
            LLVMGenerator.printf_i32(val.name);
            break;
         case INT64:
            LLVMGenerator.printf_i64(val.name);
            break;
         case REAL:
            LLVMGenerator.printf_double(val.name);
            break;
         case FLOAT32:
            LLVMGenerator.printf_float32(val.name);
            break;
         case FLOAT64:
            LLVMGenerator.printf_float64(val.name);
            break;
         default:
            error(ctx.getStart().getLine(), "unknown variable " + val.name);
      }
   }

   @Override
   public void exitRead(LangXParser.ReadContext ctx) {
      String ID = ctx.ID().getText();
      Any var = (Any) variables.get(ID, ctx);
      Value val = var.getValue();
      switch (val.type) {
         case INT:
            LLVMGenerator.scanf_i32(ID);
            break;
         case INT64:
            LLVMGenerator.scanf_i64(ID);
            break;
         case REAL:
            LLVMGenerator.scanf_double(ID);
            break;
         case FLOAT32:
            LLVMGenerator.scanf_float32(ID);
            break;
         case FLOAT64:
            LLVMGenerator.scanf_float64(ID);
            break;
         default:
            error(ctx.getStart().getLine(), "unknown variable " + ID);
      }
   }

   @Override
   public void exitCall(LangXParser.CallContext ctx) {
      String id = ctx.ID().getText();
      VarType type = functions.get(id);
      if (type == VarType.VOID)
         type = VarType.INT;
      int newId = LLVMGenerator.call(id, Const.typesLLVM[type.ordinal()]);
      String ID = "%" + String.valueOf(newId);
      Value val = new Value(ID, type);
      stack.add(val);
   }

   @Override
   public void exitCallClassReturn(LangXParser.CallClassReturnContext ctx) {
      String classVal = ctx.ID(0).getText();
      String function = ctx.ID(1).getText();

      Struct struct = (Struct) variables.get(classVal, ctx);

      Value fun = classFunctions.get(struct.structId).stream()
            .filter(val -> val.name.equals(classVal + function))
            .findFirst()
            .orElse(null);
      ;

      VarType type = fun.type;

      String idStruct = struct.load();

      if (type == VarType.VOID)
         type = VarType.INT;
      int newId = LLVMGenerator.call(fun.name, Const.typesLLVM[type.ordinal()], " ptr noundef " + idStruct + " ");
      String ID = "%" + String.valueOf(newId);
      Value val = new Value(ID, type);
      stack.add(val);
   }

   @Override
   public void exitCallSingle(LangXParser.CallSingleContext ctx) {
      String id = ctx.ID().getText();
      VarType type = functions.get(id);
      if (type == VarType.VOID)
         type = VarType.INT;
      LLVMGenerator.call(id, Const.typesLLVM[type.ordinal()]);
   }

   @Override
   public void exitCallClass(LangXParser.CallClassContext ctx) {
      String classVal = ctx.ID(0).getText();
      String function = ctx.ID(1).getText();

      Struct struct = (Struct) variables.get(classVal, ctx);

      Value fun = classFunctions.get(struct.structId).stream()
            .filter(val -> val.name.equals(struct.structId + function))
            .findFirst()
            .orElse(null);
      ;

      VarType type = fun.type;

      String idStruct = struct.load();

      if (type == VarType.VOID)
         type = VarType.INT;
      LLVMGenerator.call(fun.name, Const.typesLLVM[type.ordinal()], " ptr noundef " + idStruct + " ");
   }

   @Override
   public void exitFparam(LangXParser.FparamContext ctx) {
      String ID = ctx.ID().getText();
      String funcType = ctx.funcType().getText();
      VarType type = VarType.valueOf(funcType.toUpperCase());

      String args = "";

      if (classVal != null) {
         if (!structValues.empty()) {
            String values = "";
            for (Value valForEach : structValues) {
               values += Const.typesLLVM[valForEach.type.ordinal()] + ", ";
            }
            String trimmed = values.substring(0, values.length() - 2);
            LLVMGenerator.structInit(classVal.name, trimmed);

            ArrayList<Value> list = new ArrayList<>();
            for (Value v : structValues) {
               list.add(v);
            }

            structures.put(classVal.name, list);
            structValues.clear();
         }
         args = " ptr %0 ";
         ID = classVal.name + ID;
      }

      Value val = new Value(ID, type);
      variables.scope = VariableScope.function;
      functions.put(ID, type);
      function = val;

      if (type != VarType.VOID) {
         LLVMGenerator.functionstartType(ID, Const.typesLLVM[type.ordinal()], args);
      } else {
         LLVMGenerator.functionstartType(ID, "i32", args);
      }
      if (classVal != null) {
         ArrayList<Value> keys = structures.get(classVal.name);
         Struct struct = new Struct("this", keys, classVal.name);
         variables.put(struct);
         LLVMGenerator.initThis();
         ArrayList<Value> list = classFunctions.get(classVal.name);
         list.add(function);
      }
   }

   @Override
   public void exitReturn(LangXParser.ReturnContext ctx) {
      Value val = stack.pop();
      LLVMGenerator.addReturn(Const.typesLLVM[val.type.ordinal()], val.name);
      isReturn = true;
   }

   @Override
   public void exitFblock(LangXParser.FblockContext ctx) {
      variables.scope = VariableScope.main;
      variables.clearFunctionVariables();
      if (function.type != VarType.VOID) {
         if (isReturn) {
            LLVMGenerator.functionend();
            isReturn = true;
            return;
         } else {
            error(ctx.getStart().getLine(), "no return");
         }
      }
      LLVMGenerator.functionendReturn();
   }

   @Override
   public void enterBlockif(LangXParser.BlockifContext ctx) {
      LLVMGenerator.ifstart();
   }

   @Override
   public void exitBlockif(LangXParser.BlockifContext ctx) {
      LLVMGenerator.ifend();
   }

   @Override
   public void exitEqual(LangXParser.EqualContext ctx) {
      Value val1 = stack.pop();
      Value val2 = stack.pop();

      if (val1.type != val2.type)
         error(ctx.getStart().getLine(), "incompatible types");

      if (val1.type == VarType.INT | val1.type == VarType.INT64) {
         LLVMGenerator.icmp(val1.name, val2.name, Const.typesLLVM[val1.type.ordinal()]);
      } else {
         LLVMGenerator.fcmp(val1.name, val2.name, Const.typesLLVM[val1.type.ordinal()]);
      }
   }

   @Override
   public void enterBlockfor(LangXParser.BlockforContext ctx) {
      Value val = stack.pop();
      LLVMGenerator.repeatstart(val.name);
   }

   @Override
   public void exitBlockfor(LangXParser.BlockforContext ctx) {
      LLVMGenerator.repeatend();
   }

   @Override
   public void exitStructVal(LangXParser.StructValContext ctx) {
      String ID = ctx.ID().getText();
      String type = ctx.type().getText();
      int i = findTypeIdx(type);
      Value val = new Value(ID, VarType.values()[i]);
      structValues.add(val);
   }

   @Override
   public void exitStruct(LangXParser.StructContext ctx) {
      String ID = ctx.ID().getText();
      String values = "";
      for (Value val : structValues) {
         values += Const.typesLLVM[val.type.ordinal()] + ", ";
      }
      String trimmed = values.substring(0, values.length() - 2);
      LLVMGenerator.structInit(ID, trimmed);
      ArrayList<Value> list = new ArrayList<>();
      for (Value v : structValues) {
         list.add(v);
      }
      structures.put(ID, list);
      structValues.clear();
   }

   @Override
   public void enterClass(LangXParser.ClassContext ctx) {
      String id = ctx.ID().getText();
      classVal = new Value(id, VarType.CLASS);
      classFunctions.put(id, new ArrayList<>());
   }

   @Override
   public void exitClass(LangXParser.ClassContext ctx) {
      classVal = null;
   }

   @Override
   public void exitInt(LangXParser.IntContext ctx) {
      stack.push(new Value(ctx.INT().getText(), VarType.INT));
   }

   @Override
   public void exitInt64(LangXParser.Int64Context ctx) {
      String val = ctx.INT64().getText();
      stack.push(new Value(val.substring(0, val.length() - 1), VarType.INT64));
   }

   @Override
   public void exitReal(LangXParser.RealContext ctx) {
      stack.push(new Value(ctx.REAL().getText(), VarType.REAL));
   }

   @Override
   public void exitFloat32(LangXParser.Float32Context ctx) {
      String val = ctx.FLOAT32().getText();
      stack.push(new Value(val.substring(0, val.length() - 1), VarType.FLOAT32));
   }

   @Override
   public void exitFloat64(LangXParser.Float64Context ctx) {
      String val = ctx.FLOAT64().getText();
      stack.push(new Value(val.substring(0, val.length() - 1), VarType.FLOAT64));
   }

   @Override
   public void exitAdd(LangXParser.AddContext ctx) {
      binaryOp(ctx, "add");
   }

   @Override
   public void exitSub(LangXParser.SubContext ctx) {
      binaryOp(ctx, "sub");
   }

   @Override
   public void exitMult(LangXParser.MultContext ctx) {
      binaryOp(ctx, "mult");
   }

   @Override
   public void exitDiv(LangXParser.DivContext ctx) {
      binaryOp(ctx, "div");
   }

}
