package net.thevpc.scholar.hadrumaths.symbolic.polymorph.trigo;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.util.internal.CanProduceClass;
import net.thevpc.scholar.hadrumaths.symbolic.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by vpc on 4/30/14.
 */
@CanProduceClass({DoubleToDouble.class,DoubleToComplex.class,DoubleToVector.class,DoubleToMatrix.class})
public abstract class Conj implements FunctionExpr {
    private static final long serialVersionUID = 1L;
    private final Expr arg;

    protected Conj(Expr arg) {
        this.arg = arg;
    }

    @Override
    public String getName() {
        return "conj";
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
    public int hashCode() {
        return getClass().getName().hashCode()*31+arg.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conj conj = (Conj) o;
        return Objects.equals(arg, conj.arg);
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
        return Conj.of(argument[0]);
    }

    @Override
    public Domain getDomain() {
        return getChild(0).getDomain();
    }

    public static Conj of(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                return new ConjDoubleToDouble(e);
            }
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                return new ConjDoubleToComplex(e);
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                return new ConjDoubleToVector(e);
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                return new ConjDoubleToMatrix(e);
            }
            default: {
                throw new IllegalArgumentException("Unsupported type "+e.getType());
            }
        }
    }


}
class ConjDoubleToDouble extends Conj implements DoubleToDoubleDefaults.DoubleToDoubleUnaryDD {
    public ConjDoubleToDouble(Expr arg) {
        super(arg);
    }

    @Override
    public double evalDoubleSimple(double x) {
        return Maths.conj(x);
    }
}

class ConjDoubleToComplex extends Conj implements DoubleToComplexDefaults.DoubleToComplexUnaryDC {
    public ConjDoubleToComplex(Expr arg) {
        super(arg);
    }

    @Override
    public Complex aggregateComplex(Complex a) {
        return a.conj();
    }


}

class ConjDoubleToVector extends Conj implements DoubleToVectorDefaults.DoubleToVectorUnaryDV {
    public ConjDoubleToVector(Expr arg) {
        super(arg);
    }

    @Override
    public ComplexVector aggregateVector(ComplexVector v) {
        return v.conj();
    }

}

class ConjDoubleToMatrix extends Conj implements DoubleToMatrixDefaults.DoubleToMatrixUnaryDM {
    public ConjDoubleToMatrix(Expr arg) {
        super(arg);
    }

    @Override
    public ComplexMatrix aggregateMatrix(ComplexMatrix m) {
        return m.conj();
    }
}
