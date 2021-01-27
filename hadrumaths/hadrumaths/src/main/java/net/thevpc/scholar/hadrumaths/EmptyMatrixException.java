package net.thevpc.scholar.hadrumaths;

/**
 * Created by IntelliJ IDEA.
 * User: taha
 * Date: 6 juil. 2003
 * Time: 10:47:54
 */
public class EmptyMatrixException extends RuntimeException {
    public EmptyMatrixException() {
    }

    public EmptyMatrixException(String message) {
        super(message);
    }

    public EmptyMatrixException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyMatrixException(Throwable cause) {
        super(cause);
    }
}
