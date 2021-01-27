package net.thevpc.scholar.hadrumaths.util.dump;

import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.util.PlatformUtils;

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
        int dim = PlatformUtils.getArrayDimension(object.getClass());
        Class rct = PlatformUtils.getArrayRootComponentType(object.getClass());
        switch (len){
            case 0:return "[]";
            case 1:return "["+ Maths.dump(Array.get(object, 0))+"]";
            default:{
                StringBuilder sb=new StringBuilder();
                sb.append("[").append(Maths.dump(Array.get(object, 0)));
                for (int i = 1; i < len; i++) {
                    sb.append(", ").append(Maths.dump(Array.get(object, i)));
                }
                sb.append("]");
            }
        }
        return h.toString();
    }
}
