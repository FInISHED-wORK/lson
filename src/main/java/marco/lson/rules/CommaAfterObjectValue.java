package marco.lson.rules;

import marco.lson.LsonTokenizer;
import marco.lson.LsonTokenizer.Scope;
import marco.lson.LsonTokenizer.TokenType;

import java.util.Arrays;
import java.util.List;

public class CommaAfterObjectValue implements Rule {

    private final List<TokenType> validAfter = Arrays.asList(
            TokenType.COMMA,
            TokenType.R_CURLY
    );

    @Override
    public boolean apply(LsonTokenizer.Token<?> before, LsonTokenizer.Token<?> current, LsonTokenizer.Token<?> after) {
        if (current.scope() == Scope.OBJECT)
            if (before.type() == TokenType.COLON && !validAfter.contains(after.type()))
                return false;
        return true;
    }

    @Override
    public String failMessage() {
        return "Object value requires comma or end of object after";
    }
}
