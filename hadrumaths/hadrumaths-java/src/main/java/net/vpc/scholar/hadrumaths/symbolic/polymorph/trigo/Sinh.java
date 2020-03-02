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
public abstract class Sinh implements FunctionExpr {
    private static final long serialVersionUID = 1L;
    private final Expr arg;

    protected Sinh(Expr arg) {
        this.arg = arg;
    }

    @Override
    public String getName() {
        return "sinh";
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode()*31+arg.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sinh sinh = (Sinh) o;
        return Objects.equals(arg, sinh.arg);
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
        return Sinh.of(argument[0]);
    }

    public static Sinh of(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                return new SinhDoubleToDouble(e);
            }
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                return new SinhDoubleToComplex(e);
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                return new SinhDoubleToVector(e);
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                return new SinhDoubleToMatrix(e);
            }
            default: {
                throw new IllegalArgumentException("Unsupported type "+e.getType());
            }
        }
    }


}

class SinhDoubleToDouble extends Sinh implements DoubleToDoubleDefaults.DoubleToDoubleUnaryDD {
    public SinhDoubleToDouble(Expr arg) {
        super(arg);
    }

    @Override
    public double evalDoubleSimple(double x) {
        return Maths.sinh(x);
    }

}

class SinhDoubleToComplex extends Sinh implements DoubleToComplexDefaults.DoubleToComplexUnaryDC {
    public SinhDoubleToComplex(Expr arg) {
        super(arg);
    }

    @Override
    public Complex aggregateComplex(Complex a) {
        return a.sinh();
    }

}

class SinhDoubleToVector extends Sinh implements DoubleToVectorDefaults.DoubleToVectorUnaryDV {
    public SinhDoubleToVector(Expr arg) {
        super(arg);
    }

    @Override
    public ComplexVector aggregateVector(ComplexVector v) {
        return v.sinh();
    }

}

class SinhDoubleToMatrix extends Sinh implements DoubleToMatrixDefaults.DoubleToMatrixUnaryDM {
    public SinhDoubleToMatrix(Expr arg) {
        super(arg);
    }

    @Override
    public ComplexMatrix aggregateMatrix(ComplexMatrix m) {
        return m.sinh();
    }

}
