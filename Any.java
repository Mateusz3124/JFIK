public class Any implements VariableI {
    public Value val;

    public Any(Value val) {
        this.val = val;
    }

    public Any(String id, VarType type) {
        this.val = new Value(id, type);
    }

    @Override
    public void store(Value givenValue) {
        if (val.type != VarType.ANY) {
            LLVMGenerator.free(val.name);
        }
        int typeSize = 4;
        if (val.type == VarType.REAL || val.type == VarType.FLOAT64) {
            typeSize = 8;
        }
        LLVMGenerator.assignAny(val.name, Const.typesLLVM[givenValue.type.ordinal()], givenValue.name, typeSize);
        val.type = givenValue.type;
        return;
    }

    @Override
    public String load() {
        int anyReg = LLVMGenerator.loadPtr(val.name);
        int currReg = LLVMGenerator.load("%" + String.valueOf(anyReg), Const.typesLLVM[val.type.ordinal()]);
        return "%" + String.valueOf(currReg);
    }

    @Override
    public int hashCode() {
        return val.name.hashCode();
    }

    @Override
    public Value getValue() {
        return val;
    }

    @Override
    public void setValue(Value val) {
        this.val = val;
    }

}
