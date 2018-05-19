package net.vpc.scholar.hadrumaths.util;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 1 déc. 2006 01:00:22
 */
public class TLogNull implements TLog {
    public static final TLogNull SILENT = new TLogNull();

    public TLogNull() {
    }

    public void trace(Object msg) {
    }

    public void error(Object msg) {
    }

    public void warning(Object msg) {
    }

    public void debug(Object msg) {
    }
}
