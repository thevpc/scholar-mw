package net.vpc.scholar.hadrumaths.symbolic.polymorph.trigo;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.internal.CanProduceClass;
import net.vpc.scholar.hadrumaths.symbolic.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by vpc on 4/30/14.
 */
@CanProduceClass({DoubleToDouble.class,DoubleToComplex.class,DoubleToVector.class,DoubleToMatrix.class})
public abstract class Cosh implements FunctionExpr {
    private static final long serialVersionUID = 1L;
    private final Expr arg;

    protected Cosh(Expr arg) {
        this.arg = arg;
    }

    @Override
    public String getName() {
        return "cosh";
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
        Cosh cosh = (Cosh) o;
        return Objects.equals(arg, cosh.arg);
    }

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }

    @Override
    public List<Expr> getChildren() {
        return Arrays.asList(arg);
    }

    @Override
    public Expr getChild(int index) {
        return ExprDefaults.getChild(index, arg);
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return getChild(0).getComponentDimension();
    }

    @Override
    public Expr newInstance(Expr[] argument) {
        return Cosh.of(argument[0]);
    }

    @Override
    public Domain getDomain() {
        return getChild(0).getDomain();
    }

    public static Cosh of(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                return new CoshDoubleToDouble(e);
            }
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                return new CoshDoubleToComplex(e);
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                return new CoshDoubleToVector(e);
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                return new CoshDoubleToMatrix(e);
            }
            default: {
                throw new IllegalArgumentException("Unsupported type "+e.getType());
            }
        }
    }

}

class CoshDoubleToDouble extends Cosh implements DoubleToDoubleDefaults.DoubleToDoubleUnaryDD {
    public CoshDoubleToDouble(Expr arg) {
        super(arg);
    }
    @Override
    public double evalDoubleSimple(double x) {
        return Maths.cosh(x);
    }
}

class CoshDoubleToComplex extends Cosh implements DoubleToComplexDefaults.DoubleToComplexUnaryDC {
    public CoshDoubleToComplex(Expr arg) {
        super(arg);
    }

    @Override
    public Complex aggregateComplex(Complex a) {
        return a.cosh();
    }
}

class CoshDoubleToVector extends Cosh implements DoubleToVectorDefaults.DoubleToVectorUnaryDV {
    public CoshDoubleToVector(Expr arg) {
        super(arg);
    }

    @Override
    public ComplexVector aggregateVector(ComplexVector v) {
        return v.cosh();
    }
}

class CoshDoubleToMatrix extends Cosh implements DoubleToMatrixDefaults.DoubleToMatrixUnaryDM {
    public CoshDoubleToMatrix(Expr arg) {
        super(arg);
    }

    @Override
    public ComplexMatrix aggregateMatrix(ComplexMatrix m) {
        return m.cosh();
    }
}
