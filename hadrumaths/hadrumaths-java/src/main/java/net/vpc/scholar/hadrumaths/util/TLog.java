package net.vpc.scholar.hadrumaths.util;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 1 déc. 2006 00:58:29
 */
public interface TLog {
    public void trace(Object msg);

    public void error(Object msg);

    public void warning(Object msg);

    public void debug(Object msg);
}
