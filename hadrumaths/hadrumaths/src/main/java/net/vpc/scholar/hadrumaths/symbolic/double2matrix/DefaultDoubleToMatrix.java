package net.vpc.scholar.hadrumaths.symbolic.double2matrix;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.internal.IgnoreRandomGeneration;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.symbolic.double2vector.DefaultDoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Plus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * User: taha Date: 2 juil. 2003 Time: 16:42:35
 */
public class DefaultDoubleToMatrix extends AbstractDoubleToMatrix {
    private static final long serialVersionUID = 1L;

    //    public static final int X = Axis.X.ordinal();
//    public static final int Y = Axis.Y.ordinal();
    private final ComponentDimension componentDimension;
    private final DoubleToComplex[][] components;
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

    private DefaultDoubleToMatrix(int rows, int cols, MatrixCell<Expr> cell) {
        componentDimension = ComponentDimension.of(rows, cols);
        components = new DoubleToComplex[rows][cols];
        Domain d=null;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                components[i][j] = cell.get(i, j).toDC();
                if(d==null){
                    d=components[i][j].getDomain();
                }else{
                    d=d.expand(components[i][j].getDomain());
                }
            }
        }
        if(d==null){
            d=Domain.ZEROX;
        }
        domain=d;
    }

    private DefaultDoubleToMatrix(DoubleToComplex[][] fx) {
        Domain d = null;
        int rows = fx.length;
        int cols = rows == 0 ? 0 : fx[0].length;
        for (DoubleToComplex[] doubleToComplexes : fx) {
            if (cols != doubleToComplexes.length) {
                throw new IllegalArgumentException("Invalid");
            }
            for (int j = 0; j < doubleToComplexes.length; j++) {
                if (d == null) {
                    d = doubleToComplexes[j].getDomain();
                } else {
                    d = d.expand(doubleToComplexes[j].getDomain());
                }
            }
        }
        if (d == null) {
            domain = Domain.ZEROX;
        } else {
            domain = d;
        }
        componentDimension = ComponentDimension.of(rows, cols);
        components = fx;
    }

//    @Deprecated
//    public static DoubleToVector add(DoubleToVector one, DoubleToVector another) {
//        Expr vector2D = Maths.vector(Maths.sum(one.getComponent(Axis.X), another.getComponent(Axis.X)), Maths.sum(one.getComponent(Axis.Y), another.getComponent(Axis.Y)));
//        Map<String, Object> map = new HashMap<String, Object>();
//        TreeSet<String> k = new TreeSet<String>();
//        if (one.hasProperties()) {
//            k.addAll(one.getProperties().keySet());
//        }
//        if (another.hasProperties()) {
//            k.addAll(another.getProperties().keySet());
//        }
//        for (String s : k) {
//            Object a = one.getProperty(s);
//            Object b = another.getProperty(s);
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
//            vector2D = vector2D.setProperties(map);
//        }
//        return vector2D.toDV();
//    }


    public static DefaultDoubleToMatrix of(DoubleToComplex fx) {
        return of(1,1,(row, column) -> fx);
    }

    public static DefaultDoubleToMatrix of(DoubleToDouble fx) {
        return of(1,1,(row, column) -> fx.toDC());
    }

    @IgnoreRandomGeneration
    public static DefaultDoubleToMatrix of(int rows, int cols, MatrixCell<Expr> cell) {
        return new DefaultDoubleToMatrix(rows, cols, cell);
    }

    public static DefaultDoubleToMatrix of(DoubleToComplex[][] fx) {
        return new DefaultDoubleToMatrix(fx);
    }

    public static DefaultDoubleToMatrix of(DoubleToMatrix other) {
        ComponentDimension d = other.getComponentDimension();
        DoubleToComplex[][] components = new DoubleToComplex[d.rows][d.columns];
        for (int i = 0; i < d.rows; i++) {
            for (int j = 0; j < d.columns; j++) {
                components[i][j] = other.getComponent(i, j).toDC();
            }
        }
        return new DefaultDoubleToMatrix(components);
    }

    public static DefaultDoubleToMatrix of(DoubleToVector other) {
        ComponentDimension d = other.getComponentDimension();
        DoubleToComplex[][] components = new DoubleToComplex[d.rows][1];
        for (int i = 0; i < d.rows; i++) {
            components[i][0] = other.getComponent(Axis.cartesian(i)).toDC();
        }
        return new DefaultDoubleToMatrix(components);
    }

    public DefaultDoubleToMatrix mul(DoubleToMatrix other) {
        ComponentDimension thisDim = this.getComponentDimension();
        ComponentDimension otherDim = other.getComponentDimension();
        if (thisDim.is(1, 1)) {
            return of(otherDim.rows,otherDim.columns,(row, column) -> this.getComponent(0,0).mul(other.getComponent(column, row)).toDC());
        } else if (otherDim.is(1, 1)) {
            return of(thisDim.rows,thisDim.columns,(row, column) -> this.getComponent(column, row).mul(other.getComponent(0,0)).toDC());
        } else if (thisDim.columns == otherDim.rows) {
            return of(thisDim.rows,otherDim.columns,(row, column) -> {
                List<DoubleToComplex> p=new ArrayList<>();
                for (int i = 0; i < thisDim.columns; i++) {
                    p.add(this.getComponent(row,i).mul(other.getComponent(i,column)).toDC());
                }
                return Plus.of(p.toArray(new Expr[0]));
            });
        } else {
            throw new IllegalArgumentException("Illegal Mul Matrix " + thisDim + " * " + otherDim);
        }
    }

    public DefaultDoubleToMatrix transpose() {
        ComponentDimension d = getComponentDimension();
        return of(d.columns,d.rows,(row, column) -> this.getComponent(column, row));
    }

    public DefaultDoubleToVector toVector() {
        ComponentDimension d = getComponentDimension();
        if (d.columns == 1) {
            DoubleToComplex[] aa = new DoubleToComplex[d.rows];
            for (int i = 0; i < aa.length; i++) {
                aa[i] = getComponent(i, 0).toDC();
            }
            return DefaultDoubleToVector.of(aa);
        }
        if (d.rows == 1) {
            DoubleToComplex[] aa = new DoubleToComplex[d.columns];
            for (int i = 0; i < aa.length; i++) {
                aa[i] = getComponent(0, i).toDC();
            }
            return DefaultDoubleToVector.of(aa);
        }
        throw new IllegalArgumentException("Unsupported");
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(components);
        result = 31 * result + (domain != null ? domain.hashCode() : 0);
        return result;
    }

