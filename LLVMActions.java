import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.stream.IntStream;

import org.antlr.v4.runtime.ParserRuleContext;

enum VarType {
   INT, INT64, REAL, FLOAT32, FLOAT64, VOID, UNKNOWN, ANY, STRUCT
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
   String[] typesString = new String[] { "i32", "i64", "double", "float", "double" };
   HashMap<String, Value> mainVariables = new HashMap<>();
   HashMap<String, Value> functionVariables = new HashMap<>();
   HashMap<String, Value> globalVariables = new HashMap<>();
   VariableScope scope = VariableScope.main;

   void error(int line, String msg) {
      System.err.println("Error, line " + line + ": " + msg);
      System.exit(1);
   }

   public Value get(String id, ParserRuleContext ctx) {
      Value val = null;
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

   public void put(String id, VarType type) {
      switch (scope) {
         case VariableScope.main:
            mainVariables.put(id, new Value(("%" + id), type));
            break;

         case VariableScope.function:
            functionVariables.put(id, new Value(("%" + id), type));
            break;

         case VariableScope.global:
            globalVariables.put(id, new Value(("@" + id), type));
            break;

         default:
            System.err.println("Scope is incorrect" + scope);
            System.exit(1);
      }
   }

   public void putMalloc(String id, VarType type) {
      switch (scope) {
         case VariableScope.main:
            mainVariables.put(id, new Value(("%" + id), type, true));
            break;

         case VariableScope.function:
            functionVariables.put(id, new Value(("%" + id), type, true));
            break;

         default:
            System.err.println("Scope is incorrect" + scope);
            System.exit(1);
      }
   }

   public String load(Value val) {
      String currId = val.name;
      if (val.isMalloc) {
         int anyReg = LLVMGenerator.loadAny(val.name);
         currId = "%" + String.valueOf(anyReg);
      }
      int currReg = LLVMGenerator.load(currId, typesString[val.type.ordinal()]);
      return "%" + String.valueOf(currReg);
   }

   public void clearFunctionVariables() {
      functionVariables.clear();
   }

}

public class LLVMActions extends LangXBaseListener {
   String[] typesString = new String[] { "i32", "i64", "double", "float", "double" };

   Variables variables = new Variables();
   HashMap<String, VarType> functions = new HashMap<>();
   HashMap<String, ArrayList<Value>> structures = new HashMap<>();
   Stack<Value> stack = new Stack<>();
   Stack<Value> structValues = new Stack<>();
   boolean isReturn = false;
   Value function;

   void error(int line, String msg) {
      System.err.println("Error, line " + line + ": " + msg);
      System.exit(1);
   }

   String[] typesStringLang = new String[] { "int", "int64", "real", "float32", "float64" };

