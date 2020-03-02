/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic.polymorph.num;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.random.CanProduceClass;
import net.vpc.scholar.hadrumaths.random.NonStateField;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.symbolic.double2matrix.DefaultDoubleToMatrix;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransform;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransformer;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;
import net.vpc.scholar.hadrumaths.util.InternalUnmodifiableArrayList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author vpc
 */
@CanProduceClass({DoubleToDouble.class, DoubleToComplex.class, DoubleToVector.class, DoubleToMatrix.class})
public abstract class Mul implements OperatorExpr {
    private static final long serialVersionUID = 1L;

    static {
        ExpressionTransformFactory.setExpressionTransformer(Mul.class, ExpressionTransform.class, new ExpressionTransformer() {

            public Expr transform(Expr expression, ExpressionTransform transform) {
                Mul e = (Mul) expression;
//                ExpressionTransform t = transform;
                Expr[] e2 = new Expr[e.expressions.array.length];
                for (int i = 0; i < e2.length; i++) {
                    e2[i] = ExpressionTransformFactory.transform(e.expressions.array[i], transform);
                }
                return Maths.sum(e.expressions.array);
            }
        });
    }

    @NonStateField
    protected Domain _domain;
    @NonStateField
    protected int _eagerHashCode;
    protected InternalUnmodifiableArrayList<Expr> expressions;

    protected Mul(Expr[] expressions,int hash) {
        List<Expr> all = new ArrayList<>(expressions.length);
        for (Expr expr : expressions) {
            if (expr instanceof Mul) {
                for (Expr child : expr.getChildren()) {
                    hash=hash*31+child.hashCode();
                    all.add(child);
                }
            } else {
                hash=hash*31+expr.hashCode();
                all.add(expr);
            }
        }
        _eagerHashCode=hash;
        this.expressions = new InternalUnmodifiableArrayList<>(all.toArray(new Expr[0]));
        if (this.expressions.array.length < 1) {
            throw new IllegalArgumentException();
        }
        Domain d = expressions[0].getDomain();
        for (Expr expression : expressions) {
            d = d.intersect(expression.getDomain());
            if (expression == this) {
                throw new IllegalArgumentException();
            }
        }
        this._domain = d;
    }

    public int size() {
        return expressions.array.length;
    }

    @Override
    public Complex toComplex() {
        MutableComplex c = MutableComplex.One();
        for (Expr e : expressions) {
            c.mul(e.toComplex());
        }
        return c.toComplex();
    }

    @Override
    public double toDouble() {
        double c = 1;
        for (Expr e : expressions) {
            c *= (e.toDouble());
        }
        return c;
    }

    public boolean isZero() {
        return ExprDefaults.isZeroAny(expressions);
    }

//    @Override
//    public ExprType getNarrowType() {
//        return ExprDefaults.narrowest(expressions);
//    }

    //    public boolean isDDx() {
//        for (Expr e : expressions) {
//            if (!e.isDDx()) {
//                return false;
//            }
//        }
//        return true;
//    }

    public List<Expr> getChildren() {
        return expressions;
    }

    @Override
    public Expr mul(Complex other) {
        if (other.isZero()) {
            return Maths.ZERO;
        }
        if (other.isReal()) {
            return mul(other.toDouble());
        }
        Expr[] expr2 = ArrayUtils.copy(expressions.array);
        expr2[0] = expr2[0].mul(other);
        return Maths.mul(expr2);
    }

    @Override
    public Expr mul(Domain domain) {
        Expr[] expr2 = ArrayUtils.copy(expressions.array);
        for (int i = 0; i < expr2.length; i++) {
            expr2[i] = expr2[i].mul(domain);
        }
        return Maths.mul(expr2);
    }

    @Override
    public Expr mul(double other) {
        if (other == 0) {
            return Maths.ZERO;
        }
        Expr[] expr2 = ArrayUtils.copy(expressions.array);
        expr2[0] = expr2[0].mul(other);
        return Maths.mul(expr2);
    }

    @Override
    public final Domain getDomain() {
        return _domain;
    }

    public Expr newInstance(Expr... expressions) {
        return Mul.of(expressions);
    }

    public static Mul of(Expr... expressions) {
        switch (ExprDefaults.widest(expressions)) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                return new MulDoubleToDouble(expressions);
            }
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                return new MulDoubleToComplex(expressions);
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                return new MulDoubleToVector(expressions);
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                return new MulDoubleToMatrix(expressions);
            }
            default: {
                throw new IllegalArgumentException("Unsupported");
            }
        }
    }

    @Override
    public int hashCode() {
        return _eagerHashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mul mul = (Mul) o;
        return Objects.equals(expressions, mul.expressions);
    }

