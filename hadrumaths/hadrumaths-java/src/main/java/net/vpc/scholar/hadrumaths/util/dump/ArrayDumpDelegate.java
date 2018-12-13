package net.vpc.scholar.hadrumaths.util.dump;

import java.lang.reflect.Array;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 1 juin 2007 16:24:38
 */
public class ArrayDumpDelegate implements DumpDelegate {

    public ArrayDumpDelegate() {
    }

    public String getDumpString(Object object) {
        Dumper h = new Dumper("", Dumper.Type.ARRAY);
        int len = Array.getLength(object);
        for (int i = 0; i < len; i++) {
            h.add(Array.get(object, i));
        }
        return h.toString();
    }
}
