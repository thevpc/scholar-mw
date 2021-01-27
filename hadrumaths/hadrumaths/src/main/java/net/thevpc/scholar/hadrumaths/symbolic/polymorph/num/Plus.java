/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.symbolic.polymorph.num;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.util.internal.CanProduceClass;
import net.thevpc.scholar.hadrumaths.util.internal.NonStateField;
import net.thevpc.scholar.hadrumaths.symbolic.*;
import net.thevpc.scholar.hadrumaths.transform.ExpressionTransform;
import net.thevpc.scholar.hadrumaths.transform.ExpressionTransformer;
import net.thevpc.scholar.hadrumaths.util.ArrayUtils;
import net.thevpc.scholar.hadrumaths.util.InternalUnmodifiableArrayList;

import java.util.List;
import java.util.Objects;

/**
 * @author vpc
 */
@CanProduceClass({DoubleToDouble.class, DoubleToComplex.class, DoubleToVector.class, DoubleToMatrix.class})
public abstract class Plus implements OperatorExpr {
    private static final long serialVersionUID = 1L;

    static {
        ExpressionTransformFactory.setExpressionTransformer(Plus.class, ExpressionTransform.class, new ExpressionTransformer() {

            public Expr transform(Expr expression, ExpressionTransform transform) {
                Plus e = (Plus) expression;
                Expr[] e2 = new Expr[e.expressions.size()];
                for (int i = 0; i < e2.length; i++) {
                    e2[i] = ExpressionTransformFactory.transform(e.expressions.array[i], transform);
                }
                return Plus.of(e2);
            }
        });
    }

    protected InternalUnmodifiableArrayList<Expr> expressions;
    @NonStateField
    protected Domain _domain;
    protected ComponentDimension componentDimension;

    protected Plus(Expr... expressions) {
        this.expressions = new InternalUnmodifiableArrayList<>(expressions);
//        boolean someUnconstrained=false;
//        boolean someConstrained=false;
        Domain d = expressions[0].getDomain();
        componentDimension = expressions[0].getComponentDimension();
        for (int i = 1; i < expressions.length; i++) {
            d = d.expand(expressions[i].getDomain());
            componentDimension = componentDimension.expand(expressions[i].getComponentDimension());
        }
        _domain = d;
    }

    @Override
    public String toLatex() {
        StringBuilder sb=new StringBuilder();
        for (Expr expression : expressions) {
            String s = expression.toLatex();
            if(s.startsWith("-")){
                //
            }else {
                if (sb.length() > 0) {
                    sb.append("+");
                }
            }
            sb.append("{").append(s).append("}");
        }
        return sb.toString();
    }
    @Override
    public String getName() {
        return "+";
    }

    public int size() {
        return expressions.size();
    }

    @Override
    public Complex toComplex() {
        MutableComplex c = new MutableComplex();
        for (Expr e : expressions) {
            c.add(e.toComplex());
        }
        return c.toComplex();
    }

    @Override
    public double toDouble() {
        double c = 0;
        for (Expr e : expressions) {
            c += (e.toDouble());
        }
        return c;
    }

    @Override
    public Expr narrow(ExprType other) {
        return of(ExprDefaults.narrowAllArr(other, expressions));
    }

    public static Plus of(Expr... expressions) {
        ExprType tt = ExprDefaults.widest(expressions);
        switch (tt) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                return new PlusDoubleToDouble(expressions);
            }
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                return new PlusDoubleToComplex(expressions);
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                return new PlusDoubleToVector(expressions);
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                return new PlusDoubleToMatrix(expressions);
            }
            default: {
                throw new IllegalArgumentException("Unsupported " + tt);
            }
        }
    }

    @Override
    public ExprType getNarrowType() {
        return ExprDefaults.narrowest(expressions);
    }

    public boolean isZero() {
        return ExprDefaults.isZeroAll(expressions);
    }

    public boolean isNaN() {
        return ExprDefaults.isNaNAny(expressions);
    }

    public List<Expr> getChildren() {
        return expressions;
    }

    public boolean isInfinite() {
        return ExprDefaults.isInfiniteAny(expressions);
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return componentDimension;
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
        for (int i = 0; i < expr2.length; i++) {
            expr2[i] = expr2[i].mul(other);
        }
        return Plus.of(expr2);
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
    public Expr mul(Domain domain) {
        Expr[] expr2 = ArrayUtils.copy(expressions.array);
        for (int i = 0; i < expr2.length; i++) {
            expr2[i] = expr2[i].mul(domain);
        }
        return Plus.of(expr2);
    }

    @Override
    public Expr mul(double other) {
        if (other == 0) {
            return Maths.ZERO;
        }
        Expr[] expr2 = ArrayUtils.copy(expressions.array);
        for (int i = 0; i < expr2.length; i++) {
            expr2[i] = expr2[i].mul(other);
        }
        return Plus.of(expr2);
    }

    @Override
    public Domain getDomain() {
        return _domain;
    }

    public Expr newInstance(Expr... a) {
        return Plus.of(a);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass().getName(), expressions, componentDimension);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plus plus = (Plus) o;
        return Objects.equals(expressions, plus.expressions) &&
                Objects.equals(componentDimension, plus.componentDimension);
    }


    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }
}

