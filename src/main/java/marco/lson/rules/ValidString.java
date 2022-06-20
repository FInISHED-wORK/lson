package marco.lson.rules;

import marco.lson.LsonTokenizer;

public class ValidString implements Rule {

    private Cause cause;

    @Override
    public boolean apply(LsonTokenizer.Token<?> before, LsonTokenizer.Token<?> current, LsonTokenizer.Token<?> after) {
        if (current.type() == LsonTokenizer.TokenType.STRING && current.lex().equals("\"")) {
            this.cause = Cause.UNCLOSED;
            return false;
        }
        return true;
    }

    @Override
    public String failMessage() {
        return switch (this.cause) {
            case UNCLOSED -> "Found single double quote";
        };
    }

    private enum Cause {
        UNCLOSED
    }
}
