package marco.lson.rules;

import marco.lson.LsonTokenizer;
import marco.lson.LsonTokenizer.TokenType;

public class NoTrailingComma implements Rule {

    @Override
    public boolean apply(LsonTokenizer.Token<?> before, LsonTokenizer.Token<?> current, LsonTokenizer.Token<?> after) {
        if (current.type() == TokenType.COMMA && (after.type() == TokenType.R_BRACKET || after.type() == TokenType.R_CURLY))
            return false;
        return true;
    }

    @Override
    public String failMessage() {
        return "Json doesn't allow trailing commas";
    }
}
