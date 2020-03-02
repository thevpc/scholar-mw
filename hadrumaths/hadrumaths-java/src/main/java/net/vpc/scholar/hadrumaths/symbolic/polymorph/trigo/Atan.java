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
public abstract class Atan implements FunctionExpr {
    private static final long serialVersionUID = 1L;
    private final Expr arg;

    protected Atan(Expr arg) {
        this.arg = arg;
    }

    @Override
    public String getName() {
        return "atan";
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode()*31+arg.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Atan atan = (Atan) o;
        return Objects.equals(arg, atan.arg);
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
        return Atan.of(argument[0]);
    }

    @Override
    public Domain getDomain() {
        return getChild(0).getDomain();
    }

    public static Atan of(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                return new AtanDoubleToDouble(e);
            }
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                return new AtanDoubleToComplex(e);
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                return new AtanDoubleToVector(e);
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                return new AtanDoubleToMatrix(e);
            }
            default: {
                throw new IllegalArgumentException("Unsupported type "+e.getType());
            }
        }
    }

}

class AtanDoubleToDouble extends Atan implements DoubleToDoubleDefaults.DoubleToDoubleUnaryDD {
    public AtanDoubleToDouble(Expr arg) {
        super(arg);
    }

      @Override
    public double evalDoubleSimple(double x) {
        return Maths.atan(x);
    }

}

class AtanDoubleToComplex extends Atan implements DoubleToComplexDefaults.DoubleToComplexUnaryDC {
    public AtanDoubleToComplex(Expr arg) {
        super(arg);
    }

    @Override
    public Complex aggregateComplex(Complex a) {
        return a.atan();
    }


}

class AtanDoubleToVector extends Atan implements DoubleToVectorDefaults.DoubleToVectorUnaryDV {
    public AtanDoubleToVector(Expr arg) {
        super(arg);
    }

    @Override
    public ComplexVector aggregateVector(ComplexVector v) {
        return v.atan();
    }
}

class AtanDoubleToMatrix extends Atan implements DoubleToMatrixDefaults.DoubleToMatrixUnaryDM {
    public AtanDoubleToMatrix(Expr arg) {
        super(arg);
    }

    @Override
    public ComplexMatrix aggregateMatrix(ComplexMatrix m) {
        return m.atan();
    }

}
