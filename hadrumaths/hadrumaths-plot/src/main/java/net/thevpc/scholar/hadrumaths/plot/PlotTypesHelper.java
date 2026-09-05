package net.thevpc.scholar.hadrumaths.plot;

import net.thevpc.nuts.collections.NCollections;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.geom.HPoint;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by vpc on 7/17/17.
 */
public class PlotTypesHelper {


    private static int getArrayDim(Class any) {
        if (any.isArray()) {
            return 1 + getArrayDim(any.getComponentType());
        }
        return 0;
    }


    //should compress to  double[] or Complex[] if applicable!!


    public static double toDouble(Object obj) {
        if (obj == null) {
            return Double.NaN;
        }
        if (obj instanceof Expr) {
            Expr e = ((Expr) obj).simplify();
            if (e.isNarrow(ExprType.DOUBLE_EXPR) && e.getDomain().isUnbounded()) {
                return e.toDouble();
            }
        } else if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        throw new IllegalArgumentException("Not a Double : " + obj.getClass());
    }

    public static Complex toComplex(Object obj) {
        if (obj == null) {
            return Complex.NaN;
        }
        if (obj instanceof Complex) {
            return (Complex) obj;
        }
        if (obj instanceof Number) {
            return Complex.of(((Number) obj).doubleValue());
        }
        throw new IllegalArgumentException("Not a Complex");
    }


    public static Complex[] toComplexArray(Object obj) {
        if (obj == null) {
            return null;
        }
        Object[] objects = toObjectArray(obj);
        Complex[] complexes = new Complex[objects.length];
        for (int i = 0; i < complexes.length; i++) {
            complexes[i] = toComplex(objects[i]);
        }
//        if (obj.getClass().isArray()) {
//            if (obj.getClass().getComponentType().equals(Complex.class)) {
//                return (Complex[]) obj;
//            }
//            Complex[] arr = new Complex[Array.getLength(obj)];
//            for (int i = 0; i < arr.length; i++) {
//                arr[i] = toComplex(Array.get(obj, i));
//            }
//            return arr;
//        } else if (obj instanceof ExprList) {
//            return ((ExprList) obj).toComplexArray();
//        } else if (obj instanceof Vector) {
//            return ((Vector) obj).toArray();
//        } else if (obj instanceof Matrix) {
//            Matrix m = (Matrix) obj;
//            if (m.isColumn() && m.getColumnCount() > 0) {
//                return m.getColumn(0).toArray();
//            }
//            if (m.isRow() && m.getRowCount() > 0) {
//                return m.getRow(0).toArray();
//            }
//            if (m.getRowCount() > 0 || m.getColumnCount() > 0) {
//                throw new IllegalArgumentException("Unsupported");
//            }
//            return new Complex[0];
//        } else if (obj instanceof Collection) {
//            return toComplexArray(((Collection) obj).toArray());
//        }
        return complexes;
    }

    public static HPoint[] toPointArray(Object obj) {
        Object[] objects = toObjectArray(obj);
        if (obj == null) {
            return null;
        }
        HPoint[] arr = new HPoint[objects.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (HPoint) objects[i];
        }
        return arr;
    }

    public static Expr[] toExprArray(Object obj) {
        Object[] objects = toObjectArray(obj);
        if (objects == null) {
            return null;
        }
        Expr[] arr = new Expr[objects.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (Expr) objects[i];
        }
        return arr;
    }

    public static Object[] toObjectArray(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass().isArray()) {
            if (obj.getClass().getComponentType().equals(Object.class)) {
                return (Object[]) obj;
            }
            Object[] arr = new Object[Array.getLength(obj)];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = (Array.get(obj, i));
            }
            return arr;
        } else if (obj instanceof ComplexMatrix) {
            ComplexMatrix m = (ComplexMatrix) obj;
            if (m.isColumn() && m.getColumnCount() == 1) {
                return m.getColumn(0).toArray();
            }
            throw new IllegalArgumentException("Not an Object Array");
        } else if (obj instanceof java.util.Vector) {
            return ((java.util.Vector) obj).toArray();
        } else if (obj instanceof Vector) {
            return toObjectArray(((Vector) obj).toArray());
        } else if (obj instanceof Collection) {
            return (((Collection) obj).toArray());
        } else if (obj instanceof Iterable) {
            return NCollections.list((Iterable) obj).toArray();
        } else if (obj instanceof Iterator) {
            return NCollections.list((Iterator) obj).toArray();
        }
        throw new IllegalArgumentException("Not an Object Array");
    }

    public static Complex[][] toComplexArray2(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass().isArray()) {
            Complex[][] arr = new Complex[Array.getLength(obj)][];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = toComplexArray(Array.get(obj, i));
            }
            return arr;
        } else if (obj instanceof Collection) {
            return toComplexArray2(((Collection) obj).toArray());
        } else if (obj instanceof Matrix) {
            return toComplexArray2(((Matrix) obj).getArray());
        } else if (obj instanceof java.util.Vector) {
            return ((ComplexVector) obj).toMatrix().getArray();
        }
        throw new IllegalArgumentException("Not an Complex[][]");
    }

    public static Complex[][][] toComplexArray3(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass().isArray()) {
            Complex[][][] arr = new Complex[Array.getLength(obj)][][];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = toComplexArray2(Array.get(obj, i));
            }
            return arr;
        } else if (obj instanceof Collection) {
            return toComplexArray3(((Collection) obj).toArray());
        } else if (obj instanceof Matrix) {
            return toComplexArray3(((Matrix) obj).getArray());
        }
        throw new IllegalArgumentException("Not an Complex[][][]");
    }

    public static HPoint[][] toPointArray2(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass().isArray()) {
            if (obj.getClass().getComponentType().equals(HPoint[].class)) {
                return (HPoint[][]) obj;
            }
            HPoint[][] arr = new HPoint[Array.getLength(obj)][];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = toPointArray(Array.get(obj, i));
            }
            return arr;
        } else if (obj instanceof Collection) {
            return toPointArray2(((Collection) obj).toArray());
        }
        throw new IllegalArgumentException("Not an Point[][]");
    }

    public static double[] toDoubleArray(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass().isArray()) {
            if (obj.getClass().getComponentType().equals(Double.TYPE)) {
                return (double[]) obj;
            }
            double[] arr = new double[Array.getLength(obj)];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = toDouble(Array.get(obj, i));
            }
            return arr;
        } else if (obj instanceof Vector) {
            return toDoubleArray(((Vector) obj).toArray());
        } else if (obj instanceof Collection) {
            return toDoubleArray(((Collection) obj).toArray());
        }
        throw new IllegalArgumentException("Not an double[][]");
    }

    public static double[][] toDoubleArray2(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass().isArray()) {
            if (obj.getClass().getComponentType().equals(double[].class)) {
                return (double[][]) obj;
            }
            double[][] arr = new double[Array.getLength(obj)][];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = toDoubleArray(Array.get(obj, i));
            }
            return arr;
        } else if (obj instanceof Collection) {
            return toDoubleArray2(((Collection) obj).toArray());
        }
        throw new IllegalArgumentException("Not an double[][]");
    }


}