class PlusDoubleToDouble extends Plus implements DoubleToDoubleDefaults.DoubleToDoubleNormal {
    public PlusDoubleToDouble(Expr... expressions) {
        super(expressions);
    }

    public double evalDouble(double x, double y, double z, BooleanMarker defined) {
        double d = 0;
        for (Expr expression : expressions) {
            d += expression.toDD().evalDouble(x, y, z, defined);
        }
        return d;
    }

    public double evalDouble(double x, BooleanMarker defined) {
        double d = 0;
        for (Expr expression : expressions) {
            d += expression.toDD().evalDouble(x, defined);
        }
        return d;
    }

    public double evalDouble(double x, double y, BooleanMarker defined) {
        double d = 0;
        for (Expr expression : expressions) {
            d += expression.toDD().evalDouble(x, y, defined);
        }
        return d;
    }
}

class PlusDoubleToComplex extends Plus implements DoubleToComplex {
    public PlusDoubleToComplex(Expr... expressions) {
        super(expressions);
    }

    public DoubleToDouble getRealDD() {
        if (ExprDefaults.is(ExprType.DOUBLE_DOUBLE, expressions)) {
            return new PlusDoubleToDouble(this);
        }
        Expr[] expressions2 = new Expr[expressions.array.length];
        for (int i = 0; i < expressions.array.length; i++) {
            expressions2[i] = expressions.array[i].toDC().getRealDD();
        }
        return Maths.sum(expressions2).toDD();
    }

    public DoubleToDouble getImagDD() {
        if (ExprDefaults.is(ExprType.DOUBLE_DOUBLE, expressions)) {
            return Maths.DZERO(getDomain().getDimension());
        }
        Expr[] expressions2 = new Expr[expressions.array.length];
        for (int i = 0; i < expressions.array.length; i++) {
            expressions2[i] = expressions.array[i].toDC().getImagDD();
        }
        return new PlusDoubleToDouble(expressions2);
    }

    @Override
    public Complex evalComplex(double x, BooleanMarker defined) {
        MutableComplex c = new MutableComplex();
        for (Expr expression : expressions) {
            c.add(expression.toDC().evalComplex(x, defined));
        }
        return c.toComplex();
    }

    @Override
    public Complex evalComplex(double x, double y, BooleanMarker defined) {
        MutableComplex c = new MutableComplex();
        for (Expr expression : expressions) {
            c.add(expression.toDC().evalComplex(x, y, defined));
        }
        return c.toComplex();
    }

    @Override
    public Complex evalComplex(double x, double y, double z, BooleanMarker defined) {
        MutableComplex c = new MutableComplex();
        for (Expr expression : expressions) {
            c.add(expression.toDC().evalComplex(x, y, z, defined));
        }
        return c.toComplex();
    }
}

class PlusDoubleToVector extends Plus implements DoubleToVector {
    private int cs;

    public PlusDoubleToVector(Expr... expressions) {
        super(expressions);
        cs = Expressions.widestComponentSize(expressions);
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.of(cs, 1);
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
        PlusDoubleToVector that = (PlusDoubleToVector) o;
        return cs == that.cs;
    }

    @Override
    public Expr getComponent(Axis a) {
        return Plus.of(expressions.stream().map(x -> x.toDV().getComponent(a)).toArray(Expr[]::new));
    }

