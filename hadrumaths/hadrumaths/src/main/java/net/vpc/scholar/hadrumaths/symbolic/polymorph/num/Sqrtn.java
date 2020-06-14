package net.vpc.scholar.hadrumaths.symbolic.polymorph.num;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.util.internal.CanProduceClass;
import net.vpc.scholar.hadrumaths.symbolic.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by vpc on 4/30/14.
 */
@CanProduceClass({/*DoubleToDouble.class,*/DoubleToComplex.class,DoubleToVector.class,DoubleToMatrix.class})
public abstract class Sqrtn implements FunctionExpr {
    private static final long serialVersionUID = 1L;
    protected int n;
    private final Expr arg;

    protected Sqrtn(Expr arg, int n) {
        this.arg = arg;
        this.n = n;
    }

    @Override
    public String getName() {
        return "sqrtn";
    }

    @Override
    public String toLatex() {
        StringBuilder sb=new StringBuilder();
        sb.append("\\sqrt["+n+"]{").append(arg.toLatex()).append("}");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass().getName(),n,  arg);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sqrtn sqrtn = (Sqrtn) o;
        return n == sqrtn.n &&
                Objects.equals(arg, sqrtn.arg);
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
        return Sqrtn.of(argument[0], n);
    }

    @Override
    public Domain getDomain() {
        return getChild(0).getDomain();
    }

//    public static class SqrtnDoubleToDouble extends Sqrtn implements DoubleToDouble {
//        public SqrtnDoubleToDouble(Expr arg, int n) {
//            super(arg,n);
//        }
//        @Override
//        public DoubleToDouble toDD() {
//            return this;
//        }
//    }

    public static Sqrtn of(Expr e, int n) {
        switch (e.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE://{return new SqrtnDoubleToDouble(e,n);}
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                return new SqrtnDoubleToComplex(e, n);
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                return new SqrtnDoubleToVector(e, n);
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                return new SqrtnDoubleToMatrix(e, n);
            }
            default: {
                throw new IllegalArgumentException("Unsupported type "+e.getType());
            }
        }
    }

}
class SqrtnDoubleToComplex extends Sqrtn implements DoubleToComplexDefaults.DoubleToComplexUnaryDC {
    public SqrtnDoubleToComplex(Expr arg, int n) {
        super(arg, n);
    }

    @Override
    public Complex aggregateComplex(Complex a) {
        return a.sqrt(n);
    }

}

class SqrtnDoubleToVector extends Sqrtn implements DoubleToVectorDefaults.DoubleToVectorUnaryDV {
    public SqrtnDoubleToVector(Expr arg, int n) {
        super(arg, n);
    }

    @Override
    public ComplexVector aggregateVector(ComplexVector v) {
        return v.sqrt(n);
    }
}

class SqrtnDoubleToMatrix extends Sqrtn implements DoubleToMatrixDefaults.DoubleToMatrixUnaryDM {
    public SqrtnDoubleToMatrix(Expr arg, int n) {
        super(arg, n);
    }

    @Override
    public ComplexMatrix aggregateMatrix(ComplexMatrix m) {
        return m.sqrt(n);
    }
}
