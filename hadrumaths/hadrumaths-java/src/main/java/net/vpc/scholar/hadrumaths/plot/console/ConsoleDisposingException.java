package net.vpc.scholar.hadrumaths.plot.console;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 10 janv. 2007 01:11:46
 */
public class ConsoleDisposingException extends IllegalStateException{

    public ConsoleDisposingException() {
    }

    public ConsoleDisposingException(Throwable cause) {
        super(cause);
    }

    public ConsoleDisposingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConsoleDisposingException(String s) {
        super(s);
    }
}
