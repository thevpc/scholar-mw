package net.vpc.scholar.hadrumaths.dump;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 1 juin 2007 16:24:38
 */
public class ToStringDumpDelegate implements DumpDelegate {

    public ToStringDumpDelegate() {
    }

    public String getDumpString(Object object) {
        return String.valueOf(object);
    }
}
