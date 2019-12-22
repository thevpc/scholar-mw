/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.interop.derive;

import net.vpc.common.util.ClassMap;
import net.vpc.scholar.hadrumaths.AbstractFactory;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.ComplexMatrix;

/**
 * @author vpc
 */
public class DeriveFactory extends AbstractFactory {
    private static final ClassMap<ToDeriveString> map = new ClassMap<ToDeriveString>(Object.class, ToDeriveString.class, 3);

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
