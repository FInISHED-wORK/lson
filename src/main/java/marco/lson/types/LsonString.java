package marco.lson.types;

public record LsonString(String value) implements ILsonValue {

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}