//    @Override
//    public DoubleToComplex toDC() {
//        return new MulDoubleToComplex(expressions.toArray(new Expr[0]));
//    }
//
//    @Override
//    public DoubleToDouble toDD() {
//        return new MulDoubleToDouble(expressions.toArray(new Expr[0]));
//    }
//
//    @Override
//    public DoubleToVector toDV() {
//        return new MulDoubleToVector(expressions.toArray(new Expr[0]));
//    }

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }

    //    @Override
//    public DoubleToMatrix toDM() {
//        return new MulDoubleToMatrix(expressions.toArray(new Expr[0]));
//    }
    @Override
    public String getName() {
        return "*";
    }
}

class MulDoubleToDouble extends Mul implements DoubleToDoubleDefaults.DoubleToDoubleNormal {
    public MulDoubleToDouble(Expr... expressions) {
        super(expressions,-2027914402);
    }

    @Override
    public double evalDouble(double x, double y, double z, BooleanMarker defined) {
        //test if x,y,z is in the intersection of domains
        if (contains(x, y, z)) {
            BooleanRef rdefined = BooleanMarker.ref();
            double c = expressions.array[0].toDD().evalDouble(x, y, z, rdefined);
            if (!rdefined.get()) {
                return 0;
            }
            rdefined.reset();
            int size = size();
            for (int i = 1; i < size; i++) {
                c *= (expressions.array[i].toDD().evalDouble(x, y, z, rdefined));
                if (!rdefined.get()) {
                    return 0;
                }
            }
            defined.set();
            return c;
        }
        return 0;
    }

    @Override
    public double evalDouble(double x, BooleanMarker defined) {
        //test if x,y,z is in the intersection of domains
        if (contains(x)) {
            BooleanRef rdefined = BooleanMarker.ref();
            double c = expressions.array[0].toDD().evalDouble(x, rdefined);
            if (!rdefined.get()) {
                return 0;
            }
            rdefined.reset();
            int size = size();
            for (int i = 1; i < size; i++) {
                c *= (expressions.array[i].toDD().evalDouble(x, rdefined));
                if (!rdefined.get()) {
                    return 0;
                }
            }
            defined.set();
            return c;
        }
        return 0;
    }

    @Override
    public double evalDouble(double x, double y, BooleanMarker defined) {
        //test if x,y,z is in the intersection of domains
        if (contains(x, y)) {
            BooleanRef rdefined = BooleanMarker.ref();
            double c = expressions.array[0].toDD().evalDouble(x, y, rdefined);
            if (!rdefined.get()) {
                return 0;
            }
            rdefined.reset();
            int size = size();
            for (int i = 1; i < size; i++) {
                c *= (expressions.array[i].toDD().evalDouble(x, y, rdefined));
                if (!rdefined.get()) {
                    return 0;
                }
            }
            defined.set();
            return c;
        }
        return 0;
    }

    @Override
    public Expr narrow(ExprType other) {
        return newInstance(this.expressions.stream().map(x -> x.narrow(other)).toArray(Expr[]::new));
    }

    @Override
    public ExprType getNarrowType() {
        return ExprDefaults.narrowest(this.expressions);
    }
}


class MulDoubleToComplex extends Mul implements DoubleToComplex {

    public MulDoubleToComplex(Expr... expressions) {
        super(expressions,664688323);
    }

    @Override
    public Complex evalComplex(double x, BooleanMarker defined) {
        if (contains(x)) {
            MutableComplex c = MutableComplex.One();
            BooleanRef rdefined = BooleanMarker.ref();
            for (Expr expression : expressions) {
                c.mul(expression.toDC().evalComplex(x, rdefined));
                if (!rdefined.get()) {
                    return Complex.ZERO;
                }
                rdefined.reset();
            }
            defined.set();
            return c.toComplex();
        }
        return Complex.ZERO;
    }

    @Override
    public Complex evalComplex(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            MutableComplex c = MutableComplex.One();
            BooleanRef rdefined = BooleanMarker.ref();
            for (Expr expression : expressions) {
                c.mul(expression.toDC().evalComplex(x, y, rdefined));
                if (!rdefined.get()) {
                    return Complex.ZERO;
                }
                rdefined.reset();
            }
            defined.set();
            return c.toComplex();
        }
        return Complex.ZERO;
    }

    @Override
    public Complex evalComplex(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            MutableComplex c = MutableComplex.One();
            BooleanRef rdefined = BooleanMarker.ref();
            for (Expr expression : expressions) {
                c.mul(expression.toDC().evalComplex(x, y, z, rdefined));
                //could not call get on BooleanMarker that is not created by our selves
                if (!rdefined.get()) {
                    return Complex.ZERO;
                }
                rdefined.reset();
            }
            defined.set();
            return c.toComplex();
        }
        return Complex.ZERO;
    }

    @Override
    public Expr narrow(ExprType other) {
        return newInstance(this.expressions.stream().map(x -> x.narrow(other)).toArray(Expr[]::new));
    }

    @Override
    public ExprType getNarrowType() {
        return ExprDefaults.narrowest(this.expressions);
    }
}

