/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic.polymorph.num;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.internal.CanProduceClass;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransform;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransformer;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;
import net.vpc.scholar.hadrumaths.util.internal.NonStateField;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author vpc
 */
@CanProduceClass({DoubleToDouble.class,DoubleToComplex.class,DoubleToVector.class,DoubleToMatrix.class})
public abstract  class Minus implements OperatorExpr {
    private static final long serialVersionUID = 1L;

    static {
        ExpressionTransformFactory.setExpressionTransformer(Minus.class, ExpressionTransform.class, new ExpressionTransformer() {

            public Expr transform(Expr expression, ExpressionTransform transform) {
                Minus e = (Minus) expression;
                return Minus.of(ExpressionTransformFactory.transform(e.getChild(0), transform), ExpressionTransformFactory.transform(e.getChild(1), transform));
            }
        });
    }

    protected Expr[] expressions;
    @NonStateField
    protected Domain _domain;

    protected Minus(Expr first, Expr second) {
        this.expressions = new Expr[]{first, second};
        _domain = first.getDomain().expand(second.getDomain());
    }

    @Override
    public String getName() {
        return "-";
    }

    public String getOperatorName() {
        return "-";
    }

    @Override
    public String toLatex() {
        return "{"+expressions[0].toLatex()+"}-{"+expressions[1].toLatex()+"}";
    }

