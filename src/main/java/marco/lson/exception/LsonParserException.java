package marco.lson.exception;

import marco.lson.LsonTokenizer.Token;

public class LsonParserException extends RuntimeException {

    public LsonParserException(Token<?> token) {
        this("Error at " + token.line() + ":" + token.start() + ": " + token.type());
    }

    public LsonParserException() {
        super();
    }

    public LsonParserException(String message) {
        super(message);
    }

    public LsonParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public LsonParserException(Throwable cause) {
        super(cause);
    }

    public LsonParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
