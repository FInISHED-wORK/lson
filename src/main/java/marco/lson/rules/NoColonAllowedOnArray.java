package marco.lson.rules;

import marco.lson.LsonTokenizer;
import marco.lson.LsonTokenizer.Token;
import marco.lson.LsonTokenizer.TokenType;

public class NoColonAllowedOnArray implements Rule {

    @Override
    public boolean apply(Token<?> before, Token<?> current, Token<?> after) {
        if (current.type() == TokenType.COLON && current.scope() == LsonTokenizer.Scope.ARRAY)
            return false;
        return true;
    }

    @Override
    public String failMessage() {
        return "Json Arrays don't work with key values";
    }
}
