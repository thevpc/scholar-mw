package net.vpc.scholar.hadrumaths.symbolic.polymorph.trigo;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.random.CanProduceClass;
import net.vpc.scholar.hadrumaths.symbolic.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by vpc on 4/30/14.
 */
@CanProduceClass({DoubleToDouble.class,DoubleToComplex.class,DoubleToVector.class,DoubleToMatrix.class})
public abstract class Log10 implements FunctionExpr {
    private static final long serialVersionUID = 1L;
    private final Expr arg;

    protected Log10(Expr arg) {
        this.arg = arg;
    }

    @Override
    public String getName() {
        return "log10";
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode()*31+arg.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Log10 log10 = (Log10) o;
        return Objects.equals(arg, log10.arg);
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
        return Log10.of(argument[0]);
    }

    @Override
    public Domain getDomain() {
        return getChild(0).getDomain();
    }

    public static Log10 of(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                return new Log10DoubleToDouble(e);
            }
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                return new Log10DoubleToComplex(e);
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                return new Log10DoubleToVector(e);
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                return new Log10DoubleToMatrix(e);
            }
            default: {
                throw new IllegalArgumentException("Unsupported type "+e.getType());
            }
        }
    }


}
class Log10DoubleToDouble extends Log10 implements DoubleToDoubleDefaults.DoubleToDoubleUnaryDD {
    public Log10DoubleToDouble(Expr arg) {
        super(arg);
    }

    @Override
    public double evalDoubleSimple(double x) {
        return Maths.log10(x);
    }
}
class Log10DoubleToComplex extends Log10 implements DoubleToComplexDefaults.DoubleToComplexUnaryDC {
    public Log10DoubleToComplex(Expr arg) {
        super(arg);
    }

    @Override
    public Complex aggregateComplex(Complex a) {
        return a.log10();
    }

}

class Log10DoubleToVector extends Log10 implements DoubleToVectorDefaults.DoubleToVectorUnaryDV {
    public Log10DoubleToVector(Expr arg) {
        super(arg);
    }

    @Override
    public ComplexVector aggregateVector(ComplexVector v) {
        return v.log10();
    }
}

class Log10DoubleToMatrix extends Log10 implements DoubleToMatrixDefaults.DoubleToMatrixUnaryDM {
    public Log10DoubleToMatrix(Expr arg) {
        super(arg);
    }

    @Override
    public ComplexMatrix aggregateMatrix(ComplexMatrix m) {
        return m.log10();
    }

}
