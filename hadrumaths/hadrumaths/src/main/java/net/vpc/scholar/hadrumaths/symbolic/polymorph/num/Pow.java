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
import net.vpc.scholar.hadrumaths.util.internal.NonStateField;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author vpc
 */
@CanProduceClass({DoubleToDouble.class,DoubleToComplex.class,DoubleToVector.class,DoubleToMatrix.class})
public abstract class Pow implements OperatorExpr {
    private static final long serialVersionUID = 1L;

    static {
        ExpressionTransformFactory.setExpressionTransformer(Pow.class, ExpressionTransform.class, new ExpressionTransformer() {

            public Expr transform(Expr expression, ExpressionTransform transform) {
                Pow e = (Pow) expression;
                return Pow.of(ExpressionTransformFactory.transform(e.getFirst(), transform), ExpressionTransformFactory.transform(e.getSecond(), transform));
            }
        });
    }

    private final Expr[] expressions;
    @NonStateField
    protected Domain _domain;

    protected Pow(Expr first, Expr second) {
        this.expressions = new Expr[]{first, second};
        _domain = first.getDomain().intersect(second.getDomain());
    }

    @Override
    public String getName() {
        return "^^";
    }

//    public Expr getComponent(int row, int col) {
//        if (isNarrow(ExprType.DOUBLE_CMATRIX)) {
//            if (isNarrow(ExprDim.SCALAR) && (row != col || col != 0)) {
//                return FunctionFactory.DZEROXY;
//            }
//            Expr[] inner = new Expr[expressions.length];
//            for (int i = 0; i < inner.length; i++) {
//                inner[i] = expressions[i].toDM().getComponent(row, col);
//            }
//            return Pow.of(inner[0], inner[1]);
//        } else {
//            throw new ClassCastException();
//        }
//    }

