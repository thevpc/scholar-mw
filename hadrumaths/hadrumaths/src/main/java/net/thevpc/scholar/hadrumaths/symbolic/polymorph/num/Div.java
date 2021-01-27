/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.symbolic.polymorph.num;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.*;
import net.thevpc.scholar.hadrumaths.transform.ExpressionTransform;
import net.thevpc.scholar.hadrumaths.transform.ExpressionTransformer;
import net.thevpc.scholar.hadrumaths.util.ArrayUtils;
import net.thevpc.scholar.hadrumaths.util.internal.CanProduceClass;
import net.thevpc.scholar.hadrumaths.util.internal.NonStateField;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author vpc
 */
@CanProduceClass({DoubleToDouble.class, DoubleToComplex.class, DoubleToVector.class, DoubleToMatrix.class})
public abstract class Div implements OperatorExpr {
    private static final long serialVersionUID = 1L;

    static {
        ExpressionTransformFactory.setExpressionTransformer(Div.class, ExpressionTransform.class, new ExpressionTransformer() {

            public Expr transform(Expr expression, ExpressionTransform transform) {
                Div e = (Div) expression;
                return Div.of(ExpressionTransformFactory.transform(e.getFirst(), transform), ExpressionTransformFactory.transform(e.getSecond(), transform));
            }
        });
    }

    protected Expr[] expressions;
    @NonStateField
    protected Domain _domain;

    protected Div(Expr first, Expr second) {
        this.expressions = new Expr[]{first, second};
        _domain = first.getDomain().intersect(second.getDomain());
    }

    public static Div of(Expr first, Expr second) {
        switch (first.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                switch (second.getType()) {
                    case DOUBLE_NBR:
                    case DOUBLE_EXPR:
                    case DOUBLE_DOUBLE: {
                        return new DivDoubleToDouble(first, second);
                    }
                    case COMPLEX_NBR:
                    case COMPLEX_EXPR:
                    case DOUBLE_COMPLEX: {
                        return new DivDoubleToComplex(first, second);
                    }
                    case CVECTOR_NBR:
                    case CVECTOR_EXPR:
                    case DOUBLE_CVECTOR: {
                        return new DivDoubleToVectorDCDV(first.toDC(), second.toDV());
                    }
                    case CMATRIX_NBR:
                    case CMATRIX_EXPR:
                    case DOUBLE_CMATRIX: {
                        return new DivDoubleToMatrixDCDM(first.toDC(), second.toDM());
                    }
                }
                break;
            }
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                switch (second.getType()) {
                    case DOUBLE_NBR:
                    case DOUBLE_EXPR:
                    case DOUBLE_DOUBLE:
                    case COMPLEX_NBR:
                    case COMPLEX_EXPR:
                    case DOUBLE_COMPLEX: {
                        return new DivDoubleToComplex(first, second);
                    }
                    case CVECTOR_NBR:
                    case CVECTOR_EXPR:
                    case DOUBLE_CVECTOR: {
                        return new DivDoubleToVectorDCDV(first.toDC(), second.toDV());
                    }
                    case CMATRIX_NBR:
                    case CMATRIX_EXPR:
                    case DOUBLE_CMATRIX: {
                        return new DivDoubleToMatrixDCDM(first.toDC(), second.toDM());
                    }
                }
                break;
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                switch (second.getType()) {
                    case DOUBLE_NBR:
                    case DOUBLE_EXPR:
                    case DOUBLE_DOUBLE:
                    case COMPLEX_NBR:
                    case COMPLEX_EXPR:
                    case DOUBLE_COMPLEX: {
                        return new DivDoubleToVectorDVDC(first.toDV(), second.toDC());
                    }
                    case CVECTOR_NBR:
                    case CVECTOR_EXPR:
                    case DOUBLE_CVECTOR: {
                        return new DivDoubleToVectorDVDV(first.toDV(), second.toDV());
                    }
                    case CMATRIX_NBR:
                    case CMATRIX_EXPR:
                    case DOUBLE_CMATRIX: {
                        return new DivDoubleToMatrixDVDM(first.toDV(), second.toDM());
                    }
                }
                break;
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                switch (second.getType()) {
                    case DOUBLE_NBR:
                    case DOUBLE_EXPR:
                    case DOUBLE_DOUBLE:
                    case COMPLEX_NBR:
                    case COMPLEX_EXPR:
                    case DOUBLE_COMPLEX: {
                        return new DivDoubleToMatrixDMDC(first.toDM(), second.toDC());
                    }
                    case CVECTOR_NBR:
                    case CVECTOR_EXPR:
                    case DOUBLE_CVECTOR:
                    case CMATRIX_NBR: {
                        return new DivDoubleToMatrixDMDV(first.toDM(), second.toDV());
                    }
                    case CMATRIX_EXPR:
                    case DOUBLE_CMATRIX: {
                        return new DivDoubleToMatrixDMDM(first.toDM(), second.toDM());
                    }
                }
                break;
            }
        }
        throw new IllegalArgumentException("Unsupported " + first.getType() + " / " + second.getType());
    }

    @Override
    public String getName() {
        return "/";
    }

    public String getOperatorName() {
        return "/";
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getClass().getName());
        result = 31 * result + Arrays.hashCode(expressions);
        return result;
    }

    @Override
    public String toLatex() {
        return "\\frac{"+expressions[0].toLatex()+"}{"+expressions[1].toLatex()+"}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Div div = (Div) o;
        return Arrays.equals(expressions, div.expressions) &&
                Objects.equals(_domain, div._domain);
    }

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }

    public Expr getFirst() {
        return expressions[0];
    }

    public Expr getSecond() {
        return expressions[1];
    }

    @Override
    public Complex toComplex() {
        return getFirst().toComplex().div(getSecond().toComplex());
    }

    @Override
    public double toDouble() {
        return toComplex().toDouble();
    }

    public boolean isZero() {
        return getFirst().isZero() && !getSecond().isZero();
    }

    public boolean isNaN() {
        return ExprDefaults.isNaNAny(expressions);
    }

    public List<Expr> getChildren() {
        return Arrays.asList(expressions);
    }

    public boolean isInfinite() {
        return ExprDefaults.isInfiniteAny(expressions);
    }

    @Override
    public ComponentDimension getComponentDimension() {
        for (Expr expression : expressions) {
            ComponentDimension d = expression.getComponentDimension();
            if (d.rows != 1 || d.columns != 1) {
                return d;
            }
        }
        return ComponentDimension.SCALAR;
    }

    @Override
    public Expr mul(Complex other) {
        if (other.isZero()) {
            return Maths.ZERO;
        }
        if (other.isReal()) {
            return mul(other.toDouble());
        }
        Expr[] expr2 = ArrayUtils.copy(expressions);
        expr2[0] = expr2[0].mul(other);
        return Div.of(expr2[0], expr2[1]);
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
        Expr[] expr2 = ArrayUtils.copy(expressions);
        for (int i = 0; i < expr2.length; i++) {
            expr2[i] = expr2[i].mul(domain);
        }
        return Div.of(expr2[0], expr2[1]);
    }

    @Override
    public Expr mul(double other) {
        if (other == 0) {
            return Maths.ZERO;
        }
        Expr[] expr2 = ArrayUtils.copy(expressions);
        expr2[0] = expr2[0].mul(other);
        return Div.of(expr2[0], expr2[1]);
    }

    @Override
    public final Domain getDomain() {
        return _domain;
    }

    public Expr newInstance(Expr[] a) {
        return Div.of(a[0], a[1]);
    }


}

