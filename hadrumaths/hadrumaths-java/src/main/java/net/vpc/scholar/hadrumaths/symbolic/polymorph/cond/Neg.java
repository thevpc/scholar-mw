/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic.polymorph.cond;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.random.CanProduceClass;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransform;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransformer;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author vpc
 */
@CanProduceClass({DoubleToDouble.class, DoubleToComplex.class, DoubleToVector.class, DoubleToMatrix.class})
public abstract class Neg implements OperatorExpr {
    private static final long serialVersionUID = 1L;

    static {
        ExpressionTransformFactory.setExpressionTransformer(Neg.class, ExpressionTransform.class, new ExpressionTransformer() {

            public Expr transform(Expr expression, ExpressionTransform transform) {
                Neg e = (Neg) expression;
                return Neg.of(ExpressionTransformFactory.transform(e.getChild(0), transform));
            }
        });
    }

    protected Expr arg;

    protected Neg(Expr expression) {
        this.arg = expression;
    }

    @Override
    public String getName() {
        return "-";
    }

    public boolean isZero() {
        return arg.isZero();
    }

    public List<Expr> getChildren() {
        return Arrays.asList(arg);
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return arg.getComponentDimension();
    }

    @Override
    public Expr mul(Complex other) {
        if (other.isReal()) {
            return mul(other.toDouble());
        }
        return ExprDefaults.mul(this, other);
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
        return newInstance(arg.mul(domain));
    }

    @Override
    public Expr mul(double other) {
        if (other >= 0) {
            return newInstance(arg.mul(other));
        } else {
            return newInstance(arg.mul(-other));
        }
    }

    @Override
    public Domain getDomain() {
        return arg.getDomain();
    }

    @Override
    public Neg newInstance(Expr... e) {
        return Neg.of(e[0]);
    }

    public static Neg of(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                return new NegDoubleToDouble(e);
            }
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                return new NegDoubleToComplex(e);
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                return new NegDoubleToVector(e);
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                return new NegDoubleToMatrix(e);
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode()*31+arg.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Neg neg = (Neg) o;
        return Objects.equals(arg, neg.arg);
    }

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }

}

class NegDoubleToMatrix extends Neg implements DoubleToMatrixDefaults.DoubleToMatrixUnaryDM {
    public NegDoubleToMatrix(Expr expression) {
        super(expression.toDM());
    }


    @Override
    public ComplexMatrix aggregateMatrix(ComplexMatrix m) {
        return m.neg();
    }
}

class NegDoubleToComplex extends Neg implements DoubleToComplexDefaults.DoubleToComplexUnaryDC {
    public NegDoubleToComplex(Expr expression) {
        super(expression.toDC());
    }

    @Override
    public Complex aggregateComplex(Complex a) {
        return a.neg();
    }

}

class NegDoubleToVector extends Neg implements DoubleToVectorDefaults.DoubleToVectorUnaryDV {
    public NegDoubleToVector(Expr expression) {
        super(expression.toDV());
    }

    @Override
    public ComplexVector aggregateVector(ComplexVector v) {
        return v.neg();
    }

    public int getComponentSize() {
        return arg.toDV().getComponentSize();
    }

    public DoubleToComplex getComponent(Axis a) {
        return Neg.of(arg.toDV().getComponent(a)).toDC();
    }
}

class NegDoubleToDouble extends Neg implements DoubleToDoubleDefaults.DoubleToDoubleUnaryDD {
    public NegDoubleToDouble(Expr expression) {
        super(expression.toDD());
    }

    @Override
    public double evalDoubleSimple(double x) {
        return -x;
    }
}
