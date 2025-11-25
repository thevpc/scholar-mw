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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author vpc
 */
@CanProduceClass({DoubleToDouble.class, DoubleToComplex.class, DoubleToVector.class, DoubleToMatrix.class})
public abstract class Inv implements FunctionExpr {
    private static final long serialVersionUID = 1L;
    protected Expr arg;

    protected Inv(Expr arg) {
        this.arg = arg;
    }

    @Override
    public String getName() {
        return "inv";
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode()*31+arg.hashCode();
    }
    @Override
    public String toLatex() {
        StringBuilder sb=new StringBuilder();
        sb.append("\\text{").append(getName()).append("}\\left(");
        sb.append(arg.toLatex());
        sb.append("\\right)");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inv inv = (Inv) o;
        return Objects.equals(arg, inv.arg);
    }

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }

    public static Inv of(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                return new InvDoubleToDouble(e);
            }
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                return new InvDoubleToComplex(e);
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                return new InvDoubleToVector(e);
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                return new InvDoubleToMatrix(e);
            }
            default: {
                throw new IllegalArgumentException("Unsupported type " + e.getType());
            }
        }
    }

    @Override
    public Complex toComplex() {
        return arg.toComplex().inv();
    }

    @Override
    public double toDouble() {
        return 1.0 / arg.toDouble();
    }

    public boolean isZero() {
        return arg.isInfinite();
    }

    public boolean isNaN() {
        return arg.isNaN();
    }

    public List<Expr> getChildren() {
        return Arrays.asList(arg);
    }

    public boolean isInfinite() {
        return arg.isZero();
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return arg.getComponentDimension();
    }

//    @Override
//    public ComplexMatrix toMatrix() {
//        return expression.toMatrix().inv();
//    }

    @Override
    public Expr mul(Complex other) {
        if (other.isZero()) {
            if (arg.isZero()) {
                return Maths.DDNAN;
            }
            return Maths.ZERO;
        }
        return Inv.of(arg.mul(other.inv()));
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
        return Inv.of(arg.mul(domain));
    }

    @Override
    public Expr mul(double other) {
        if (other == 0) {
            return Maths.ZERO;
        }
        return Inv.of(arg.mul(1 / other));
    }

    @Override
    public Domain getDomain() {
        return arg.getDomain();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////


//    @Override
//    public Complex computeComplexArg(double x,BooleanMarker defined) {
//        Out<Range> ranges = new Out<>();
//        Complex complex = computeComplexArg(new double[]{x}, null, ranges)[0];
//        defined.set(ranges.get().getDefined1().get(0));
//        return complex;
//    }

    public Expr newInstance(Expr[] e) {
        return Inv.of(e[0]);
    }
}

class InvDoubleToDouble extends Inv implements DoubleToDoubleDefaults.DoubleToDoubleUnaryDD {
    public InvDoubleToDouble(Expr expression) {
        super(expression.toDD());
    }

    @Override
    public double evalDoubleSimple(double x) {
        return 1 / x;
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Inv");
    }

}

class InvDoubleToComplex extends Inv implements DoubleToComplexDefaults.DoubleToComplexUnaryDC {//WithUnaryExprHelperDoubleToComplex

    public InvDoubleToComplex(Expr expression) {
        super(expression);
    }

    @Override
    public Complex aggregateComplex(Complex a) {
        return a.inv();
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Inv");
    }
}

class InvDoubleToVector extends Inv implements DoubleToVectorDefaults.DoubleToVectorUnaryDV {
    public InvDoubleToVector(Expr expression) {
        super(expression);
    }

    @Override
    public ComplexVector aggregateVector(ComplexVector v) {
        return v.inv();
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Inv");
    }
}

class InvDoubleToMatrix extends Inv implements DoubleToMatrixDefaults.DoubleToMatrixUnaryDM {
    public InvDoubleToMatrix(Expr expression) {
        super(expression);
    }

    @Override
    public ComplexMatrix aggregateMatrix(ComplexMatrix m) {
        return m.inv();
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Inv");
    }
}
