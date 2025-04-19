import java.util.ArrayList;
import java.util.stream.IntStream;

public class Struct implements VariableI {

    public Value val;
    public ArrayList<Value> keys;
    public String structId;

    public Struct(Value val) {
        this.val = val;
    }

    public Struct(String id, VarType type) {
        this.val = new Value(id, type);
    }

    public Struct(String id, ArrayList<Value> keys, String structId) {
        this.val = new Value(id, VarType.STRUCT);
        this.keys = keys;
        this.structId = structId;
    }

    @Override
    public void store(Value val) {
        throw new Error("should not use this method give key");
    }

    public void store(Value val, String key) {
        Value keyVal = loadKey(key);

        LLVMGenerator.assignStruct(Const.typesLLVM[keyVal.type.ordinal()], val.name, keyVal.name);
    }

    @Override
    public String load() {
        int currReg = LLVMGenerator.loadPtr(val.name);
        return "%" + String.valueOf(currReg);
    }

    private Value loadKey(String key) {
        int index = IntStream.range(0, keys.size())
                .filter(i -> key.equals(keys.get(i).name))
                .findFirst()
                .orElse(-1);

        int id = LLVMGenerator.loadStruct(val.name, structId, index);
        VarType type = keys.get(index).type;
        return new Value("%" + id, type);
    }

    public String loadKeyToValue(String key) {
        Value val = loadKey(key);
        int idCorrectType = LLVMGenerator.loadPtrToType(val.name, Const.typesLLVM[val.type.ordinal()]);
        return "%" + String.valueOf(idCorrectType);
    }

    public VarType getKeyType(String key) {
        for (Value val : keys) {
            if (val.name.equals(key)) {
                return val.type;
            }
        }
        throw new Error("key: " + key + " not found");
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