    public static Minus of(Expr first, Expr second) {
        switch (first.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                switch (second.getType()) {
                    case DOUBLE_NBR:
                    case DOUBLE_EXPR:
                    case DOUBLE_DOUBLE: {
                        return new MinusDoubleToDouble(first, second);
                    }
                    case COMPLEX_NBR:
                    case COMPLEX_EXPR:
                    case DOUBLE_COMPLEX: {
                        return new MinusDoubleToComplex(first, second);
                    }
                    case CVECTOR_NBR:
                    case CVECTOR_EXPR:
                    case DOUBLE_CVECTOR: {
                        return new MinusDoubleToVectorDCDV(first.toDC(), second.toDV());
                    }
                    case CMATRIX_NBR:
                    case CMATRIX_EXPR:
                    case DOUBLE_CMATRIX: {
                        return new MinusDoubleToMatrixDCDM(first.toDC(), second.toDM());
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
                        return new MinusDoubleToComplex(first, second);
                    }
                    case CVECTOR_NBR:
                    case CVECTOR_EXPR:
                    case DOUBLE_CVECTOR: {
                        return new MinusDoubleToVectorDCDV(first.toDC(), second.toDV());
                    }
                    case CMATRIX_NBR:
                    case CMATRIX_EXPR:
                    case DOUBLE_CMATRIX: {
                        return new MinusDoubleToMatrixDCDM(first.toDC(), second.toDM());
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
                        return new MinusDoubleToVectorDVDC(first.toDV(), second.toDC());
                    }
                    case CVECTOR_NBR:
                    case CVECTOR_EXPR:
                    case DOUBLE_CVECTOR: {
                        return new MinusDoubleToVectorDVDV(first.toDV(), second.toDV());
                    }
                    case CMATRIX_NBR:
                    case CMATRIX_EXPR:
                    case DOUBLE_CMATRIX: {
                        return new MinusDoubleToMatrixDVDM(first.toDV(), second.toDM());
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
                        return new MinusDoubleToMatrixDMDC(first.toDM(), second.toDC());
                    }
                    case CVECTOR_NBR:
                    case CVECTOR_EXPR:
                    case DOUBLE_CVECTOR:
                    case CMATRIX_NBR: {
                        return new MinusDoubleToMatrixDMDV(first.toDM(), second.toDV());
                    }
                    case CMATRIX_EXPR:
                    case DOUBLE_CMATRIX: {
                        return new MinusDoubleToMatrixDMDM(first.toDM(), second.toDM());
                    }
                }
                break;
            }
        }
        throw new IllegalArgumentException("Unsupported " + first.getType() + " - " + second.getType());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getClass().getName(),_domain);
        result = 31 * result + Arrays.hashCode(expressions);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Minus minus = (Minus) o;
        return Arrays.equals(expressions, minus.expressions) &&
                Objects.equals(_domain, minus._domain);
    }

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }

    @Override
    public ExprType getNarrowType() {
        return ExprDefaults.narrowest(expressions);
    }


    @Override
    public Expr narrow(ExprType other) {
        if (is(other)) {
            return this;
        }
        return newInstance(ExprDefaults.narrowAll(other, expressions)).cast(other);
    }


    public boolean isZero() {
        return ExprDefaults.isZeroAll(expressions);
    }


    public boolean isNaN() {
        return ExprDefaults.isNaNAny(expressions);
    }


    public boolean isInfinite() {
        return ExprDefaults.isInfiniteAny(expressions);
    }


    @Override
    public Expr getChild(int index) {
        return ExprDefaults.getChild(index, expressions);
    }


    @Override
    public final Domain getDomain() {
        return _domain;
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


    public List<Expr> getChildren() {
        return Arrays.asList(expressions);
    }


    @Override
    public Complex toComplex() {
        return getChild(0).toComplex().minus(getChild(1).toComplex());
    }


    @Override
    public double toDouble() {
        return toComplex().toDouble();
    }


    public Expr newInstance(Expr[] a) {
        return Minus.of(a[0], a[1]);
    }

    @Override
    public Expr mul(Domain domain) {
        Expr[] expr2 = ArrayUtils.copy(expressions);
        for (int i = 0; i < expr2.length; i++) {
            expr2[i] = expr2[i].mul(domain);
        }
        return Minus.of(expr2[0], expr2[1]);
    }

    @Override
    public Expr mul(double other) {
        if (other == 0) {
            return Maths.ZERO;
        }
        Expr[] expr2 = ArrayUtils.copy(expressions);
        for (int i = 0; i < expr2.length; i++) {
            expr2[i] = expr2[i].mul(other);
        }
        return Minus.of(expr2[0], expr2[1]);
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
        for (int i = 0; i < expr2.length; i++) {
            expr2[i] = expr2[i].mul(other);
        }
        return Minus.of(expr2[0], expr2[1]);
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


}

class MinusDoubleToDouble extends Minus implements DoubleToDoubleDefaults.DoubleToDoubleBinaryDDDD {
    public MinusDoubleToDouble(Expr first, Expr second) {
        super(first.toDD(), second.toDD());
    }

    @Override
    public double evalDoubleSimple(double a, double b) {
        return a - b;
    }


    /// reimplementing this because sub should be equivalent to plus in that
    /// sens that a sub of so that :
    //  x - <undefined> = x
    //  <undefined> - x  = x
    //  <undefined> - <undefined>  = <undefined>

    public double evalDouble(double x, double y, double z, BooleanMarker defined) {
        List<Expr> operands = this.getChildren();
        BooleanRef defined1 = BooleanMarker.ref();
        BooleanRef defined2 = BooleanMarker.ref();
        double a = operands.get(0).toDD().evalDouble(x, y, z, defined1);
        double b = operands.get(1).toDD().evalDouble(x, y, z, defined2);
        if (defined1.get() || defined2.get()) {
            defined.set();
            return evalDoubleSimple(a, b);
        }
        return 0;
    }

    public double evalDouble(double x, BooleanMarker defined) {
        List<Expr> operands = this.getChildren();
        BooleanRef defined1 = BooleanMarker.ref();
        BooleanRef defined2 = BooleanMarker.ref();
        double a = operands.get(0).toDD().evalDouble(x, defined1);
        double b = operands.get(1).toDD().evalDouble(x, defined2);
        if (defined1.get() || defined2.get()) {
            defined.set();
            return evalDoubleSimple(a, b);
        }
        return 0;
    }

    public double evalDouble(double x, double y, BooleanMarker defined) {
        List<Expr> operands = this.getChildren();
        BooleanRef defined1 = BooleanMarker.ref();
        BooleanRef defined2 = BooleanMarker.ref();
        double a = operands.get(0).toDD().evalDouble(x, y, defined1);
        double b = operands.get(1).toDD().evalDouble(x, y, defined2);
        if (defined1.get() || defined2.get()) {
            defined.set();
            return evalDoubleSimple(a, b);
        }
        return 0;
    }
}

class MinusDoubleToComplex extends Minus implements DoubleToComplexDefaults.DoubleToComplexDCDC {
    public MinusDoubleToComplex(Expr first, Expr second) {
        super(first.toDC(), second.toDC());
    }

    @Override
    public Complex aggregateComplex(Complex a, Complex b) {
        return a.minus(b);
    }

}

class MinusDoubleToVectorDVDC extends Minus implements DoubleToVectorDefaults.DoubleToVectorDVDC {
    public MinusDoubleToVectorDVDC(Expr first, Expr second) {
        super(first.toDV(), second.toDC());
    }

    @Override
    public ComplexVector evalVectorSimple(ComplexVector a, Complex b) {
        return a.sub(b);
    }

}

class MinusDoubleToVectorDCDV extends Minus implements DoubleToVectorDefaults.DoubleToVectorDCDV {
    public MinusDoubleToVectorDCDV(Expr first, Expr second) {
        super(first.toDC(), second.toDV());
    }

    @Override
    public ComplexVector evalVectorSimple(Complex a, ComplexVector b) {
        return b.neg().add(a);
    }

}

class MinusDoubleToVectorDVDV extends Minus implements DoubleToVectorDefaults.DoubleToVectorDVDV {
    public MinusDoubleToVectorDVDV(Expr first, Expr second) {
        super(first.toDV(), second.toDV());
    }

    @Override
    public ComplexVector evalVectorSimple(ComplexVector a, ComplexVector b) {
        return a.sub(b);
    }

}

class MinusDoubleToMatrixDCDM extends Minus implements DoubleToMatrixDefaults.DoubleToMatrixDCDM {
    public MinusDoubleToMatrixDCDM(Expr first, Expr second) {
        super(first.toDC(), second.toDM());
    }

    @Override
    public ComplexMatrix evalMatrixSimple(Complex a, ComplexMatrix b) {
        return b.neg().add(a);
    }


}

class MinusDoubleToMatrixDMDC extends Minus implements DoubleToMatrixDefaults.DoubleToMatrixDMDC {
    public MinusDoubleToMatrixDMDC(Expr first, Expr second) {
        super(first.toDM(), second.toDC());
    }

    @Override
    public ComplexMatrix evalMatrixSimple(ComplexMatrix a, Complex b) {
        return a.sub(b);
    }

}

class MinusDoubleToMatrixDVDM extends Minus implements DoubleToMatrixDefaults.DoubleToMatrixDVDM {
    public MinusDoubleToMatrixDVDM(Expr first, Expr second) {
        super(first.toDV(), second);
    }

    @Override
    public ComplexMatrix evalMatrixSimple(ComplexVector a, ComplexMatrix b) {
        return b.neg().add(a.toMatrix());
    }

}

class MinusDoubleToMatrixDMDV extends Minus implements DoubleToMatrixDefaults.DoubleToMatrixDMDV {
    public MinusDoubleToMatrixDMDV(Expr first, Expr second) {
        super(first.toDM(), second.toDV());
    }

    @Override
    public ComplexMatrix evalMatrixSimple(ComplexMatrix a, ComplexVector b) {
        return a.sub(b.toMatrix());
    }


}

class MinusDoubleToMatrixDMDM extends Minus implements DoubleToMatrixDefaults.DoubleToMatrixDMDM {
    public MinusDoubleToMatrixDMDM(Expr first, Expr second) {
        super(first.toDM(), second.toDM());
    }

    @Override
    public ComplexMatrix evalMatrixSimple(ComplexMatrix a, ComplexMatrix b) {
        return a.sub(b);
    }

}
