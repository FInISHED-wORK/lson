package marco.lson.types;

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
        return ((LsonObject) this.get(index));
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
