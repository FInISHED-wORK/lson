package marco.lson;

import marco.lson.exception.LsonParserException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RuleTest {

    @Test
    void noTrailingComma() {
        var json = "[{,}]";
        var exception = assertThrows(LsonParserException.class, () -> new LsonTokenizer(json));
        System.out.println(exception.getMessage());
    }

    @Test
    void expectStringBeforeColon() {
        var json = "{\n\t1:1\n}";
        var exception = assertThrows(LsonParserException.class, () -> new LsonTokenizer(json));
        System.out.println(exception.getMessage());
    }

    @Test
    void noColonAllowedInArray() {
        var json = "[:]";
        var exception = assertThrows(LsonParserException.class, () -> new LsonTokenizer(json));
        System.out.println(exception.getMessage());
    }

    @Test
    void expectedCommaAfterValue() {
        var json = "[,1]";
        var exception = assertThrows(LsonParserException.class, () -> new LsonTokenizer(json));
        System.out.println(exception.getMessage());
    }

    @Test
    void colonAfterObjectKey() {
        var json = "{\"Hi\", 1}";
        var exception = assertThrows(LsonParserException.class, () -> new LsonTokenizer(json));
        System.out.println(exception.getMessage());
    }

    @Test
    void validateString() {
        var json = "\"";
        var exception = assertThrows(LsonParserException.class, () -> new LsonTokenizer(json));
        System.out.println(exception.getMessage());
    }
}
