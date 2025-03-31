
import java.util.HashMap;
import java.util.Stack;

enum VarType {
   INT, INT64, REAL, UNKNOWN
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

   HashMap<String, VarType> variables = new HashMap<String, VarType>();
   Stack<Value> stack = new Stack<Value>();

   @Override
   public void exitAssignType(LangXParser.AssignTypeContext ctx) {
      String ID = ctx.ID().getText();
      String type = ctx.type().getText();

      if (type.equals("int")) {
         variables.put(ID, VarType.INT);
         LLVMGenerator.declare_i32(ID);
      } else if (type.equals("int64")) {
         variables.put(ID, VarType.INT64);
         LLVMGenerator.declare_i64(ID);
      } else if (type.equals("real")) {
         variables.put(ID, VarType.REAL);
         LLVMGenerator.declare_double(ID);
      } else {
         error(ctx.getStart().getLine(), "unknown type " + type);
      }
   }

   @Override
   public void exitAssign(LangXParser.AssignContext ctx) {
      String ID = ctx.ID().getText();
      Value v = stack.pop();
      VarType type = variables.get(ID);
      if (type != v.type) {
         error(ctx.getStart().getLine(), "Incompatible types expected type " + type + " got type " + v.type);
      }
      if (type == VarType.INT) {
         LLVMGenerator.assign_i32(ID, v.name);
      }
      if (type == VarType.INT64) {
         LLVMGenerator.assign_i64(ID, v.name);
      }
      if (type == VarType.REAL) {
         LLVMGenerator.assign_double(ID, v.name);
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
   public void exitAdd(LangXParser.AddContext ctx) {
      Value v1 = stack.pop();
      Value v2 = stack.pop();
      if (v1.type == v2.type) {
         if (v1.type == VarType.INT) {
            LLVMGenerator.add_i32(v1.name, v2.name);
            stack.push(new Value("%" + (LLVMGenerator.reg - 1), VarType.INT));
         }
         if (v1.type == VarType.INT64) {
            LLVMGenerator.add_i64(v1.name, v2.name);
            stack.push(new Value("%" + (LLVMGenerator.reg - 1), VarType.INT64));
         }
         if (v1.type == VarType.REAL) {
            LLVMGenerator.add_double(v1.name, v2.name);
            stack.push(new Value("%" + (LLVMGenerator.reg - 1), VarType.REAL));
         }
      } else {
         error(ctx.getStart().getLine(), "add type mismatch");
      }
   }

   @Override
   public void exitSub(LangXParser.SubContext ctx) {
      Value v1 = stack.pop();
      Value v2 = stack.pop();
      if (v1.type == v2.type) {
         if (v1.type == VarType.INT) {
            LLVMGenerator.sub_i32(v2.name, v1.name);
            stack.push(new Value("%" + (LLVMGenerator.reg - 1), VarType.INT));
         }
         if (v1.type == VarType.INT64) {
            LLVMGenerator.sub_i64(v2.name, v1.name);
            stack.push(new Value("%" + (LLVMGenerator.reg - 1), VarType.INT64));
         }
         if (v1.type == VarType.REAL) {
            LLVMGenerator.sub_double(v2.name, v1.name);
            stack.push(new Value("%" + (LLVMGenerator.reg - 1), VarType.REAL));
         }
      } else {
         error(ctx.getStart().getLine(), "subtraction type mismatch");
      }
   }

   @Override
   public void exitMult(LangXParser.MultContext ctx) {
      Value v1 = stack.pop();
      Value v2 = stack.pop();
      if (v1.type == v2.type) {
         if (v1.type == VarType.INT) {
            LLVMGenerator.mult_i32(v1.name, v2.name);
            stack.push(new Value("%" + (LLVMGenerator.reg - 1), VarType.INT));
         }
         if (v1.type == VarType.INT64) {
            LLVMGenerator.mult_i64(v1.name, v2.name);
            stack.push(new Value("%" + (LLVMGenerator.reg - 1), VarType.INT64));
         }
         if (v1.type == VarType.REAL) {
            LLVMGenerator.mult_double(v1.name, v2.name);
            stack.push(new Value("%" + (LLVMGenerator.reg - 1), VarType.REAL));
         }
      } else {
         error(ctx.getStart().getLine(), "mult type mismatch");
      }
   }

   @Override
   public void exitDiv(LangXParser.DivContext ctx) {
      Value v1 = stack.pop();
      Value v2 = stack.pop();
      if (v1.type == v2.type) {
         if (v1.type == VarType.INT) {
            LLVMGenerator.div_i32(v2.name, v1.name);
            stack.push(new Value("%" + (LLVMGenerator.reg - 1), VarType.INT));
         }
         if (v1.type == VarType.INT64) {
            LLVMGenerator.div_i64(v2.name, v1.name);
            stack.push(new Value("%" + (LLVMGenerator.reg - 1), VarType.INT64));
         }
         if (v1.type == VarType.REAL) {
            LLVMGenerator.div_double(v2.name, v1.name);
            stack.push(new Value("%" + (LLVMGenerator.reg - 1), VarType.REAL));
         }
      } else {
         error(ctx.getStart().getLine(), "mult type mismatch");
      }
   }

   @Override
   public void exitToint(LangXParser.TointContext ctx) {
      Value v = stack.pop();
      if (v.type == VarType.REAL) {
         LLVMGenerator.fptosi(v.name);
         stack.push(new Value("%" + (LLVMGenerator.reg - 1), VarType.INT));
      } else if (v.type == VarType.INT64) {
         LLVMGenerator.sext64(v.name);
         stack.push(new Value("%" + (LLVMGenerator.reg - 1), VarType.INT));
      }
   }

   @Override
   public void exitToint64(LangXParser.Toint64Context ctx) {
      Value v = stack.pop();
      if (v.type == VarType.REAL) {
         LLVMGenerator.fptosi64(v.name);
         stack.push(new Value("%" + (LLVMGenerator.reg - 1), VarType.INT64));
      } else if (v.type == VarType.INT) {
         LLVMGenerator.sext(v.name);
         stack.push(new Value("%" + (LLVMGenerator.reg - 1), VarType.INT64));
      }
   }

   @Override
   public void exitToreal(LangXParser.TorealContext ctx) {
      Value v = stack.pop();
      if (v.type == VarType.INT) {
         LLVMGenerator.sitofp(v.name);
         stack.push(new Value("%" + (LLVMGenerator.reg - 1), VarType.REAL));
      } else if (v.type == VarType.INT64) {
         LLVMGenerator.si64tofp(v.name);
         stack.push(new Value("%" + (LLVMGenerator.reg - 1), VarType.REAL));
      }
   }

   @Override
   public void exitPrint(LangXParser.PrintContext ctx) {
      String ID = ctx.ID().getText();
      VarType type = variables.get(ID);
      if (type != null) {
         if (type == VarType.INT) {
            LLVMGenerator.printf_i32(ID);
         }
         if (type == VarType.INT64) {
            LLVMGenerator.printf_i64(ID);
         }
         if (type == VarType.REAL) {
            LLVMGenerator.printf_double(ID);
         }
      } else {
         error(ctx.getStart().getLine(), "unknown variable " + ID);
      }
   }

   @Override
   public void exitRead(LangXParser.ReadContext ctx) {
      String ID = ctx.ID().getText();
      VarType type = variables.get(ID);
      if (type == VarType.INT) {
         LLVMGenerator.scanf_i32(ID);
      } else if (type == VarType.INT64) {
         LLVMGenerator.scanf_i64(ID);
      } else {
         LLVMGenerator.scanf_double(ID);
      }
   }

   void error(int line, String msg) {
      System.err.println("Error, line " + line + ", " + msg);
      System.exit(1);
   }

}
