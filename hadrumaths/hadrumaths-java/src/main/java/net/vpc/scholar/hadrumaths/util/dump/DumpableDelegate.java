package net.vpc.scholar.hadrumaths.util.dump;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 1 juin 2007 16:24:38
 */
public class DumpableDelegate implements DumpDelegate {

    public DumpableDelegate() {
    }

    public String getDumpString(Object object) {
        return ((Dumpable) object).dump();
    }
}
