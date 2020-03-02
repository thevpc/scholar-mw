package net.vpc.scholar.hadrumaths.symbolic.polymorph.num;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.random.CanProduceClass;
import net.vpc.scholar.hadrumaths.symbolic.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by vpc on 4/30/14.
 */
@CanProduceClass({DoubleToDouble.class, DoubleToComplex.class, DoubleToVector.class, DoubleToMatrix.class})
public abstract class Abs implements FunctionExpr {
    private static final long serialVersionUID = 1L;

    private final Expr arg;

    protected Abs(Expr arg) {
        this.arg = arg;
    }

    @Override
    public String getName() {
        return "abs";
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode()*31+arg.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Abs abs = (Abs) o;
        return Objects.equals(arg, abs.arg);
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
        return Abs.of(argument[0]);
    }

    public static Abs of(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                return new AbsDoubleToDouble(e);
            }
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                return new AbsDoubleToComplex(e);
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                return new AbsDoubleToVector(e);
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                return new AbsDoubleToMatrix(e);
            }
            default: {
                throw new IllegalArgumentException("Unsupported");
            }
        }
    }


}

class AbsDoubleToDouble extends Abs implements DoubleToDoubleDefaults.DoubleToDoubleUnaryDD {
    public AbsDoubleToDouble(Expr arg) {
        super(arg);
    }

    @Override
    public double evalDoubleSimple(double x) {
        return Maths.abs(x);
    }

}

class AbsDoubleToComplex extends Abs implements DoubleToComplexDefaults.DoubleToComplexUnaryDC {
    public AbsDoubleToComplex(Expr arg) {
        super(arg);
    }

    @Override
    public Complex aggregateComplex(Complex a) {
        return a.abs();
    }

}

class AbsDoubleToVector extends Abs implements DoubleToVectorDefaults.DoubleToVectorUnaryDV {
    public AbsDoubleToVector(Expr arg) {
        super(arg);
    }

    @Override
    public ComplexVector aggregateVector(ComplexVector v) {
        return v.abs();
    }

}

class AbsDoubleToMatrix extends Abs implements DoubleToMatrixDefaults.DoubleToMatrixUnaryDM {
    public AbsDoubleToMatrix(Expr arg) {
        super(arg);
    }

    @Override
    public ComplexMatrix aggregateMatrix(ComplexMatrix m) {
        return m.abs();
    }

}