   public int findTypeIdx(String value) {
      for (int i = 0; i < typesStringLang.length; i++) {
         if (typesStringLang[i].equals(value)) {
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
         variables.putMalloc(ID, VarType.ANY);
         LLVMGenerator.declare(ID, "ptr");
         return;
      }

      if (type.equals("struct")) {
         variables.putMalloc(ID, VarType.STRUCT);
         LLVMGenerator.declare(ID, "ptr");
         return;
      }

      int i = findTypeIdx(type);
      variables.put(ID, VarType.values()[i]);
      LLVMGenerator.declare(ID, typesString[i]);
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
   public void exitAssignStruct(LangXParser.AssignStructContext ctx) {
      String ID = ctx.ID(0).getText();
      String IDTARGET = ctx.ID(1).getText();
      Value id = variables.get(ID, ctx);
      id.structID = IDTARGET;
      initStructMalloc(id.name, IDTARGET);
   }

   @Override
   public void exitAssignTypedStruct(LangXParser.AssignTypedStructContext ctx) {
      String ID = ctx.ID(0).getText();
      String IDTARGET = ctx.ID(1).getText();
      variables.putMalloc(ID, VarType.STRUCT);
      Value val = variables.get(ID, ctx);
      val.structID = IDTARGET;
      LLVMGenerator.declare(ID, "ptr");
      initStructMalloc("%" + ID, IDTARGET);
   }

   @Override
   public void exitAssignTyped(LangXParser.AssignTypedContext ctx) {
      String ID = ctx.ID().getText();
      String type = ctx.type().getText();

      Value val = stack.pop();
      int i = findTypeIdx(type);

      if (type.equals("any")) {
         variables.putMalloc(ID, val.type);
         LLVMGenerator.declare(ID, "ptr");
         int typeSize = 4;
         if (val.type == VarType.REAL || val.type == VarType.FLOAT64) {
            typeSize = 8;
         }
         LLVMGenerator.assignAny("%" + ID, typesString[val.type.ordinal()], val.name, typeSize);
         return;
      }

      LLVMGenerator.declare(ID, typesString[i]);

      variables.put(ID, VarType.values()[i]);
      Value variable = variables.get(ID, ctx);
      LLVMGenerator.assign(variable.name, val.name, typesString[i]);
   }

   @Override
   public void exitAssignTypeGlobal(LangXParser.AssignTypeGlobalContext ctx) {
      String ID = ctx.ID().getText();
      String type = ctx.type().getText();

      if (type.equals("any")) {
         error(ctx.getStart().getLine(), "Global can't be any");
      }

      int i = findTypeIdx(type);
      VariableScope curr = variables.scope;
      variables.scope = VariableScope.global;
      variables.put(ID, VarType.values()[i]);
      variables.scope = curr;
      String defaultVal = "0";
      if (VarType.values()[i] != VarType.INT && VarType.values()[i] != VarType.INT64)
         defaultVal = "0.0";
      LLVMGenerator.declareGlobal(ID, typesString[i], defaultVal);
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
      LLVMGenerator.declareGlobal(ID, typesString[i], val.name);

      VariableScope curr = variables.scope;
      variables.scope = VariableScope.global;
      variables.put(ID, VarType.values()[i]);
      variables.scope = curr;
   }

   @Override
   public void exitAssign(LangXParser.AssignContext ctx) {
      String ID = ctx.ID().getText();
      Value v = stack.pop();
      Value val = variables.get(ID, ctx);

      if (val.isMalloc) {
         if (val.type != VarType.ANY) {
            LLVMGenerator.free(val.name);
         }
         int typeSize = 4;
         if (val.type == VarType.REAL || val.type == VarType.FLOAT64) {
            typeSize = 8;
         }
         LLVMGenerator.assignAny(val.name, typesString[v.type.ordinal()], v.name, typeSize);
         variables.putMalloc(ID, v.type);
         return;
      }

      if (val.type != v.type) {
         error(ctx.getStart().getLine(), "Incompatible types: expected " + val.type + ", got " + v.type);
         return;
      }

      LLVMGenerator.assign(val.name, v.name, typesString[v.type.ordinal()]);
   }

   @Override
   public void exitAssignStructKey(LangXParser.AssignStructKeyContext ctx) {
      String ID = ctx.ID(0).getText();
      String KEY = ctx.ID(1).getText();
      Value v = stack.pop();
      Value val = variables.get(ID, ctx);
      ArrayList<Value> list = structures.get(val.structID);

      int index = IntStream.range(0, list.size())
            .filter(i -> KEY.equals(list.get(i).name))
            .findFirst()
            .orElse(-1);

      int id = LLVMGenerator.loadStruct(ID, val.structID, index);
      LLVMGenerator.assignStruct(typesString[list.get(index).type.ordinal()], v.name, String.valueOf(id));
   }

   public void exitStructValue(LangXParser.StructValueContext ctx) {
      String ID = ctx.ID(0).getText();
      String KEY = ctx.ID(1).getText();

      Value val = variables.get(ID, ctx);
      ArrayList<Value> list = structures.get(val.structID);

      int index = IntStream.range(0, list.size())
            .filter(i -> KEY.equals(list.get(i).name))
            .findFirst()
            .orElse(-1);

      int id = LLVMGenerator.loadStruct(ID, val.structID, index);
      VarType type = list.get(index).type;
      int idCorrectType = LLVMGenerator.loadPtrToType("%" + id, typesString[type.ordinal()]);
      Value newVal = new Value("%" + idCorrectType, type);
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
      Value val = variables.get(ID, ctx);
      String id = variables.load(val);
      stack.push(new Value(id, val.type));
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
      String ID = ctx.ID().getText();
      Value val = variables.get(ID, ctx);
      variables.load(val);
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
            error(ctx.getStart().getLine(), "unknown variable " + ID);
      }
   }

   public void exitPrintStruct(LangXParser.PrintStructContext ctx) {
      String ID = ctx.ID(0).getText();
      String KEY = ctx.ID(1).getText();

      Value variable = variables.get(ID, ctx);
      ArrayList<Value> list = structures.get(variable.structID);

      int index = IntStream.range(0, list.size())
            .filter(i -> KEY.equals(list.get(i).name))
            .findFirst()
            .orElse(-1);

      int id = LLVMGenerator.loadStruct(ID, variable.structID, index);
      VarType type = list.get(index).type;
      int idCorrectType = LLVMGenerator.loadPtrToType("%" + id, typesString[type.ordinal()]);
      Value val = new Value("%" + idCorrectType, type);

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
            error(ctx.getStart().getLine(), "unknown variable " + ID);
      }
   }

   @Override
   public void exitRead(LangXParser.ReadContext ctx) {
      String ID = ctx.ID().getText();
      Value val = variables.get(ID, ctx);
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
      int newId = LLVMGenerator.call(id, typesString[type.ordinal()]);
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
      LLVMGenerator.call(id, typesString[type.ordinal()]);
   }

   @Override
   public void exitFparam(LangXParser.FparamContext ctx) {
      String ID = ctx.ID().getText();
      String funcType = ctx.funcType().getText();
      VarType type = VarType.valueOf(funcType.toUpperCase());

      Value val = new Value(ID, type);
      variables.scope = VariableScope.function;
      functions.put(ID, type);
      function = val;
      if (type != VarType.VOID) {
         LLVMGenerator.functionstartType(ID, typesString[type.ordinal()]);
      } else {
         LLVMGenerator.functionstart(ID);
      }
   }

   @Override
   public void exitReturn(LangXParser.ReturnContext ctx) {
      Value val = stack.pop();
      LLVMGenerator.addReturn(typesString[val.type.ordinal()], val.name);
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
         LLVMGenerator.icmp(val1.name, val2.name, typesString[val1.type.ordinal()]);
      } else {
         LLVMGenerator.fcmp(val1.name, val2.name, typesString[val1.type.ordinal()]);
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
         values += typesString[val.type.ordinal()] + ", ";
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

}
