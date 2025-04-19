public class Variable implements VariableI {
    public Value val;

    public Variable(Value val) {
        this.val = val;
    }

    public Variable(String id, VarType type) {
        this.val = new Value(id, type);
    }

    @Override
    public void store(Value givenValue) {
        LLVMGenerator.assign(val.name, givenValue.name, Const.typesLLVM[val.type.ordinal()]);
    }

    @Override
    public String load() {
        int currReg = LLVMGenerator.load(val.name, Const.typesLLVM[val.type.ordinal()]);
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
