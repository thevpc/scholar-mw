package net.thevpc.scholar.hadrumaths.util.log;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 1 déc. 2006 00:58:29
 */
public interface TLog {
    void trace(Object msg);

    void error(Object msg);

    void warning(Object msg);

    void debug(Object msg);
}
