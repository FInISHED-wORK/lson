package marco.lson.types;

import marco.lson.exception.LsonException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LsonObject implements ILsonValue {

    private final Map<String, ILsonValue> values = new HashMap<>();

    public void add(String key, ILsonValue value) {
        values.put(key, value);
    }

    public void add(String key, String value) {
        this.add(key, new LsonString(value));
    }

    public void add(String key, double value) {
        this.add(key, new LsonNumber(value));
    }

    public void add(String key, boolean value) {
        this.add(key, new LsonBoolean(value));
    }

    public ILsonValue getRawValue(String key) {
        return this.values.get(key);
    }

    public String asString(String key) {
        return ((LsonString) this.getRawValue(key)).value();
    }

    public LsonArray asArray(String key) {
        if (!(this.getRawValue(key) instanceof LsonArray))
            throw new LsonException("Can't convert " + this.getRawValue(key).getClass().getSimpleName() + " to array.");
        return (LsonArray) this.getRawValue(key);
    }

    public LsonObject asObject(String key) {
        if (!(this.getRawValue(key) instanceof LsonObject))
            throw new LsonException("Can't convert " + this.getRawValue(key).getClass().getSimpleName() + " to object.");
        return (LsonObject) this.getRawValue(key);
    }

    public boolean asBool(String key) {
        return ((LsonBoolean) this.getRawValue(key)).bool();
    }

    public double asNumber(String key) {
        return ((LsonNumber) this.getRawValue(key)).number();
    }

    public boolean hasKey(String key) {
        return this.values.containsKey(key);
    }

    public boolean isNull(String key) {
        return this.getRawValue(key) == null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{\n");
        if (!values.isEmpty()) {
            for (var entry : values.entrySet()) {
                builder.append("\t").append("\"").append(entry.getKey()).append("\": ").append(entry.getValue()).append(",\n");
            }
            builder.deleteCharAt(builder.length() - 2).append("}");
        } else {
            builder.append("}");
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LsonObject that = (LsonObject) o;
        return Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }
}
