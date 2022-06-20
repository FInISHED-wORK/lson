package marco.lson.types;

import java.util.Objects;

public record LsonNumber(double number) implements ILsonValue {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LsonNumber that = (LsonNumber) o;
        return Objects.equals(number, that.number);
    }

    @Override
    public String toString() {
        return String.valueOf(this.number);
    }
}
