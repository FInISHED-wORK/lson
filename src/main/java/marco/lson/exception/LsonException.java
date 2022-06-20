package marco.lson.exception;

public class LsonException extends RuntimeException {

    public LsonException() {
        super();
    }

    public LsonException(String message) {
        super(message);
    }

    public LsonException(String message, Throwable cause) {
        super(message, cause);
    }

    public LsonException(Throwable cause) {
        super(cause);
    }

    public LsonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
