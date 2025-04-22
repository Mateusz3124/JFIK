public class Any implements VariableI {
    public Struct struct;
    public Value val;

    public Any(Value val) {
        this.val = val;
    }

    public Any(String id, VarType type) {
        Value var = new Value(id, type);
        this.val = var;
    }

    @Override
    public void store(Value givenValue) {
        if (val.type != VarType.ANY) {
            LLVMGenerator.free(val.name);
        }
        if (givenValue.type != VarType.STRUCT) {
            int typeSize = 4;
            if (val.type == VarType.REAL || val.type == VarType.FLOAT64) {
                typeSize = 8;
            }
            LLVMGenerator.assignAny(val.name, Const.typesLLVM[givenValue.type.ordinal()], givenValue.name, typeSize);
            val.type = givenValue.type;
            return;
        }
    }

    public void storeStruct(Struct struct) {
        LLVMGenerator.assignPtrPtr(struct.getValue().name, val.name);
        this.struct = struct;
    }

    @Override
    public String load() {
        if (val.type != VarType.STRUCT) {
            int anyReg = LLVMGenerator.loadPtr(val.name);
            int currReg = LLVMGenerator.load("%" + String.valueOf(anyReg), Const.typesLLVM[val.type.ordinal()]);
            return "%" + String.valueOf(currReg);
        } else {
            return this.struct.load();
        }
    }

    @Override
    public int hashCode() {
        return val.name.hashCode();
    }

    @Override
    public Value getValue() {
        return val;
    }

    public Struct geStruct() {
        return struct;
    }

    @Override
    public void setValue(Value val) {
        this.val = val;
    }

}
