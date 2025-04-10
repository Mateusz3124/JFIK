
import java.util.HashMap;
import java.util.Stack;
import org.antlr.v4.runtime.ParserRuleContext;

enum VarType {
   INT, INT64, REAL, FLOAT32, FLOAT64, UNKNOWN
}

class Value {
   public String name;
   public VarType type;

   public Value(String name, VarType type) {
      this.name = name;
      this.type = type;
   }
}

public class LLVMActions extends LangXBaseListener {

   HashMap<String, VarType> variables = new HashMap<>();
   Stack<Value> stack = new Stack<>();

   @Override
   public void exitAssignType(LangXParser.AssignTypeContext ctx) {
      String ID = ctx.ID().getText();
      String type = ctx.type().getText();

      switch (type) {
         case "int":
            variables.put(ID, VarType.INT);
            LLVMGenerator.declare_i32(ID);
            break;
         case "int64":
            variables.put(ID, VarType.INT64);
            LLVMGenerator.declare_i64(ID);
            break;
         case "real":
            variables.put(ID, VarType.REAL);
            LLVMGenerator.declare_double(ID);
            break;
         case "float32":
            variables.put(ID, VarType.FLOAT32);
            LLVMGenerator.declare_float32(ID);
            break;
         case "float64":
            variables.put(ID, VarType.FLOAT64);
            LLVMGenerator.declare_float64(ID);
            break;
         default:
            error(ctx.getStart().getLine(), "unknown type " + type);
      }
   }

   @Override
   public void exitAssign(LangXParser.AssignContext ctx) {
      String ID = ctx.ID().getText();
      Value v = stack.pop();
      VarType type = variables.get(ID);

      if (type != v.type) {
         if (type == VarType.REAL && v.type == VarType.INT64) {
            v = castToReal(v);
         } else if (type == VarType.INT64 && v.type == VarType.REAL) {
            v = castToInt64(v);
         } else if (type == VarType.FLOAT32 && v.type == VarType.REAL) {
            v = castToFloat32(v);
         } else if (type == VarType.FLOAT64 && v.type == VarType.REAL) {
            v = castToFloat64(v);
         } else if (type == VarType.INT && v.type == VarType.REAL) {
            v = castToInt(v);
         } else {
		if(type == null) {
		error(ctx.getStart().getLine(), "Value not initialized");
		}
            error(ctx.getStart().getLine(), "Incompatible types: expected " + type + ", got " + v.type);
            return;
         }
      }
      switch (type) {
         case INT: LLVMGenerator.assign_i32(ID, v.name); break;
         case INT64: LLVMGenerator.assign_i64(ID, v.name); break;
         case REAL: LLVMGenerator.assign_double(ID, v.name); break;
         case FLOAT32: LLVMGenerator.assign_float32(ID, v.name); break;
         case FLOAT64: LLVMGenerator.assign_float64(ID, v.name); break;
      }
   }

