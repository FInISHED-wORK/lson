package marco.lson.rules;

import marco.lson.LsonTokenizer.Token;

public interface Rule {

    boolean apply(Token<?> before, Token<?> current, Token<?> after);

    String failMessage();
}
