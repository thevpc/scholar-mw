package net.thevpc.scholar.hadrumaths;

public class HException extends RuntimeException{
    public HException() {
    }

    public HException(String message) {
        super(message);
    }

    public HException(String message, Throwable cause) {
        super(message, cause);
    }

    public HException(Throwable cause) {
        super(cause);
    }

    public HException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
