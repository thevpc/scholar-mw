package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.Vector;
import net.vpc.scholar.hadrumaths.geom.Point;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.vpc.scholar.hadrumaths.util.CollectionUtils;

import java.lang.reflect.Array;
import java.util.*;

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

    private static boolean isSameCouple(String a, String b, String c, String d) {
        if ((a.equals(c) && b.equals(d)) || (a.equals(d) && b.equals(c))) {
            return true;
        }
        return false;
    }

    private static String resolveUmbrellaType(String type1, String type2) {
        if (type1.equals(type2)) {
            return type1;
        }
        if (type1.equals("object") || type2.equals("object")) {
            return "object";
        }
        if (type1.equals("null")) {
            return type2;
        }
        if (type2.equals("null")) {
            return type1;
        }
        if (isSameCouple(type1, type2, "expr[][]", "complex[][]")) {
            return "expr[][]";
        }
        if (isSameCouple(type1, type2, "complex[][]", "number[][]")) {
            return "complex[][]";
        }
        if (isSameCouple(type1, type2, "expr[]", "complex[]")) {
            return "expr[]";
        }
        if (isSameCouple(type1, type2, "complex[]", "number[]")) {
            return "complex[]";
        }

        if (isSameCouple(type1, type2, "point[]", "null[]")) {
            return "point[]";
        }
        if (isSameCouple(type1, type2, "expr[]", "null[]")) {
            return "expr[]";
        }
        if (isSameCouple(type1, type2, "complex[]", "null[]")) {
            return "complex[]";
        }
        if (isSameCouple(type1, type2, "number[]", "null[]")) {
            return "number[]";
        }
        if (isSameCouple(type1, type2, "expr[][]", "null[][]")) {
            return "expr[][]";
        }
        if (isSameCouple(type1, type2, "complex[][]", "null[][]")) {
            return "complex[][]";
        }
        if (isSameCouple(type1, type2, "number[][]", "null[][]")) {
            return "number[][]";
        }
        return "object";
    }

    public static boolean isComponentType(Object obj) {
        if (obj.getClass().isArray()) {
            return true;
        } else if (obj instanceof TVector) {
            return true;
        } else if (obj instanceof Collection) {
            return true;
        } else if (obj instanceof Iterable) {
            return true;
        }
        return false;
    }

    public static TypeAndValue resolveComponentType(Object obj) {
        List initial=new ArrayList();
        if (obj.getClass().isArray()) {
            int l = Array.getLength(obj);
            for (int i = 0; i < l; i++) {
                initial.add(Array.get(obj,i));
            }
        }else if(obj instanceof TVector){
            initial.addAll(Arrays.asList(((TVector) obj).toArray()));
        }else if(obj instanceof Collection){
            initial.addAll((Collection) obj);
        }else if(obj instanceof Iterable){
            for (Object o : (Iterable) obj) {
                initial.add(o);
            }
        }else{
            return new TypeAndValue("object",obj);
        }
        String t = "null";
        int l = initial.size();
        List<Object> all = new ArrayList<>();
        if (l == 0) {
            return new TypeAndValue("null[]", all);
        }
        for (Object o : initial) {
            TypeAndValue ii = resolveType(o);
            String itm = ii.type;
            all.add(ii.value);
            if (t == null) {
                t = itm;
            } else {
                t = resolveUmbrellaType(t, itm);
            }
        }
        if (
                t.equals("number") || t.equals("complex") || t.equals("point") || t.equals("expr")
                        || t.equals("number[]") || t.equals("complex[]")
                        || t.equals("null") || t.equals("null[]")
                ) {
            t = t + "[]";
        } else {
            t = "object";
        }
        return compress(new TypeAndValue(t, all));
    }
    //should compress to  double[] or Complex[] if applicable!!
    protected static TypeAndValue compress(TypeAndValue t){
        return t;
    }

    public static TypeAndValue resolveType(Object obj) {
        if (obj == null) {
            return new TypeAndValue("null", obj);
        }
        obj = Plot.Config.convert(obj);
        if (obj instanceof Complex) {
            return new TypeAndValue("complex", obj);
        }
        if (obj instanceof DoubleValue && ((DoubleValue) obj).getDomain().isFull()) {
            return new TypeAndValue("number", ((DoubleValue) obj).getValue());
        }
        if (obj instanceof TMatrix) {
            TMatrix m = (TMatrix) obj;
            int c = m.getColumnCount();
            int r = m.getRowCount();
            if(c ==1){
                if(r==1){
                    return resolveType(m.get(0,0));
                }
                return resolveComponentType(m.getColumn(0));
            }
            for (Object o : m.getRows()) {
                TypeAndValue typeAndValue = resolveType(o);
                if(typeAndValue.type.equals("complex[]")){
                    return new TypeAndValue("complex[][]",obj);
                }
            }
            return new TypeAndValue("number[][]",obj);
        }
        if (obj instanceof Vector) {
            for (Complex complex : ((Vector) obj).toArray()) {
                if(!complex.isDouble()){
                    return new TypeAndValue("complex[]",obj);
                }
            }
            return new TypeAndValue("number[]", obj);
        }
        if (obj instanceof ToDoubleArrayAware) {
            return (new TypeAndValue("number[]",((ToDoubleArrayAware) obj).toDoubleArray()));
        }
        if (obj instanceof ComplexArray) {
            return (resolveType(((ComplexArray) obj).toComplexArray()));
        }
        if (obj instanceof Number) {
            return new TypeAndValue("number", obj);
        }
        if (obj instanceof Expr) {
            return new TypeAndValue("expr", obj);
        }
        if (obj instanceof Point) {
            return new TypeAndValue("point", obj);
        }
        if (isComponentType(obj)) {
            return resolveComponentType(obj);
        }
        return new TypeAndValue("object", obj);
    }

    public static double toDouble(Object obj) {
        if (obj == null) {
            return Double.NaN;
        }
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        throw new IllegalArgumentException("Not a Complex");
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
        Complex[] complexes=new Complex[objects.length];
        for (int i = 0; i < complexes.length; i++) {
            complexes[i]=toComplex(objects[i]);
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
        } else if (obj instanceof Matrix) {
            Matrix m = (Matrix) obj;
            if (m.isColumn() && m.getColumnCount()==1) {
                return m.getColumn(0).toArray();
            }
            throw new IllegalArgumentException("Not an Object Array");
        } else if (obj instanceof Vector) {
            return ((Vector) obj).toArray();
        } else if (obj instanceof TVector) {
            return toObjectArray(((TVector) obj).toArray());
        } else if (obj instanceof Collection) {
            return (((Collection) obj).toArray());
        } else if (obj instanceof Iterable) {
            return CollectionUtils.toList((Iterable)obj).toArray();
        } else if (obj instanceof Iterator) {
            return CollectionUtils.toList((Iterator)obj).toArray();
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
        } else if (obj instanceof Vector) {
            return ((Vector) obj).toMatrix().getArray();
        }
        throw new IllegalArgumentException("Not an Complex[][]");
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

    public static class TypeAndValue {
        public String type;
        public Object value;

        public TypeAndValue(String type, Object value) {
            this.type = type;
            this.value = value;
        }
    }

}