    public static Pow of(Expr first, Expr second) {
        switch (first.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                switch (second.getType()) {
                    case DOUBLE_NBR:
                    case DOUBLE_EXPR:
                    case DOUBLE_DOUBLE: {
                        return new PowDoubleToDouble(first, second);
                    }
                    case COMPLEX_NBR:
                    case COMPLEX_EXPR:
                    case DOUBLE_COMPLEX: {
                        return new PowDoubleToComplex(first, second);
                    }
                    case CVECTOR_NBR:
                    case CVECTOR_EXPR:
                    case DOUBLE_CVECTOR: {
                        return new PowDoubleToVectorDCDV(first.toDC(), second.toDV());
                    }
                    case CMATRIX_NBR:
                    case CMATRIX_EXPR:
                    case DOUBLE_CMATRIX: {
                        return new PowDoubleToMatrixDCDM(first.toDC(), second.toDM());
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
                        return new PowDoubleToComplex(first, second);
                    }
                    case CVECTOR_NBR:
                    case CVECTOR_EXPR:
                    case DOUBLE_CVECTOR: {
                        return new PowDoubleToVectorDCDV(first.toDC(), second.toDV());
                    }
                    case CMATRIX_NBR:
                    case CMATRIX_EXPR:
                    case DOUBLE_CMATRIX: {
                        return new PowDoubleToMatrixDCDM(first.toDC(), second.toDM());
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
                        return new PowDoubleToVectorDVDC(first.toDV(), second.toDC());
                    }
                    case CVECTOR_NBR:
                    case CVECTOR_EXPR:
                    case DOUBLE_CVECTOR: {
                        return new PowDoubleToVectorDVDV(first.toDV(), second.toDV());
                    }
                    case CMATRIX_NBR:
                    case CMATRIX_EXPR:
                    case DOUBLE_CMATRIX: {
                        return new PowDoubleToMatrixDVDM(first.toDV(), second.toDM());
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
                        return new PowDoubleToMatrixDMDC(first.toDM(), second.toDC());
                    }
                    case CVECTOR_NBR:
                    case CVECTOR_EXPR:
                    case DOUBLE_CVECTOR:
                    case CMATRIX_NBR: {
                        return new PowDoubleToMatrixDMDV(first.toDM(), second.toDV());
                    }
                    case CMATRIX_EXPR:
                    case DOUBLE_CMATRIX: {
                        return new PowDoubleToMatrixDMDM(first.toDM(), second.toDM());
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
        Pow pow = (Pow) o;
        return Arrays.equals(expressions, pow.expressions) &&
                Objects.equals(_domain, pow._domain);
    }

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }

    public boolean isZero() {
        return (getFirst().isZero() && !getSecond().isZero());
    }


    public boolean isNaN() {
        return ExprDefaults.isNaNAny(expressions);
    }

    @Override
    public String toLatex() {
        return "{"+expressions[1].toLatex()+"}^{"+expressions[1].toLatex()+"}";
    }

    public boolean isInfinite() {
        return ExprDefaults.isInfiniteAny(expressions);
    }


    public Expr getFirst() {
        return expressions[0];
    }

    public Expr getSecond() {
        return expressions[1];
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
        return getFirst().toComplex().pow(getSecond().toComplex());
    }


    @Override
    public double toDouble() {
        return toComplex().toDouble();
    }


    public Expr newInstance(Expr[] a) {
        return Pow.of(a[0], a[1]);
    }


}

class PowDoubleToDouble extends Pow implements DoubleToDoubleDefaults.DoubleToDoubleBinaryDDDD {
    public PowDoubleToDouble(Expr first, Expr second) {
        super(first.toDD(), second.toDD());
    }


    @Override
    public double evalDoubleSimple(double a, double b) {
        return Math.pow(a, b);
    }
}

class PowDoubleToComplex extends Pow implements DoubleToComplexDefaults.DoubleToComplexDCDC {
    public PowDoubleToComplex(Expr first, Expr second) {
        super(first.toDC(), second.toDC());
    }

    @Override
    public Complex aggregateComplex(Complex a, Complex b) {
        return a.pow(b);
    }
}

class PowDoubleToVectorDVDC extends Pow implements DoubleToVectorDefaults.DoubleToVectorDVDC {
    public PowDoubleToVectorDVDC(Expr first, Expr second) {
        super(first.toDV(), second.toDC());
    }

    @Override
    public ComplexVector evalVectorSimple(ComplexVector a, Complex b) {
        return Maths.complexVector(a.pow(b));
    }
}


class PowDoubleToVectorDCDV extends Pow implements DoubleToVectorDefaults.DoubleToVectorDCDV {
    public PowDoubleToVectorDCDV(Expr first, Expr second) {
        super(first, second);
    }

    @Override
    public ComplexVector evalVectorSimple(Complex a, ComplexVector b) {
        return b.neg().add(a);
    }
}

class PowDoubleToVectorDVDV extends Pow implements DoubleToVectorDefaults.DoubleToVectorDVDV {
    public PowDoubleToVectorDVDV(Expr first, Expr second) {
        super(first.toDV(), second.toDV());
    }
    @Override
    public ComplexVector evalVectorSimple(ComplexVector a, ComplexVector b) {
        return a.sub(b);
    }
}

class PowDoubleToMatrixDCDM extends Pow implements DoubleToMatrixDefaults.DoubleToMatrixDCDM {
    public PowDoubleToMatrixDCDM(Expr first, Expr second) {
        super(first.toDC(), second.toDM());
    }

    @Override
    public ComplexMatrix evalMatrixSimple(Complex a, ComplexMatrix b) {
        return b.neg().add(a);
    }

}

class PowDoubleToMatrixDMDC extends Pow implements DoubleToMatrixDefaults.DoubleToMatrixDMDC {
    public PowDoubleToMatrixDMDC(Expr first, Expr second) {
        super(first.toDM(), second.toDC());
    }

    @Override
    public ComplexMatrix evalMatrixSimple(ComplexMatrix a, Complex b) {
        return a.sub(b);
    }

}

class PowDoubleToMatrixDVDM extends Pow implements DoubleToMatrixDefaults.DoubleToMatrixDVDM {
    public PowDoubleToMatrixDVDM(Expr first, Expr second) {
        super(first.toDV(), second.toDM());
    }

    @Override
    public ComplexMatrix evalMatrixSimple(ComplexVector a, ComplexMatrix b) {
        return b.neg().add(a.toMatrix());
    }
}

class PowDoubleToMatrixDMDV extends Pow implements DoubleToMatrixDefaults.DoubleToMatrixDMDV {
    public PowDoubleToMatrixDMDV(Expr first, Expr second) {
        super(first.toDM(), second.toDV());
    }

    @Override
    public ComplexMatrix evalMatrixSimple(ComplexMatrix a, ComplexVector b) {
        return a.sub(b.toMatrix());
    }

}

class PowDoubleToMatrixDMDM extends Pow implements DoubleToMatrixDefaults.DoubleToMatrixDMDM {
    public PowDoubleToMatrixDMDM(Expr first, Expr second) {
        super(first.toDM(), second.toDM());
    }

    @Override
    public ComplexMatrix evalMatrixSimple(ComplexMatrix a, ComplexMatrix b) {
        return a.sub(b);
    }


}

