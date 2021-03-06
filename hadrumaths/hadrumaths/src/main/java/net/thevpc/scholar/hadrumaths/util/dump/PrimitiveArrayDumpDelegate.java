package net.thevpc.scholar.hadrumaths.util.dump;

import net.thevpc.scholar.hadrumaths.Maths;

import java.lang.reflect.Array;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 1 juin 2007 16:24:38
 */
public class PrimitiveArrayDumpDelegate implements DumpDelegate {

    public PrimitiveArrayDumpDelegate() {
    }

    public String getDumpString(Object object) {
        StringBuilder sb = new StringBuilder();
        int len = Array.getLength(object);
        sb.append("[");
        for (int i = 0; i < len; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(Maths.dump(Array.get(object, i)));
        }
        sb.append("]");
        return sb.toString();
    }
}