class MulDoubleToVector extends Mul implements DoubleToVector {
    int cs = 1;

    public MulDoubleToVector(Expr... expressions) {
        super(expressions,-1522343728);
        for (Expr expression : expressions) {
            int s = expression.toDV().getComponentSize();
            if (s == 1) {
                //
            } else if (cs == 1) {
                cs = s;
            } else if (cs != s) {
                throw new IllegalArgumentException("Cannot multiply vectors " + cs + " * " + s);
            } else {
                cs = 1;
            }
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 31 + cs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MulDoubleToVector that = (MulDoubleToVector) o;
        return cs == that.cs;
    }

    @Override
    public Expr narrow(ExprType other) {
        switch (other) {
            case DOUBLE_CVECTOR:
                return this;
            case DOUBLE_CMATRIX: {
                List<DoubleToMatrix> all = new ArrayList<>();
                all.add(expressions.get(0).toDM());
                ComponentDimension dim = expressions.get(0).getComponentDimension();
                for (int i = 1; i < expressions.size(); i++) {
                    Expr v = expressions.get(i);
                    ComponentDimension d2 = v.getComponentDimension();
                    if (dim.is(1, 1)) {
                        dim = d2;
                        all.add(v.toDM());
                    } else if (d2.is(1, 1)) {
                        all.add(v.toDM());
                        //
                    } else {
                        if (dim.columns != d2.rows) {
                            if (dim.columns == d2.columns) {
                                v = DefaultDoubleToMatrix.of(v.toDV()).transpose();
                            } else {
                                throw new IllegalArgumentException("Invalid matrix multiplication " + dim + " * " + d2);
                            }
                        } else {

                        }
                        DoubleToMatrix e = v.toDM();
                        all.add(e);
                        dim = ComponentDimension.of(dim.rows, e.getComponentDimension().columns);
                    }
                }
                return newInstance(all.toArray(new Expr[0]));
            }
        }
        return newInstance(this.expressions.stream().map(x -> x.narrow(other)).toArray(Expr[]::new));
    }

    @Override
    public ExprType getNarrowType() {
        return DoubleToVectorDefaults.getNarrowType(this);
    }

    @Override
    public DoubleToVector toDV() {
        return this;
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.of(cs, 1);
    }

    @Override
    public Expr getComponent(Axis a) {
        return Mul.of(expressions.stream().map(x -> x.toDV().getComponent(a)).toArray(Expr[]::new));
    }

    @Override
    public ComplexVector evalVector(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            BooleanRef rdefined = BooleanMarker.ref();
            ComplexVector c = expressions.get(0).toDV().evalVector(x, y, z, rdefined);
            if (!rdefined.get()) {
                return DoubleToVectorDefaults.Zero(getComponentSize());
            }
            for (int i = 1; i < expressions.size(); i++) {
                DoubleToVector expression = expressions.get(i).toDV();
                rdefined.reset();
                c = c.mul(expression.evalVector(x, y, z, rdefined));
                //could not call get on BooleanMarker that is not created by our selves
                if (!rdefined.get()) {
                    return DoubleToVectorDefaults.Zero(getComponentSize());
                }
            }
            defined.set();
            return c;
        }
        return DoubleToVectorDefaults.Zero(getComponentSize());
    }

    @Override
    public ComplexVector evalVector(double x, BooleanMarker defined) {
        if (contains(x)) {
            BooleanRef rdefined = BooleanMarker.ref();
            ComplexVector c = expressions.get(0).toDV().evalVector(x, rdefined);
            if (!rdefined.get()) {
                return DoubleToVectorDefaults.Zero(getComponentSize());
            }
            for (int i = 1; i < expressions.size(); i++) {
                DoubleToVector expression = expressions.get(i).toDV();
                rdefined.reset();
                c = c.mul(expression.evalVector(x, rdefined));
                //could not call get on BooleanMarker that is not created by our selves
                if (!rdefined.get()) {
                    return DoubleToVectorDefaults.Zero(getComponentSize());
                }
            }
            defined.set();
            return c;
        }
        return DoubleToVectorDefaults.Zero(getComponentSize());
    }