//    public Complex scalarProduct(boolean hemitian, DoubleToVector other) {
//        return Maths.scalarProduct(this, other);
//    }
//
//    public DoubleToVector add(DoubleToVector other) {
//        Expr vector2D = Maths.vector(Maths.sum(getComponent(Axis.X), other.getComponent(Axis.X)), Maths.sum(getComponent(Axis.Y), other.getComponent(Axis.Y)));
//        HashMap<String, Object> map = new HashMap<String, Object>();
//        TreeSet<String> k = new TreeSet<String>();
//        if (this.hasProperties()) {
//            k.addAll(this.getProperties().keySet());
//        }
//        if (other.hasProperties()) {
//            k.addAll(other.getProperties().keySet());
//        }
//        for (String s : k) {
//            Object a = this.getProperty(s);
//            Object b = other.getProperty(s);
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
//            vector2D = vector2D.setProperties(map);
//        }
//        return vector2D.toDV();
//    }

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

//    @Override
//    public DoubleToComplex getComponent(Axis a) {
//        if (a.ordinal() >= components.length) {
//            return FunctionFactory.CZEROXY;
//        }
//        return components[a.ordinal()];
//    }


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

//    @Override
//    public int getComponentSize() {
//        return components.length;
//    }

//    public void setProperties(LinkedHashMap<String, Object> properties) {
//        this.properties = properties;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DefaultDoubleToMatrix)) return false;

        DefaultDoubleToMatrix vdCxy = (DefaultDoubleToMatrix) o;

        if (domain != null ? !domain.equals(vdCxy.domain) : vdCxy.domain != null) return false;
        return Arrays.deepEquals(components, vdCxy.components);
    }

