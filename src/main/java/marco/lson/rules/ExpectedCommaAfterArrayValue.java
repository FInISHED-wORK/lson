package marco.lson.rules;

import marco.lson.LsonTokenizer;

import java.util.Arrays;
import java.util.List;

public class ExpectedCommaAfterArrayValue implements Rule {

    private final List<LsonTokenizer.TokenType> allowedAfter = Arrays.asList(
            LsonTokenizer.TokenType.BOOLEAN,
            LsonTokenizer.TokenType.STRING,
            LsonTokenizer.TokenType.NUMBER,
            LsonTokenizer.TokenType.NULL,
            LsonTokenizer.TokenType.R_BRACKET,
            LsonTokenizer.TokenType.R_CURLY
    );

    @Override
    public boolean apply(LsonTokenizer.Token<?> before, LsonTokenizer.Token<?> current, LsonTokenizer.Token<?> after) {
        if (current.type() == LsonTokenizer.TokenType.COMMA && !allowedAfter.contains(before.type()))
            return false;
        return true;
    }

    @Override
    public String failMessage() {
        return "Comma is allowed after this types: " + allowedAfter;
    }
}
