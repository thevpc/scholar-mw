package net.thevpc.scholar.hadrumaths.symbolic.polymorph.trigo;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.util.internal.CanProduceClass;
import net.thevpc.scholar.hadrumaths.symbolic.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Principal inverse hyperbolic tangent, atanh(z) = 1/2 log((1+z)/(1-z)).
 * Follows the C99 branch cuts (see Complex.atanh()).
 *
 * @author vpc
 */
@CanProduceClass({/*DoubleToDouble.class,*/DoubleToComplex.class,DoubleToVector.class,DoubleToMatrix.class})
public abstract class Atanh implements FunctionExpr {
    private static final long serialVersionUID = 1L;
    private final Expr arg;

    protected Atanh(Expr arg) {
        this.arg = arg;
    }

    @Override
    public String getName() {
        return "atanh";
    }

    @Override
    public String toLatex() {
        StringBuilder sb = new StringBuilder();
        sb.append("\\text{").append(getName()).append("}\\left(");
        sb.append(arg.toLatex());
        sb.append("\\right)");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode() * 31 + arg.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Atanh atanh = (Atanh) o;
        return Objects.equals(arg, atanh.arg);
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
        return Atanh.of(argument[0]);
    }

    @Override
    public Domain getDomain() {
        return getChild(0).getDomain();
    }

    /**
     * atanh is a complex function (real only for |x| &lt; 1), so a real argument still maps
     * to the complex implementation, exactly like Asinh/Acosh/Sqrt.
     */
    public static Atanh of(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE:
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                return new AtanhDoubleToComplex(e);
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                return new AtanhDoubleToVector(e);
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                return new AtanhDoubleToMatrix(e);
            }
            default: {
                throw new IllegalArgumentException("Unsupported type " + e.getType());
            }
        }
    }

}

class AtanhDoubleToComplex extends Atanh implements DoubleToComplexDefaults.DoubleToComplexUnaryDC {
    public AtanhDoubleToComplex(Expr arg) {
        super(arg);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Atanh");
    }

    @Override
    public Complex aggregateComplex(Complex a) {
        return a.atanh();
    }
}

class AtanhDoubleToVector extends Atanh implements DoubleToVectorDefaults.DoubleToVectorUnaryDV {
    public AtanhDoubleToVector(Expr arg) {
        super(arg);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Atanh");
    }

    @Override
    public ComplexVector aggregateVector(ComplexVector v) {
        return v.atanh();
    }
}

class AtanhDoubleToMatrix extends Atanh implements DoubleToMatrixDefaults.DoubleToMatrixUnaryDM {
    public AtanhDoubleToMatrix(Expr arg) {
        super(arg);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Atanh");
    }

    @Override
    public ComplexMatrix aggregateMatrix(ComplexMatrix m) {
        return m.atanh();
    }
}
