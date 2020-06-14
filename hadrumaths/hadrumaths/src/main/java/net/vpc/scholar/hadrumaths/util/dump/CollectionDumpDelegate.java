package net.vpc.scholar.hadrumaths.util.dump;

import java.util.Collection;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 1 juin 2007 16:24:38
 */
public class CollectionDumpDelegate implements DumpDelegate {

    public CollectionDumpDelegate() {
    }

    public String getDumpString(Object object) {
        Dumper h = new Dumper(null);
        Collection m = (Collection) object;
        for (Object o : m) {
            h.add(o);
        }
        return h.toString();
    }
}