    @Override
    public ComplexVector evalVector(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            ComplexVector c = expressions.get(0).toDV().evalVector(x, y, z, defined);
            for (int i = 1; i < expressions.size(); i++) {
                DoubleToVector expression = expressions.get(i).toDV();
                c = c.add(expression.evalVector(x, y, z, defined));
            }
            return c;
        }
        return DoubleToVectorDefaults.Zero(getComponentSize());
    }

    @Override
    public ComplexVector evalVector(double x, BooleanMarker defined) {
        if (contains(x)) {
            ComplexVector c = expressions.get(0).toDV().evalVector(x, defined);
            for (int i = 1; i < expressions.size(); i++) {
                DoubleToVector expression = expressions.get(i).toDV();
                c = c.add(expression.evalVector(x, defined));
            }
            return c;
        }
        return DoubleToVectorDefaults.Zero(getComponentSize());
    }

    @Override
    public ComplexVector evalVector(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            ComplexVector c = expressions.get(0).toDV().evalVector(x, y, defined);
            for (int i = 1; i < expressions.size(); i++) {
                DoubleToVector expression = expressions.get(i).toDV();
                c = c.add(expression.evalVector(x, y, defined));
            }
            return c;
        }
        return DoubleToVectorDefaults.Zero(getComponentSize());
    }

    @Override
    public int getComponentSize() {
        return cs;
    }


}

class PlusDoubleToMatrix extends Plus implements DoubleToMatrix {

    public PlusDoubleToMatrix(Expr... expressions) {
        super(expressions);
    }

    public ComplexMatrix evalMatrix(double x) {
        return Expressions.evalMatrix(this, x);
    }

    @Override
    public ComplexMatrix evalMatrix(double x, BooleanMarker defined) {
        ComplexMatrix c = expressions.array[0].toDM().evalMatrix(x, defined);
        for (int i = 1; i < expressions.array.length; i++) {
            c = c.add(expressions.array[i].toDM().evalMatrix(x, defined));
        }
        return c;
    }

    @Override
    public ComplexMatrix evalMatrix(double x, double y, BooleanMarker defined) {
        ComplexMatrix c = expressions.array[0].toDM().evalMatrix(x, y, defined);
        for (int i = 1; i < expressions.array.length; i++) {
            c = c.add(expressions.array[i].toDM().evalMatrix(x, y, defined));
        }
        return c;
    }

    @Override
    public ComplexMatrix evalMatrix(double x, double y, double z, BooleanMarker defined) {
        ComplexMatrix c = expressions.array[0].toDM().evalMatrix(x, y, z, defined);
        for (int i = 1; i < expressions.array.length; i++) {
            c = c.add(expressions.array[i].toDM().evalMatrix(x, y, z, defined));
        }
        return c;
    }

    public Expr getComponent(int row, int col) {
        if (isNarrow(ExprType.DOUBLE_CMATRIX)) {
            if (isNarrow(ExprDim.SCALAR) && (row != col || col != 0)) {
                return Maths.DZEROXY;
            }
            DoubleToMatrix[] m = new DoubleToMatrix[expressions.array.length];
            ComponentDimension dd = null;

            for (int i = 0; i < expressions.array.length; i++) {
                m[i] = expressions.array[i].toDM();
                ComponentDimension d = m[i].getComponentDimension();
                if (d.equals(ComponentDimension.SCALAR)) {
                    //scalar , ok
                } else {
                    if (dd == null) {
                        dd = d;
                    } else if (!dd.equals(d)) {
                        throw new IllegalArgumentException("Matrix Dimensions does not match. Unable to a eval " + dd + "+" + d);
                    }
                }
            }
            if (dd == null) {
                dd = ComponentDimension.SCALAR;
            }
            if (dd.equals(ComponentDimension.SCALAR)) {
                Expr[] inner = new Expr[expressions.array.length];
                for (int i = 0; i < inner.length; i++) {
                    inner[i] = m[i].getComponent(row, col);
                }
                return Plus.of(inner);
            } else {
                Expr[] inner = new Expr[expressions.array.length];
                for (int i = 0; i < inner.length; i++) {
                    inner[i] = m[i].getComponent(row, col);
                }
                return Plus.of(inner);
            }
        } else {
            throw new ClassCastException();
        }

    }


}


