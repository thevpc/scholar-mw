package net.vpc.scholar.hadrumaths;

import java.io.IOException;

public class RuntimeIOException extends RuntimeException {
    public RuntimeIOException() {
    }

    public RuntimeIOException(String message) {
        super(message);
    }

    public RuntimeIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeIOException(IOException cause) {
        super(cause);
    }

    public RuntimeIOException(String message, IOException cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
