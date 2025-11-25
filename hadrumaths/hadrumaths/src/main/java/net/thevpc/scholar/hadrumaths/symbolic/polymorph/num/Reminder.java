/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.symbolic.polymorph.num;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.util.internal.CanProduceClass;
import net.thevpc.scholar.hadrumaths.symbolic.*;
import net.thevpc.scholar.hadrumaths.transform.ExpressionTransform;
import net.thevpc.scholar.hadrumaths.transform.ExpressionTransformer;
import net.thevpc.scholar.hadrumaths.util.ArrayUtils;
import net.thevpc.scholar.hadrumaths.util.internal.NonStateField;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author vpc
 */
@CanProduceClass({/*DoubleToDouble.class,*/DoubleToComplex.class,DoubleToVector.class,DoubleToMatrix.class})
public abstract class Reminder implements OperatorExpr {
    private static final long serialVersionUID = 1L;


    static {
        ExpressionTransformFactory.setExpressionTransformer(Reminder.class, ExpressionTransform.class, new ExpressionTransformer() {

            public Expr transform(Expr expression, ExpressionTransform transform) {
                Reminder e = (Reminder) expression;
                return Reminder.of(ExpressionTransformFactory.transform(e.getFirst(), transform), ExpressionTransformFactory.transform(e.getSecond(), transform));
            }
        });
    }

    @NonStateField
    private final Domain _domain;
    protected Expr[] expressions;

    protected Reminder(Expr first, Expr second) {
        this.expressions = new Expr[]{first, second};
        _domain = first.getDomain().expand(second.getDomain());
    }

    @Override
    public String getName() {
        return "%";
    }

    public String getOperatorName() {
        return "%";
    }

    @Override
    public Complex toComplex() {
        return getFirst().toComplex().rem(getSecond().toComplex());
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
        return Mul.of(expr2[0], expr2[1]);
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
        return Reminder.of(expr2[0], expr2[1]);
    }

    @Override
    public Expr mul(double other) {
        if (other == 0) {
            return Maths.ZERO;
        }
        Expr[] expr2 = ArrayUtils.copy(expressions);
        expr2[0] = expr2[0].mul(other);
        return Mul.of(expr2[0], expr2[1]);
    }

    @Override
    public String toLatex() {
        StringBuilder sb=new StringBuilder();
        sb.append("{").append(expressions[0].toLatex()).append("}");
        sb.append("\\text{mod}");
        sb.append("{").append(expressions[1].toLatex()).append("}");
        return sb.toString();
    }

    @Override
    public final Domain getDomain() {
        return _domain;
    }

    public Expr newInstance(Expr[] a) {
        return Reminder.of(a[0], a[1]);
    }

    public static Reminder of(Expr first, Expr second) {
        switch (first.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                switch (second.getType()) {
                    case DOUBLE_NBR:
                    case DOUBLE_EXPR:
                    case DOUBLE_DOUBLE: {
                        return new ReminderDoubleToDouble(first, second);
                    }
                    case COMPLEX_NBR:
                    case COMPLEX_EXPR:
                    case DOUBLE_COMPLEX: {
                        return new ReminderDoubleToComplex(first, second);
                    }
                    case CVECTOR_NBR:
                    case CVECTOR_EXPR:
                    case DOUBLE_CVECTOR:
                    case CMATRIX_NBR:
                    case CMATRIX_EXPR:
                    case DOUBLE_CMATRIX: {
                        return new ReminderDoubleToMatrixDMDM(first.toDM(), second.toDM());
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
                        return new ReminderDoubleToComplex(first, second);
                    }
                    case CVECTOR_NBR:
                    case CVECTOR_EXPR:
                    case DOUBLE_CVECTOR:
                    case CMATRIX_NBR:
                    case CMATRIX_EXPR:
                    case DOUBLE_CMATRIX: {
                        return new ReminderDoubleToMatrixDMDM(first.toDM(), second.toDM());
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
                        return new ReminderDoubleToVectorDVDC(first.toDV(), second.toDC());
                    }
                    case CVECTOR_NBR:
                    case CVECTOR_EXPR:
                    case DOUBLE_CVECTOR:
                    case CMATRIX_NBR:
                    case CMATRIX_EXPR:
                    case DOUBLE_CMATRIX: {
                        return new ReminderDoubleToMatrixDMDM(first.toDM(), second.toDM());
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
                    case DOUBLE_COMPLEX:
                    case COMPLEX_EXPR: {
                        return new ReminderDoubleToMatrixDMDC(first.toDM(), second.toDC());
                    }
                    case CVECTOR_NBR:
                    case CVECTOR_EXPR:
                    case DOUBLE_CVECTOR:
                    case CMATRIX_NBR:
                    case CMATRIX_EXPR:
                    case DOUBLE_CMATRIX: {
                        return new ReminderDoubleToMatrixDMDM(first.toDM(), second.toDM());
                    }
                }
                break;
            }
        }
        throw new IllegalArgumentException("Unsupported " + first.getType() + " % " + second.getType());
    }

