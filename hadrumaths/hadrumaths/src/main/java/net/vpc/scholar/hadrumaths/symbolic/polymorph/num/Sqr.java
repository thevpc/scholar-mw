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
@CanProduceClass({DoubleToDouble.class, DoubleToComplex.class, DoubleToVector.class, DoubleToMatrix.class})
public abstract class Sqr implements FunctionExpr {
    private static final long serialVersionUID = 1L;
    private final Expr arg;

    protected Sqr(Expr arg) {
        this.arg = arg;
    }

    @Override
    public String getName() {
        return "sqr";
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode()*31+arg.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sqr sqr = (Sqr) o;
        return Objects.equals(arg, sqr.arg);
    }

    @Override
    public String toLatex() {
        StringBuilder sb=new StringBuilder();
        sb.append("{").append(arg.toLatex()).append("}");
        sb.append("^");
        sb.append("{").append("2").append("}");
        return sb.toString();
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
        return Sqr.of(argument[0]);
    }

    public static Sqr of(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                return new SqrDoubleToDouble(e);
            }
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                return new SqrDoubleToComplex(e);
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                return new SqrDoubleToVector(e);
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                return new SqrDoubleToMatrix(e);
            }
            default: {
                throw new IllegalArgumentException("Unsupported type "+e.getType());
            }
        }
    }


}

class SqrDoubleToDouble extends Sqr implements DoubleToDoubleDefaults.DoubleToDoubleUnaryDD {
    public SqrDoubleToDouble(Expr arg) {
        super(arg);
    }

    @Override
    public double evalDoubleSimple(double x) {
        return Maths.sqr(x);
    }
}

class SqrDoubleToComplex extends Sqr implements DoubleToComplexDefaults.DoubleToComplexUnaryDC {
    public SqrDoubleToComplex(Expr arg) {
        super(arg);
    }

    @Override
    public Complex aggregateComplex(Complex a) {
        return a.sqr();
    }

}

class SqrDoubleToVector extends Sqr implements DoubleToVectorDefaults.DoubleToVectorUnaryDV {
    public SqrDoubleToVector(Expr arg) {
        super(arg);
    }

    @Override
    public ComplexVector aggregateVector(ComplexVector v) {
        return v.sqr();
    }
}

class SqrDoubleToMatrix extends Sqr implements DoubleToMatrixDefaults.DoubleToMatrixUnaryDM {
    public SqrDoubleToMatrix(Expr arg) {
        super(arg);
    }

    @Override
    public ComplexMatrix aggregateMatrix(ComplexMatrix m) {
        return m.sqr();
    }

}
