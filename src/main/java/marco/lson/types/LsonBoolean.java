package marco.lson.types;

public record LsonBoolean(boolean bool) implements ILsonValue {

    @Override
    public String toString() {
        return String.valueOf(this.bool);
    }
}