//    @Override
//    public boolean isInvariant(Axis axis) {
//        return false;
//    }

    public List<Expr> getChildren() {
        List<Expr> all = new ArrayList<>();
        for (DoubleToComplex[] component : components) {
            all.addAll(Arrays.asList(component));
        }
        return all;
    }

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
//            Expr e = null;
//            switch (updated.length) {
//                case 1: {
//                    e = Maths.vector(updated[0].toDC());
//                    break;
//                }
//                case 2: {
//                    e = Maths.vector(updated[0].toDC(), updated[1].toDC());
//                    break;
//                }
//                case 3: {
//                    e = Maths.vector(updated[0].toDC(), updated[1].toDC(), updated[2].toDC());
//                    break;
//                }
//                default: {
//                    throw new IllegalArgumentException("Unsupported");
//                }
//            }
//            e = ExprDefaults.copyProperties(this, e);
//            return ExprDefaults.updateTitleVars(e, name, value);
//        }
//        return this;
//    }

    public ComponentDimension getComponentDimension() {
        return componentDimension;
    }

//    @Override
//    public boolean isSmartMulDouble() {
//        return true;
//    }

//    @Override
//    public Expr mul(Domain domain) {
//        DoubleToComplex[] c2 = new DoubleToComplex[components.length];
//        for (int i = 0; i < components.length; i++) {
//            c2[i] = components[i].mul(domain).toDC();
//        }
//        return new DefaultDoubleToMatrix(c2, components.length);
//    }
//
//    @Override
//    public Expr mul(double other) {
//        DoubleToComplex[] c2 = new DoubleToComplex[components.length];
//        for (int i = 0; i < components.length; i++) {
//            c2[i] = components[i].mul(other).toDC();
//        }
//        return new DefaultDoubleToMatrix(c2, components.length);
//    }
//
//    @Override
//    public Expr mul(Complex other) {
//        DoubleToComplex[] c2 = new DoubleToComplex[components.length];
//        for (int i = 0; i < components.length; i++) {
//            c2[i] = components[i].mul(other).toDC();
//        }
//        return new DefaultDoubleToMatrix(c2, components.length);
//    }

//    @Override
//    public boolean isSmartMulComplex() {
//        return true;
//    }
//
//    @Override
//    public boolean isSmartMulDomain() {
//        return true;
//    }

    public Domain getDomain() {
        return domain;
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        DoubleToComplex[][] components2 = new DoubleToComplex[componentDimension.rows][componentDimension.columns];
        int count = componentDimension.rows * componentDimension.columns;
        for (int a = 0; a < count; a++) {
            int i = a / componentDimension.columns;
            int j = a % componentDimension.columns;
            components2[i][j] = subExpressions[a].toDC();
        }
        return new DefaultDoubleToMatrix(components2);
    }

    @Override
    public ComplexMatrix evalMatrix(double x, BooleanMarker defined) {
        Complex[][] vals = new Complex[componentDimension.rows][componentDimension.columns];
        BooleanRef r = BooleanMarker.ref();
        for (int i = 0; i < vals.length; i++) {
            for (int j = 0; j < vals[i].length; j++) {
                vals[i][j] = components[i][j].evalComplex(x, r);
            }
        }
        if (r.get()) {
            return Maths.matrix(vals);
        } else {
            return DoubleToMatrixDefaults.Zero(this);
        }
    }

    @Override
    public ComplexMatrix evalMatrix(double x, double y, BooleanMarker defined) {
        Complex[][] vals = new Complex[componentDimension.rows][componentDimension.columns];
        BooleanRef r = BooleanMarker.ref();
        for (int i = 0; i < vals.length; i++) {
            for (int j = 0; j < vals[i].length; j++) {
                vals[i][j] = components[i][j].evalComplex(x, y, r);
            }
        }
        if (r.get()) {
            return Maths.matrix(vals);
        } else {
            return DoubleToMatrixDefaults.Zero(this);
        }
    }

    @Override
    public ComplexMatrix evalMatrix(double x, double y, double z, BooleanMarker defined) {
        Complex[][] vals = new Complex[componentDimension.rows][componentDimension.columns];
        BooleanRef r = BooleanMarker.ref();
        for (int i = 0; i < vals.length; i++) {
            for (int j = 0; j < vals[i].length; j++) {
                vals[i][j] = components[i][j].evalComplex(x, y, z, r);
            }
        }
        if (r.get()) {
            return Maths.matrix(vals);
        } else {
            return DoubleToMatrixDefaults.Zero(this);
        }
    }

    public Expr getComponent(int row, int col) {
        if (row < componentDimension.rows && col < componentDimension.columns) {
            return components[row][col];
        }
        return Maths.DZEROX;
    }

    @Override
    public String toLatex() {
        throw new UnsupportedOperationException("Not Implemented toLatex for "+getClass().getName());
    }

}
