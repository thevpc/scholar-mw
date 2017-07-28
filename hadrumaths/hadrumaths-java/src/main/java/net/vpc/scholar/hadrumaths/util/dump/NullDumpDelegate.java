package net.vpc.scholar.hadrumaths.util.dump;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 1 juin 2007 16:24:38
 */
public class NullDumpDelegate implements DumpDelegate{
    private String str;

    public NullDumpDelegate() {
        this("");
    }

    public NullDumpDelegate(String str) {
        this.str = str;
    }

    public String getDumpString(Object object) {
        return String.valueOf(str);
    }
}
