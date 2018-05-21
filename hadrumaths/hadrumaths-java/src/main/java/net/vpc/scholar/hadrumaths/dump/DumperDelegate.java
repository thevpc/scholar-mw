package net.vpc.scholar.hadrumaths.dump;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 1 juin 2007 16:24:38
 */
public class DumperDelegate implements DumpDelegate {
    public static final DumperDelegate INSTANCE = new DumperDelegate();

    public DumperDelegate() {
    }

    public String getDumpString(Object object) {
        return new Dumper(object).toString();
    }
}
