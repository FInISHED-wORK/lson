package marco.lson.rules;

import marco.lson.LsonTokenizer.Scope;
import marco.lson.LsonTokenizer.Token;
import marco.lson.LsonTokenizer.TokenType;

import java.util.Arrays;
import java.util.List;

public class ColonAfterObjectKey implements Rule {

    private final List<TokenType> needBefore = Arrays.asList(
            TokenType.L_BRACKET,
            TokenType.L_CURLY,
            TokenType.COMMA
    );

    @Override
    public boolean apply(Token<?> before, Token<?> current, Token<?> after) {
        if (current.scope() == Scope.OBJECT && current.type() == TokenType.STRING)
            if (needBefore.contains(before.type()) && after.type() != TokenType.COLON)
                return false;
        return true;
    }

    @Override
    public String failMessage() {
        return "Colon is required after an object key";
    }
}
