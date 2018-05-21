package net.vpc.scholar.hadrumaths.expeval;

public class MissingTokenException extends RuntimeException {
    public MissingTokenException() {
    }

    public MissingTokenException(String message) {
        super(message);
    }

    public MissingTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingTokenException(Throwable cause) {
        super(cause);
    }

    public MissingTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
