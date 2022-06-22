package marco.lson.types;

import marco.lson.exception.LsonException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LsonArray implements ILsonValue {

    private final List<ILsonValue> values = new ArrayList<>();

    public LsonArray(ILsonValue... values) {
        for (ILsonValue value : values) {
            this.add(value);
        }
    }

    public ILsonValue get(int index) {
        return this.values.get(index);
    }

    public void add(ILsonValue value) {
        this.values.add(value);
    }

    public LsonObject indexAsObject(int index) {
        if (!(this.get(index) instanceof LsonObject))
            throw new LsonException("Can't convert " + this.get(index).getClass().getSimpleName() + " to object.");
        return ((LsonObject) this.get(index));
    }

    public LsonArray indexAsArray(int index) {
        if (!(this.get(index) instanceof LsonArray))
            throw new LsonException("Can't convert " + this.get(index).getClass().getSimpleName() + " to array.");
        return (LsonArray) this.get(index);
    }

    public double indexAsNumber(int index) {
        if (!(this.get(index) instanceof LsonNumber))
            throw new LsonException("Can't convert " + this.get(index).getClass().getSimpleName() + " to object.");
        return ((LsonNumber) this.get(index)).number();
    }

    public String indexAsString(int index) {
        if (!(this.get(index) instanceof LsonString))
            throw new LsonException("Can't convert " + this.get(index).getClass().getSimpleName() + " to string.");
        return ((LsonString) this.get(index)).value();
    }

    public boolean indexAsBool(int index) {
        if (!(this.get(index) instanceof LsonBoolean))
            throw new LsonException("Can't convert " + this.get(index).getClass().getSimpleName() + " to boolean.");
        return ((LsonBoolean) this.get(index)).bool();
    }

    public boolean indexIsNull(int index) {
        return this.get(index) == null;
    }

    public List<ILsonValue> getValues() {
        return this.values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LsonArray lsonArray = (LsonArray) o;
        return Objects.equals(values, lsonArray.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

    @Override
    public String toString() {
        var build = new StringBuilder("[\n");

        if (!values.isEmpty()) {
            for (var value : values) {
                build.append("\t").append(value.toString()).append(",\n");
            }
            build.deleteCharAt(build.length() - 2).append("]");
        } else {
            build.append("]");
        }
        return build.toString();
    }
}
