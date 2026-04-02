/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadrumaths.interop.derive;

import net.thevpc.nuts.reflect.NClassMap;
import net.thevpc.scholar.hadrumaths.AbstractFactory;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;

/**
 * @author vpc
 */
public class DeriveFactory extends AbstractFactory {
    private static final NClassMap<Object,ToDeriveString> map = NClassMap.of(Object.class, ToDeriveString.class, 3);

    static {
        register(ComplexMatrix.class, new MatrixToDeriveString());
        register(Complex.class, new ComplexToDeriveString());
    }

    private DeriveFactory() {
    }

    public static void register(Class clz, ToDeriveString t) {
        map.put(clz, t);
    }

    @SuppressWarnings("unchecked")
    public static String toDeriveString(Object o, ToDeriveStringParam... format) {
        return map.get(o.getClass()).toDeriveString(o, format);
    }
}
