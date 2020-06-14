package net.vpc.scholar.hadrumaths.symbolic.polymorph.trigo;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.vpc.scholar.hadrumaths.util.internal.CanProduceClass;

/**
 * Created by vpc on 4/30/14.
 */
@CanProduceClass({/*DoubleToDouble.class,*/DoubleToComplex.class,DoubleToVector.class,DoubleToMatrix.class})
public abstract class Acosh implements FunctionExpr {
    private static final long serialVersionUID = 1L;
    private final Expr arg;

    protected Acosh(Expr arg) {
        this.arg = arg;
    }

    @Override
    public String getName() {
        return "acosh";
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
        Acosh acosh = (Acosh) o;
        return Objects.equals(arg, acosh.arg);
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
    public Domain getDomain() {
        return getChild(0).getDomain();
    }

    @Override
    public Expr newInstance(Expr[] argument) {
        return Acosh.of(argument[0]);
    }

//    public static class AcoshDoubleToDouble extends Acosh implements GenericFunctionXDoubleToDouble {
//        public AcoshDoubleToDouble(Expr arg) {
//            super(arg);
//        }
//
//        @Override
//        public DoubleToDouble toDD() {
//            return this;
//        }
//    }

    public static Acosh of(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE:
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: // complex function
            {
                return new AcoshDoubleToComplex(e);
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                return new AcoshDoubleToVector(e);
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                return new AcoshDoubleToMatrix(e);
            }
            default: {
                throw new IllegalArgumentException("Unsupported type "+e.getType());
            }
        }
    }

}

class AcoshDoubleToComplex extends Acosh implements DoubleToComplexDefaults.DoubleToComplexUnaryDC {
    public AcoshDoubleToComplex(Expr arg) {
        super(arg);
    }

    @Override
    public Complex aggregateComplex(Complex a) {
        return a.acosh();
    }

}

class AcoshDoubleToVector extends Acosh implements DoubleToVectorDefaults.DoubleToVectorUnaryDV {
    public AcoshDoubleToVector(Expr arg) {
        super(arg);
    }

    @Override
    public ComplexVector aggregateVector(ComplexVector v) {
        return v.acosh();
    }

}

class AcoshDoubleToMatrix extends Acosh implements DoubleToMatrixDefaults.DoubleToMatrixUnaryDM {
    public AcoshDoubleToMatrix(Expr arg) {
        super(arg);
    }

    @Override
    public ComplexMatrix aggregateMatrix(ComplexMatrix m) {
        return m.acosh();
    }

}