    @Override
    public ComplexVector evalVector(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            BooleanRef rdefined = BooleanMarker.ref();
            ComplexVector c = expressions.get(0).toDV().evalVector(x, y, rdefined);
            if (!rdefined.get()) {
                return DoubleToVectorDefaults.Zero(getComponentSize());
            }
            for (int i = 1; i < expressions.size(); i++) {
                DoubleToVector expression = expressions.get(i).toDV();
                rdefined.reset();
                c = c.mul(expression.evalVector(x, y, rdefined));
                //could not call get on BooleanMarker that is not created by our selves
                if (!rdefined.get()) {
                    return DoubleToVectorDefaults.Zero(getComponentSize());
                }
            }
            defined.set();
            return c;
        }
        return DoubleToVectorDefaults.Zero(getComponentSize());
    }

    @Override
    public int getComponentSize() {
        return cs;
    }


}


class MulDoubleToMatrix extends Mul implements DoubleToMatrix {
    private ComponentDimension dim;

    public MulDoubleToMatrix(Expr... expressions) {
        super(expressions,-1783195826);
        dim = expressions[0].getComponentDimension();
        for (int i = 1; i < expressions.length; i++) {
            ComponentDimension d2 = expressions[i].getComponentDimension();
            if (dim.is(1, 1)) {
                dim = d2;
            } else if (d2.is(1, 1)) {
                //
            } else {
                if (dim.columns != d2.rows) {
                    throw new IllegalArgumentException("Invalid matrix multiplication " + dim + " * " + d2);
                }
                dim = ComponentDimension.of(dim.rows, d2.columns);
            }
        }
    }

    @Override
    public Expr narrow(ExprType other) {
        switch (other) {
            case DOUBLE_CMATRIX:
                return this;
        }
        return newInstance(this.expressions.stream().map(x -> x.narrow(other)).toArray(Expr[]::new));
    }

    @Override
    public ExprType getNarrowType() {
        return DoubleToMatrixDefaults.getNarrowType(this);
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return dim;
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 31 + dim.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MulDoubleToMatrix that = (MulDoubleToMatrix) o;
        return Objects.equals(dim, that.dim);
    }

    @Override
    public ComplexMatrix evalMatrix(double x, BooleanMarker defined) {
        if (contains(x)) {
            BooleanRef rdefined = BooleanMarker.ref();
            ComplexMatrix c = expressions.get(0).toDM().evalMatrix(x, rdefined);
            if (!rdefined.get()) {
                return DoubleToMatrixDefaults.Zero(dim);
            }
            for (int i = 1; i < expressions.size(); i++) {
                DoubleToMatrix expression = expressions.get(i).toDM();
                rdefined.reset();
                c = c.mul(expression.evalMatrix(x, rdefined));
                //could not call get on BooleanMarker that is not created by our selves
                if (!rdefined.get()) {
                    return DoubleToMatrixDefaults.Zero(dim);
                }
            }
            defined.set();
            return c;
        }
        return DoubleToMatrixDefaults.Zero(dim);
    }

    @Override
    public ComplexMatrix evalMatrix(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            BooleanRef rdefined = BooleanMarker.ref();
            ComplexMatrix c = expressions.get(0).toDM().evalMatrix(x, y, rdefined);
            if (!rdefined.get()) {
                return DoubleToMatrixDefaults.Zero(dim);
            }
            for (int i = 1; i < expressions.size(); i++) {
                DoubleToMatrix expression = expressions.get(i).toDM();
                rdefined.reset();
                c = c.mul(expression.evalMatrix(x, y, rdefined));
                //could not call get on BooleanMarker that is not created by our selves
                if (!rdefined.get()) {
                    return DoubleToMatrixDefaults.Zero(dim);
                }
            }
            defined.set();
            return c;
        }
        return DoubleToMatrixDefaults.Zero(dim);
    }

    @Override
    public ComplexMatrix evalMatrix(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            BooleanRef rdefined = BooleanMarker.ref();
            ComplexMatrix c = expressions.get(0).toDM().evalMatrix(x, y, z, rdefined);
            if (!rdefined.get()) {
                return DoubleToMatrixDefaults.Zero(dim);
            }
            for (int i = 1; i < expressions.size(); i++) {
                DoubleToMatrix expression = expressions.get(i).toDM();
                rdefined.reset();
                c = c.mul(expression.evalMatrix(x, y, z, rdefined));
                //could not call get on BooleanMarker that is not created by our selves
                if (!rdefined.get()) {
                    return DoubleToMatrixDefaults.Zero(dim);
                }
            }
            defined.set();
            return c;
        }
        return DoubleToMatrixDefaults.Zero(dim);
    }

    @Override
    public Expr getComponent(int row, int col) {
        return toDefaultDoubleToMatrix().getComponent(row, col);
    }

    private DefaultDoubleToMatrix toDefaultDoubleToMatrix() {
        DefaultDoubleToMatrix d = null;
        for (Expr ee : expressions) {
            if (d == null) {
                d = DefaultDoubleToMatrix.of(ee.toDM());
            } else {
                d = d.mul(ee.toDM());
            }
        }
        return d;
    }
}

