package net.vpc.scholar.hadrumaths.plot;

import net.vpc.common.util.CollectionUtils;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.geom.Point;

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
            if (e.isDouble()) {
                return e.toDouble();
            }
        } else if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        throw new IllegalArgumentException("Not a Double");
    }

    public static Complex toComplex(Object obj) {
        if (obj == null) {
            return Complex.NaN;
        }
        if (obj instanceof Complex) {
            return (Complex) obj;
        }
        if (obj instanceof Number) {
            return Complex.valueOf(((Number) obj).doubleValue());
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

    public static Point[] toPointArray(Object obj) {
        Object[] objects = toObjectArray(obj);
        if (obj == null) {
            return null;
        }
        Point[] arr = new Point[objects.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (Point) objects[i];
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
        } else if (obj instanceof TVector) {
            return toObjectArray(((TVector) obj).toArray());
        } else if (obj instanceof Collection) {
            return (((Collection) obj).toArray());
        } else if (obj instanceof Iterable) {
            return CollectionUtils.toList((Iterable) obj).toArray();
        } else if (obj instanceof Iterator) {
            return CollectionUtils.toList((Iterator) obj).toArray();
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
        } else if (obj instanceof TMatrix) {
            return toComplexArray2(((TMatrix) obj).getArray());
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
        } else if (obj instanceof TMatrix) {
            return toComplexArray3(((TMatrix) obj).getArray());
        }
        throw new IllegalArgumentException("Not an Complex[][][]");
    }

    public static Point[][] toPointArray2(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass().isArray()) {
            if (obj.getClass().getComponentType().equals(Point[].class)) {
                return (Point[][]) obj;
            }
            Point[][] arr = new Point[Array.getLength(obj)][];
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
        } else if (obj instanceof TVector) {
            return toDoubleArray(((TVector) obj).toArray());
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
