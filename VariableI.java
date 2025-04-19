public interface VariableI {

    public void store(Value val);

    public String load();

    public Value getValue();

    public void setValue(Value val);
}