    public Expr getFirst() {
        return expressions[0];
    }

    public Expr getSecond() {
        return expressions[1];
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
        Reminder reminder = (Reminder) o;
        return Arrays.equals(expressions, reminder.expressions) &&
                Objects.equals(_domain, reminder._domain);
    }

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }

}

class ReminderDoubleToDouble extends Reminder implements DoubleToDoubleDefaults.DoubleToDoubleBinaryDDDD {
    public ReminderDoubleToDouble(Expr first, Expr second) {
        super(first, second);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Reminder");
    }

    @Override
    public double evalDoubleSimple(double a, double b) {
        return a % b;
    }
}

class ReminderDoubleToComplex extends Reminder implements DoubleToComplexDefaults.DoubleToComplexDCDC {
    public ReminderDoubleToComplex(Expr first, Expr second) {
        super(first, second);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Reminder");
    }

    @Override
    public Complex aggregateComplex(Complex a, Complex b) {
        return a.rem(b);
    }

}

//    public static class ReminderDoubleToVectorDVDV extends Reminder implements DoubleToVectorDefaults.DoubleToVectorDVDV {
//        public ReminderDoubleToVectorDVDV(Expr first, Expr second) {
//            super(first, second);
//        }
//
//        @Override
//        public DoubleToVector toDV() {
//            return this;
//        }
//
//        @Override
//        public ComplexVector computeVector(ComplexVector dv, ComplexVector dc) {
//            return dv.rem(dc);
//        }
//    }

class ReminderDoubleToVectorDVDC extends Reminder implements DoubleToVectorDefaults.DoubleToVectorDVDC {
    public ReminderDoubleToVectorDVDC(Expr first, Expr second) {
        super(first, second);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Reminder");
    }


    @Override
    public ComplexVector evalVectorSimple(ComplexVector dv, Complex dc) {
        return dv.rem(dc);
    }
}

class ReminderDoubleToMatrixDMDC extends Reminder implements DoubleToMatrixDefaults.DoubleToMatrixDMDC {
    public ReminderDoubleToMatrixDMDC(Expr first, Expr second) {
        super(first, second);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Reminder");
    }


    @Override
    public ComplexMatrix evalMatrixSimple(ComplexMatrix dv, Complex dc) {
        return dv.rem(dc);
    }
}

class ReminderDoubleToMatrixDMDM extends Reminder implements DoubleToMatrixDefaults.DoubleToMatrixDMDM {
    public ReminderDoubleToMatrixDMDM(Expr first, Expr second) {
        super(first, second);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Reminder");
    }


    @Override
    public ComplexMatrix evalMatrixSimple(ComplexMatrix dv, ComplexMatrix dc) {
        return dv.rem(dc);
    }
}
