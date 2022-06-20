package marco.lson;

import marco.lson.LsonTokenizer.Scope;
import marco.lson.exception.LsonException;
import marco.lson.types.ILsonValue;
import marco.lson.types.LsonArray;
import marco.lson.types.LsonObject;

import java.io.File;
import java.nio.file.Files;

public class Lson {

    private ILsonValue json;

    public Lson(File input) {
        try {
            var str = Files.readString(input.toPath());
            var parser = new LsonParser(str);
            this.json = parser.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Lson(String input) {
        var parser = new LsonParser(input);
        this.json = parser.parse();
    }

    public LsonObject asObject() {
        if (!this.isValidScope(Scope.OBJECT)) throw new LsonException("Given json is an array.");
        return ((LsonObject) this.json);
    }

    public LsonArray asArray() {
        if (!this.isValidScope(Scope.ARRAY)) throw new LsonException("Given json is an object.");
        return ((LsonArray) this.json);
    }

    private boolean isArray() {
        return this.json instanceof LsonArray;
    }

    private boolean isObject() {
        return this.json instanceof LsonObject;
    }

    private boolean isValidScope(Scope scope) {
        return switch (scope) {
            case ARRAY -> this.isArray();
            case OBJECT -> this.isObject();
            default -> false;
        };
    }
}
