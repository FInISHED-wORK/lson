package marco.lson.rules;

import marco.lson.LsonTokenizer;
import marco.lson.LsonTokenizer.TokenType;

public class ExpectKeyBeforeColon implements Rule {
    @Override
    public boolean apply(LsonTokenizer.Token<?> before, LsonTokenizer.Token<?> current, LsonTokenizer.Token<?> after) {
        if (current.type() == TokenType.COLON && before.type() != TokenType.STRING) return false;
        return true;
    }

    @Override
    public String failMessage() {
        return "Expected a key string before the colon";
    }
}
