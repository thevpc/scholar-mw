//package net.vpc.scholar.hadrumaths.symbolic;
//
//import net.vpc.scholar.hadrumaths.*;
//
//import java.util.*;
//
//import static net.vpc.scholar.hadrumaths.Maths.CZERO;
//
///**
// * User: taha Date: 2 juil. 2003 Time: 16:42:35
// */
//public class DefaultDoubleToMatrix extends AbstractDoubleToVector implements Cloneable {
//    private static final long serialVersionUID = 1L;
//
//    //    public static final int X = Axis.X.ordinal();
////    public static final int Y = Axis.Y.ordinal();
//    private ComponentDimension componentDimension;
//    private DoubleToComplex[] components;
//    private Domain domain;
//
////    public VDCxy(IDoubleToComplex fx, IDoubleToComplex fy) {
////        this(null, fx, fy, null);
////    }
////
////    public VDCxy(String name, IDoubleToDouble fx, IDoubleToDouble fy) {
////        this(name, new DCxy(fx), new DCxy(fy), null);
////    }
////
////    public VDCxy(String name, IDoubleToDouble fx, IDoubleToComplex fy) {
////        this(name, new DCxy(fx), fy, null);
////    }
////
////    public VDCxy(String name, IDoubleToComplex fx, IDoubleToDouble fy) {
////        this(name, fx, new DCxy(fy), null);
////    }
////
////    public VDCxy(IDoubleToDouble fx, IDoubleToDouble fy) {
////        this(null, fx == null ? null : new DCxy(fx), fy == null ? null : new DCxy(fy), null);
////    }
////
////    public VDCxy(String name, IDoubleToComplex fx, IDoubleToComplex fy) {
////        this(name, fx, fy, null);
////    }
//
//    private DefaultDoubleToMatrix(DoubleToComplex fx, DoubleToComplex fy, DoubleToComplex fz, int dim) {
//        switch (dim) {
//            case 1: {
//                components = new DoubleToComplex[]{fx};
//                componentDimension = ComponentDimension.SCALAR;
//                domain = fx.getDomain();
//                break;
//            }
//            case 2: {
//                components = new DoubleToComplex[]{fx, fy};
//                componentDimension = ComponentDimension.VECTOR2;
//                domain = fx.getDomain().expand(fy.getDomain());
//                break;
//            }
//            case 3: {
//                components = new DoubleToComplex[]{fx, fy, fz};
//                componentDimension = ComponentDimension.VECTOR3;
//                domain = fx.getDomain().expand(fy.getDomain()).expand(fz.getDomain());
//                break;
//            }
//            default: {
//                throw new UnsupportedComponentDimensionException(dim);
//            }
//        }
//    }
//
//    @Override
//    public int getComponentSize() {
//        return components.length;
//    }
//
//    @Deprecated
//    public static DoubleToVector add(DoubleToVector one, DoubleToVector another) {
//        DoubleToVector vector2D = Maths.vector(Maths.sum(one.getComponent(Axis.X), another.getComponent(Axis.X)), Maths.sum(one.getComponent(Axis.Y), another.getComponent(Axis.Y)));
//        HashMap<String, Object> map = new HashMap<String, Object>();
//        TreeSet<String> k = new TreeSet<String>();
//        if (one.hasProperties()) {
//            k.addAll(one.getProperties().keySet());
//        }
//        if (another.hasProperties()) {
//            k.addAll(another.getProperties().keySet());
//        }
//        for (String s : k) {
//            Object a = (!one.hasProperties()) ? null : one.getProperties().get(s);
//            Object b = (!another.hasProperties()) ? null : another.getProperties().get(s);
//            if (a == null) {
//                map.put(s, b);
//            } else if (b == null) {
//                map.put(s, a);
//            } else if (a.equals(b)) {
//                map.put(s, a);
//            } else {
//                map.put(s, a + "+" + b);
//            }
//        }
//        if (!map.isEmpty()) {
//            vector2D.setProperties(map);
//        }
//        return vector2D;
//    }
//
//    public static DefaultDoubleToMatrix create(DoubleToComplex fx) {
//        return new DefaultDoubleToMatrix(fx, new ComplexValue(CZERO, fx.getDomain()), new ComplexValue(CZERO, fx.getDomain()), 1);
//    }
//
//    public static DefaultDoubleToMatrix create(DoubleToComplex fx, DoubleToComplex fy) {
//        return new DefaultDoubleToMatrix(fx, fy, new ComplexValue(CZERO, fx.getDomain()), 2);
//    }
//
//    public static DefaultDoubleToMatrix create(DoubleToComplex fx, DoubleToComplex fy, DoubleToComplex fz) {
//        return new DefaultDoubleToMatrix(fx, fy, fz, 3);
//    }
//
//    @Override
//    public DoubleToComplex getComponent(Axis a) {
//        return components[a.ordinal()];
//    }
//
//    public Expr getComponent(int row, int col) {
//        if (isScalarExpr() && (row != col || col != 0)) {
//            return FunctionFactory.DZEROXY;
//        }
//        return components[row];
//    }
//
//    @Override
//    public String getComponentTitle(int row, int col) {
//        if (col == 0) {
//            if (row == 0) {
//                return "X";
//            }
//            if (row == 1) {
//                return "Y";
//            }
//        }
//        return "[" + (row + 1) + "," + (col + 1) + ")";
//    }
//
//    public Domain getDomainImpl() {
//        return domain;
//    }
//
//
//    //    public VDCxy simplify() {
////        return new VDCxy(name, fx.simplify(), fy.simplify(), properties);
////    }
////    public IDCxy getFx() {
////        return fx;
////    }
////
////
////    public IDCxy getFy() {
////        return fy;
////    }
//
//    public ComponentDimension getComponentDimension() {
//        return componentDimension;
//    }
//
////    public void setProperties(LinkedHashMap<String, Object> properties) {
////        this.properties = properties;
////    }
//
//    public Complex scalarProduct(boolean hemitian, DoubleToVector other) {
//        return Maths.scalarProduct(this, other);
//    }
//
//    public DoubleToVector add(DoubleToVector other) {
//        DoubleToVector vector2D = Maths.vector(Maths.sum(getComponent(Axis.X), other.getComponent(Axis.X)), Maths.sum(getComponent(Axis.Y), other.getComponent(Axis.Y)));
//        HashMap<String, Object> map = new HashMap<String, Object>();
//        TreeSet<String> k = new TreeSet<String>();
//        if (this.hasProperties()) {
//            k.addAll(this.getProperties().keySet());
//        }
//        if (other.hasProperties()) {
//            k.addAll(other.getProperties().keySet());
//        }
//        for (String s : k) {
//            Object a = (!this.hasProperties()) ? null : this.getProperties().get(s);
//            Object b = (!other.hasProperties()) ? null : other.getProperties().get(s);
//            if (a == null) {
//                map.put(s, b);
//            } else if (b == null) {
//                map.put(s, a);
//            } else if (a.equals(b)) {
//                map.put(s, a);
//            } else {
//                map.put(s, a + "+" + b);
//            }
//        }
//        if (map != null && map.size() > 0) {
//            vector2D.setProperties(map);
//        }
//        return vector2D;
//    }
//
////    @Override
////    public String toString() {
////        return "[" + fx.toString() + " ; " + fy.toString() + "]";
////    }
//
//    @Override
//    public DoubleToMatrix clone() {
//        Map<String, Object> properties = hasProperties() ? getProperties() : null;
//        switch (getComponentSize()) {
//            case 1: {
//                return (DoubleToMatrix) new DefaultDoubleToMatrix((DoubleToComplex) getComponent(Axis.X).clone(), null, null, 1).setProperties(properties);
//            }
//            case 2: {
//                return (DoubleToMatrix) new DefaultDoubleToMatrix((DoubleToComplex) getComponent(Axis.X).clone(), (DoubleToComplex) getComponent(Axis.Y).clone(), null, 2).setProperties(properties);
//            }
//            case 3: {
//                return (DoubleToMatrix) new DefaultDoubleToMatrix((DoubleToComplex) getComponent(Axis.X).clone(), (DoubleToComplex) getComponent(Axis.Y).clone(), (DoubleToComplex) getComponent(Axis.Z).clone(), 3).setProperties(properties);
//            }
//        }
//        throw new UnsupportedComponentDimensionException(componentDimension.rows);
//    }
//
//
//    public List<Expr> getSubExpressions() {
//        return (List) Arrays.asList(components);
//    }
//
//    @Override
//    public Expr setParam(String name, Expr value) {
//        Expr[] updated = new Expr[components.length];
//        boolean changed = false;
//        for (int i = 0; i < updated.length; i++) {
//            Expr s1 = components[i];
//            Expr s2 = s1.setParam(name, value);
//            if (s1 != s2) {
//                changed = true;
//            }
//            updated[i] = s2;
//        }
//        if (changed) {
//            Expr e = Maths.vector(updated[0].toDC(), updated[0].toDC());
//            e = Any.copyProperties(this, e);
//            return Any.updateTitleVars(e, name, value);
//        }
//        return this;
//    }
//
//    @Override
//    public Expr composeX(Expr xreplacement) {
//        Expr[] updated = new Expr[components.length];
//        boolean changed = false;
//        for (int i = 0; i < updated.length; i++) {
//            Expr s1 = components[i];
//            Expr s2 = s1.composeX(xreplacement);
//            if (s1 != s2) {
//                changed = true;
//            }
//            updated[i] = s2;
//        }
//        if (changed) {
//            Expr e = Maths.vector(updated[0].toDC(), updated[0].toDC());
//            e = Any.copyProperties(this, e);
//            return e;
//        }
//        return this;
//    }
//
//    @Override
//    public Expr composeY(Expr yreplacement) {
//        Expr[] updated = new Expr[components.length];
//        boolean changed = false;
//        for (int i = 0; i < updated.length; i++) {
//            Expr s1 = components[i];
//            Expr s2 = s1.composeY(yreplacement);
//            if (s1 != s2) {
//                changed = true;
//            }
//            updated[i] = s2;
//        }
//        if (changed) {
//            Expr e = Maths.vector(updated[0].toDC(), updated[0].toDC());
//            e = Any.copyProperties(this, e);
//            return e;
//        }
//        return this;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof DefaultDoubleToMatrix)) return false;
//
//        DefaultDoubleToMatrix vdCxy = (DefaultDoubleToMatrix) o;
//
//        if (domain != null ? !domain.equals(vdCxy.domain) : vdCxy.domain != null) return false;
//        if (componentDimension != null ? !componentDimension.equals(vdCxy.componentDimension) : vdCxy.componentDimension != null)
//            return false;
//        if (!Arrays.equals(components, vdCxy.components)) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = Arrays.hashCode(components);
//        result = 31 * result + (domain != null ? domain.hashCode() : 0);
//        result = 31 * result + (componentDimension != null ? componentDimension.hashCode() : 0);
//        return result;
//    }
//
//
//    @Override
//    public boolean isInvariantImpl(Axis axis) {
//        return false;
//    }
//
//}
