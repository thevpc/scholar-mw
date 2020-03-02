package net.vpc.scholar.hadrumaths.symbolic.double2vector;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.ExprDefaults;
import net.vpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;

import java.util.*;


/**
 * User: taha Date: 2 juil. 2003 Time: 16:42:35
 */
public class DefaultDoubleToVector extends AbstractDoubleToVector {
    private static final long serialVersionUID = 1L;

    //    public static final int X = Axis.X.ordinal();
//    public static final int Y = Axis.Y.ordinal();
    private final ComponentDimension componentDimension;
    private final DoubleToComplex[] components;
    private final Domain domain;

//    public VDCxy(IDoubleToComplex fx, IDoubleToComplex fy) {
//        this(null, fx, fy, null);
//    }
//
//    public VDCxy(String name, IDoubleToDouble fx, IDoubleToDouble fy) {
//        this(name, new DefaultDoubleToComplex(fx), new DefaultDoubleToComplex(fy), null);
//    }
//
//    public VDCxy(String name, IDoubleToDouble fx, IDoubleToComplex fy) {
//        this(name, new DefaultDoubleToComplex(fx), fy, null);
//    }
//
//    public VDCxy(String name, IDoubleToComplex fx, IDoubleToDouble fy) {
//        this(name, fx, new DefaultDoubleToComplex(fy), null);
//    }
//
//    public VDCxy(IDoubleToDouble fx, IDoubleToDouble fy) {
//        this(null, fx == null ? null : new DefaultDoubleToComplex(fx), fy == null ? null : new DefaultDoubleToComplex(fy), null);
//    }
//
//    public VDCxy(String name, IDoubleToComplex fx, IDoubleToComplex fy) {
//        this(name, fx, fy, null);
//    }

    private DefaultDoubleToVector(DoubleToComplex[] f, int dim) {
        this(f[0], dim > 1 ? f[1] : null, dim > 2 ? f[2] : null, dim);
    }

    private DefaultDoubleToVector(DoubleToComplex fx, DoubleToComplex fy, DoubleToComplex fz, int dim) {
        switch (dim) {
            case 1: {
                components = new DoubleToComplex[]{fx};
                componentDimension = ComponentDimension.SCALAR;
                domain = fx.getDomain();
                break;
            }
            case 2: {
                components = new DoubleToComplex[]{fx, fy};
                componentDimension = ComponentDimension.VECTOR2;
                domain = fx.getDomain().expand(fy.getDomain());
                break;
            }
            case 3: {
                components = new DoubleToComplex[]{fx, fy, fz};
                componentDimension = ComponentDimension.VECTOR3;
                domain = fx.getDomain().expand(fy.getDomain()).expand(fz.getDomain());
                break;
            }
            default: {
                throw new UnsupportedComponentDimensionException(dim);
            }
        }
    }

    @Deprecated
    public static DoubleToVector add(DoubleToVector one, DoubleToVector another) {
        Expr vector2D = Maths.vector(Maths.sum(one.getComponent(Axis.X), another.getComponent(Axis.X)), Maths.sum(one.getComponent(Axis.Y), another.getComponent(Axis.Y)));
        Map<String, Object> map = new HashMap<String, Object>();
        TreeSet<String> k = new TreeSet<String>();
        if (one.hasProperties()) {
            k.addAll(one.getProperties().keySet());
        }
        if (another.hasProperties()) {
            k.addAll(another.getProperties().keySet());
        }
        for (String s : k) {
            Object a = one.getProperty(s);
            Object b = another.getProperty(s);
            if (a == null) {
                map.put(s, b);
            } else if (b == null) {
                map.put(s, a);
            } else if (a.equals(b)) {
                map.put(s, a);
            } else {
                map.put(s, a + "+" + b);
            }
        }
        if (!map.isEmpty()) {
            vector2D = vector2D.setProperties(map);
        }
        return vector2D.toDV();
    }

    public static DefaultDoubleToVector of(DoubleToDouble fx) {
        return new DefaultDoubleToVector(fx.toDC(), new DefaultComplexValue(Maths.CZERO, fx.getDomain()), new DefaultComplexValue(Maths.CZERO, fx.getDomain()), 1);
    }