class DivDoubleToDouble extends Div implements DoubleToDoubleDefaults.DoubleToDoubleBinaryDDDD, DoubleToDoubleDefaults.DoubleToDoubleNormal {
    public DivDoubleToDouble(Expr first, Expr second) {
        super(first.toDD(), second.toDD());
    }

    @Override
    public double evalDoubleSimple(double a, double b) {
        return a / b;
    }
}

class DivDoubleToComplex extends Div implements DoubleToComplexDefaults.DoubleToComplexDCDC {
    public DivDoubleToComplex(Expr first, Expr second) {
        super(first.toDC(), second.toDC());
    }

    @Override
    public Complex aggregateComplex(Complex a, Complex b) {
        return a.div(b);
    }
}

class DivDoubleToVectorDVDC extends Div implements DoubleToVectorDefaults.DoubleToVectorDVDC {
    public DivDoubleToVectorDVDC(Expr first, Expr second) {
        super(first.toDV(), second.toDC());
    }

    @Override
    public ComplexVector evalVectorSimple(ComplexVector a, Complex b) {
        return a.div(b);
    }
}

class DivDoubleToVectorDCDV extends Div implements DoubleToVectorDefaults.DoubleToVectorDCDV {
    public DivDoubleToVectorDCDV(Expr first, Expr second) {
        super(first.toDC(), second.toDV());
    }

    @Override
    public ComplexVector evalVectorSimple(Complex a, ComplexVector b) {
        return b.inv().mul(a);
    }
}

class DivDoubleToVectorDVDV extends Div implements DoubleToVectorDefaults.DoubleToVectorDVDV {
    public DivDoubleToVectorDVDV(Expr first, Expr second) {
        super(first.toDV(), second.toDV());
    }

    @Override
    public ComplexVector evalVectorSimple(ComplexVector a, ComplexVector b) {
        return a.toMatrix().div(b.toMatrix()).toVector();
    }
}

class DivDoubleToMatrixDCDM extends Div implements DoubleToMatrixDefaults.DoubleToMatrixDCDM {
    public DivDoubleToMatrixDCDM(Expr first, Expr second) {
        super(first.toDC(), second.toDM());
    }

    @Override
    public ComplexMatrix evalMatrixSimple(Complex a, ComplexMatrix b) {
        return b.inv().mul(a);
    }
}

class DivDoubleToMatrixDMDC extends Div implements DoubleToMatrixDefaults.DoubleToMatrixDMDC {
    public DivDoubleToMatrixDMDC(Expr first, Expr second) {
        super(first.toDM(), second.toDC());
    }

    @Override
    public ComplexMatrix evalMatrixSimple(ComplexMatrix a, Complex b) {
        return a.div(b);
    }
}

class DivDoubleToMatrixDVDM extends Div implements DoubleToMatrixDefaults.DoubleToMatrixDVDM {
    public DivDoubleToMatrixDVDM(Expr first, Expr second) {
        super(first.toDV(), second.toDM());
    }

    @Override
    public ComplexMatrix evalMatrixSimple(ComplexVector a, ComplexMatrix b) {
        return a.toMatrix().div(b);
    }

}

class DivDoubleToMatrixDMDV extends Div implements DoubleToMatrixDefaults.DoubleToMatrixDMDV {
    public DivDoubleToMatrixDMDV(Expr first, Expr second) {
        super(first.toDM(), second.toDV());
    }

    @Override
    public ComplexMatrix evalMatrixSimple(ComplexMatrix a, ComplexVector b) {
        return a.div(b.toMatrix());
    }
}

class DivDoubleToMatrixDMDM extends Div implements DoubleToMatrixDefaults.DoubleToMatrixDMDM {
    public DivDoubleToMatrixDMDM(Expr first, Expr second) {
        super(first.toDM(), second.toDM());
    }

    @Override
    public ComplexMatrix evalMatrixSimple(ComplexMatrix a, ComplexMatrix b) {
        return a.div(b);
    }

}