   @Override
   public void exitProg(LangXParser.ProgContext ctx) {
      System.out.println(LLVMGenerator.generate());
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

      String result = "%" + (LLVMGenerator.reg);
      switch (v1.type) {
         case INT:
            callOp(op + "_i32", v2.name, v1.name); break;
         case INT64:
            callOp(op + "_i64", v2.name, v1.name); break;
         case REAL:
            callOp(op + "_double", v2.name, v1.name); break;
         case FLOAT32:
            callOp(op + "_float32", v2.name, v1.name); break;
         case FLOAT64:
            callOp(op + "_float64", v2.name, v1.name); break;
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

   @Override
   public void exitPrint(LangXParser.PrintContext ctx) {
      String ID = ctx.ID().getText();
      VarType type = variables.get(ID);
      switch (type) {
         case INT: LLVMGenerator.printf_i32(ID); break;
         case INT64: LLVMGenerator.printf_i64(ID); break;
         case REAL: LLVMGenerator.printf_double(ID); break;
         case FLOAT32: LLVMGenerator.printf_float32(ID); break;
         case FLOAT64: LLVMGenerator.printf_float64(ID); break;
         default: error(ctx.getStart().getLine(), "unknown variable " + ID);
      }
   }

   @Override
   public void exitRead(LangXParser.ReadContext ctx) {
      String ID = ctx.ID().getText();
      VarType type = variables.get(ID);
      switch (type) {
         case INT: LLVMGenerator.scanf_i32(ID); break;
         case INT64: LLVMGenerator.scanf_i64(ID); break;
         case REAL: LLVMGenerator.scanf_double(ID); break;
         case FLOAT32: LLVMGenerator.scanf_float32(ID); break;
         case FLOAT64: LLVMGenerator.scanf_float64(ID); break;
         default: error(ctx.getStart().getLine(), "unknown variable " + ID);
      }
   }

   void error(int line, String msg) {
      System.err.println("Error, line " + line + ": " + msg);
      System.exit(1);
   }

   private Value castToReal(Value v) {
      String input = v.name;

      // If it's not already a register (e.g., a constant), lift it to a real value first
      //if (!input.startsWith("%")) {
       //  String constReg = "%" + LLVMGenerator.reg++;
        // if (input.matches("^-?\\d+$")) {  // If it's an integer constant
         //   LLVMGenerator.main_text += constReg + " = sitofp i64 " + input + " to double\n"; // Cast integer to real
         //} else {  // If it's already a floating-point number
          //  LLVMGenerator.main_text += constReg + " = fadd double 0.0, " + input + "\n"; // Convert floating-point to double
         //}
         //input = constReg;
	 //return new Value(constReg, VarType.REAL);
      //}

      String result = "%" + LLVMGenerator.reg++;
      LLVMGenerator.main_text += result + " = sitofp i64 " + input + " to double\n";  // Correct cast for integer to floating-point
      return new Value(result, VarType.REAL); // Return a REAL type value
   }

   private Value castToInt64(Value v) {
      String input = v.name;

      // If it's not already a register, convert it to real first
      //if (!input.startsWith("%")) {
        // String constReg = "%" + LLVMGenerator.reg++;
        // if (input.matches("^-?\\d+$")) {  // If it's an integer constant
         //   LLVMGenerator.main_text += constReg + " = fadd double 0.0, " + input + ".0\n"; // Treat integer as real (double)
         ///} else {  // If it's already a floating-point number
           /// LLVMGenerator.main_text += constReg + " = fadd double 0.0, " + input + "\n"; // Convert to double
        // }
         //input = constReg;
	   //     return new Value(constReg, VarType.INT64);
//      }

      String result = "%" + LLVMGenerator.reg++;
      LLVMGenerator.main_text += result + " = fptosi double " + input + " to i64\n";  // Cast floating-point to integer
      return new Value(result, VarType.INT64); // Return INT64 type value
   }

   private Value castToFloat32(Value v) {
      String input = v.name;

      // If it's a constant, convert it to a real and then cast it to float32
//      if (!input.startsWith("%")) {
  //       String constReg = "%" + LLVMGenerator.reg++;
    //     if (input.matches("^-?\\d+$")) { // If it's an integer constant
      //      LLVMGenerator.main_text += constReg + " = fadd double 0.0, " + input + ".0\n"; // Treat as real (double)
       //  } else {  // If it's already a floating point number
        //    LLVMGenerator.main_text += constReg + " = fadd double 0.0, " + input + "\n"; // Convert to double
        // }
         //input = constReg;
	  //      return new Value(constReg, VarType.FLOAT32);
      //}

      String result = "%" + LLVMGenerator.reg++;
      LLVMGenerator.main_text += result + " = fptosi double " + input + " to float\n"; // Convert REAL to FLOAT32
      return new Value(result, VarType.FLOAT32); // Return FLOAT32 type value
   }

   private Value castToFloat64(Value v) {
      String input = v.name;

      // If it's a constant, convert it to a real and then cast it to float64
      //if (!input.startsWith("%")) {
        // String constReg = "%" + LLVMGenerator.reg++;
         //if (input.matches("^-?\\d+$")) { // If it's an integer constant
          //  LLVMGenerator.main_text += constReg + " = fadd double 0.0, " + input + ".0\n"; // Treat integer as real (double)
         //} else {  // If it's already a floating point number
          //  LLVMGenerator.main_text += constReg + " = fadd double 0.0, " + input + "\n"; // Convert to double
         //}
         //input = constReg;
	  //      return new Value(constReg, VarType.FLOAT64);
      //}

      String result = "%" + LLVMGenerator.reg++;
      LLVMGenerator.main_text += result + " = fptosi double " + input + " to double\n"; // Convert REAL to FLOAT64
      return new Value(result, VarType.FLOAT64); // Return FLOAT64 type value
   }

   private Value castToInt(Value v) {
      String input = v.name;

      // If it's a constant, convert it to a real and then cast it to int
     // if (!input.startsWith("%")) {
        // String constReg = "%" + LLVMGenerator.reg++;
        // if (input.matches("^-?\\d+$")) { // If it's an integer constant
           // LLVMGenerator.main_text += constReg + " = fadd double 0.0, " + input + ".0\n"; // Treat integer as real (double)
         //} else {  // If it's already a floating point number
          //  LLVMGenerator.main_text += constReg + " = fadd double 0.0, " + input + "\n"; // Convert to double
         //}
         //input = constReg;
	//        return new Value(constReg, VarType.INT);
      //}

      String result = "%" + LLVMGenerator.reg++;
      LLVMGenerator.main_text += result + " = fptosi double " + input + " to i32\n"; // Convert REAL to INT (i32)
      return new Value(result, VarType.INT); // Return INT type value
   }


}