    public static DefaultDoubleToVector of(DoubleToComplex fx) {
        return new DefaultDoubleToVector(fx, new DefaultComplexValue(Maths.CZERO, fx.getDomain()), new DefaultComplexValue(Maths.CZERO, fx.getDomain()), 1);
    }

    public static DefaultDoubleToVector of(DoubleToComplex fx, DoubleToComplex fy) {
        return new DefaultDoubleToVector(fx, fy, new DefaultComplexValue(Maths.CZERO, fx.getDomain()), 2);
    }

    public static DefaultDoubleToVector of(DoubleToComplex fx, DoubleToComplex fy, DoubleToComplex fz) {
        return new DefaultDoubleToVector(fx, fy, fz, 3);
    }

    public static DefaultDoubleToVector of(DoubleToComplex... fx) {
        switch (fx.length){
            case 1:{
                return of(fx[0]);
            }
            case 2:{
                return of(fx[0],fx[1]);
            }
            case 3:{
                return of(fx[0],fx[1],fx[2]);
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public Expr getComponent(int row, int col) {
        if(col==0 && row<components.length){
            return components[row];
        }
        return Maths.CZEROX;
    }

//    private void checkComponent(int row, int col) {
//        ComponentDimension d = getComponentDimension();
//        if (row < 0 || row >= d.rows) {
//            throw new IllegalArgumentException("Invalid Row " + row + " not in [0 .. " + d.rows + " ]");
//        }
//        if (col < 0 || col >= d.columns) {
//            throw new IllegalArgumentException("Invalid Column " + col + " not in [0 .. " + d.columns + " ]");
//        }
//    }

    public Complex scalarProduct(boolean hemitian, DoubleToVector other) {
        return Maths.scalarProduct(this, other).toComplex();
    }

    public DoubleToVector add(DoubleToVector other) {
        Expr vector2D = Maths.vector(Maths.sum(getComponent(Axis.X), other.getComponent(Axis.X)), Maths.sum(getComponent(Axis.Y), other.getComponent(Axis.Y)));
        HashMap<String, Object> map = new HashMap<String, Object>();
        TreeSet<String> k = new TreeSet<String>();
        if (this.hasProperties()) {
            k.addAll(this.getProperties().keySet());
        }
        if (other.hasProperties()) {
            k.addAll(other.getProperties().keySet());
        }
        for (String s : k) {
            Object a = this.getProperty(s);
            Object b = other.getProperty(s);
            if (a == null) {
                map.put(s, b);
            } else if (b == null) {
                map.put(s, a);
            } else if (a.equals(b)) {
                map.put(s, a);
            } else {
                map.put(s, a + "+" + b);
            }
        }
        if (!map.isEmpty()) {
            vector2D = vector2D.setProperties(map);
        }
        return vector2D.toDV();
    }

//    @Override
//    public String getComponentTitle(int row, int col) {
//        checkComponent(row, col);
//        ComponentDimension d = getComponentDimension();
//        if (d.columns == 1) {
//            if (d.rows <= 3) {
//                switch (row) {
//                    case 0:
//                        return "X";
//                    case 1:
//                        return "Y";
//                    case 2:
//                        return "Z";
//                }
//            }
//        } else if (d.rows == 1) {
//            if (d.columns <= 3) {
//                switch (col) {
//                    case 0:
//                        return "X";
//                    case 1:
//                        return "Y";
//                    case 2:
//                        return "Z";
//                }
//            }
//        }
//        return "[" + (row + 1) + "," + (col + 1) + ")";
//    }

    @Override
    public DoubleToComplex getComponent(Axis a) {
        if (a.index() >= components.length) {
            return Maths.CZEROXY;
        }
        return components[a.index()];
    }


    //    public VDCxy simplify() {
//        return new VDCxy(name, fx.simplify(), fy.simplify(), properties);
//    }
//    public IDCxy getFx() {
//        return fx;
//    }
//
//
//    public IDCxy getFy() {
//        return fy;
//    }

    @Override
    public int getComponentSize() {
        return components.length;
    }

//    public void setProperties(LinkedHashMap<String, Object> properties) {
//        this.properties = properties;
//    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(components);
        result = 31 * result + (domain != null ? domain.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultDoubleToVector)) return false;

        DefaultDoubleToVector vdCxy = (DefaultDoubleToVector) o;

        if (domain != null ? !domain.equals(vdCxy.domain) : vdCxy.domain != null) return false;
        return Arrays.equals(components, vdCxy.components);
    }

    @Override
    public boolean isInvariant(Axis axis) {
        return false;
    }

    @Override
    public Expr setParam(String name, Expr value) {
        Expr[] updated = new Expr[components.length];
        boolean changed = false;
        for (int i = 0; i < updated.length; i++) {
            Expr s1 = components[i];
            Expr s2 = s1.setParam(name, value);
            if (s1 != s2) {
                changed = true;
            }
            updated[i] = s2;
        }
        if (changed) {
            Expr e = null;
            switch (updated.length) {
                case 1: {
                    e = Maths.vector(updated[0].toDC());
                    break;
                }
                case 2: {
                    e = Maths.vector(updated[0].toDC(), updated[1].toDC());
                    break;
                }
                case 3: {
                    e = Maths.vector(updated[0].toDC(), updated[1].toDC(), updated[2].toDC());
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unsupported");
                }
            }
            e = ExprDefaults.copyProperties(this, e);
            return ExprDefaults.updateTitleVars(e, name, value);
        }
        return this;
    }

    public List<Expr> getChildren() {
        return (List) Arrays.asList(components);
    }

    @Override
    public Expr compose(Expr xreplacement, Expr yreplacement, Expr zreplacement) {
        Expr[] updated = new Expr[components.length];
        boolean changed = false;
        for (int i = 0; i < updated.length; i++) {
            Expr s1 = components[i];
            Expr s2 = s1.compose(xreplacement, yreplacement, zreplacement);
            if (s1 != s2) {
                changed = true;
            }
            updated[i] = s2;
        }
        if (changed) {
            Expr e = Maths.vector(updated[0].toDC(), updated[0].toDC());
            e = ExprDefaults.copyProperties(this, e);
            return e;
        }
        return this;
    }

    public ComponentDimension getComponentDimension() {
        return componentDimension;
    }

    @Override
    public Expr mul(Domain domain) {
        DoubleToComplex[] c2 = new DoubleToComplex[components.length];
        for (int i = 0; i < components.length; i++) {
            c2[i] = components[i].mul(domain).toDC();
        }
        return new DefaultDoubleToVector(c2, components.length);
    }

    @Override
    public Expr mul(double other) {
        DoubleToComplex[] c2 = new DoubleToComplex[components.length];
        for (int i = 0; i < components.length; i++) {
            c2[i] = components[i].mul(other).toDC();
        }
        return new DefaultDoubleToVector(c2, components.length);
    }

    @Override
    public Expr mul(Complex other) {
        DoubleToComplex[] c2 = new DoubleToComplex[components.length];
        for (int i = 0; i < components.length; i++) {
            c2[i] = components[i].mul(other).toDC();
        }
        return new DefaultDoubleToVector(c2, components.length);
    }

    @Override
    public boolean isSmartMulDouble() {
        return true;
    }

    @Override
    public boolean isSmartMulComplex() {
        return true;
    }

    @Override
    public boolean isSmartMulDomain() {
        return true;
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        int dim = getComponentSize();
        switch (dim) {
            case 1:
                return new DefaultDoubleToVector(new DoubleToComplex[]{subExpressions[0].toDC()}, dim);
            case 2:
                return new DefaultDoubleToVector(new DoubleToComplex[]{subExpressions[0].toDC(), subExpressions[1].toDC()}, dim);
            case 3:
                return new DefaultDoubleToVector(new DoubleToComplex[]{subExpressions[0].toDC(), subExpressions[1].toDC(), subExpressions[2].toDC()}, dim);
        }
        throw new IllegalArgumentException("Unexpected");
    }

    public Domain getDomain() {
        return domain;
    }
}
