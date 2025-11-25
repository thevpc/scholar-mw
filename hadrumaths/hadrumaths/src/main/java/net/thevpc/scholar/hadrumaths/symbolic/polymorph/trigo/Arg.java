package net.thevpc.scholar.hadrumaths.symbolic.polymorph.trigo;

import net.thevpc.nuts.elem.NElement;
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
public abstract class Arg implements FunctionExpr {
    private static final long serialVersionUID = 1L;
    private final Expr arg;

    protected Arg(Expr arg) {
        this.arg = arg;
    }

    @Override
    public String getName() {
        return "arg";
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode()*31+arg.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arg arg1 = (Arg) o;
        return Objects.equals(arg, arg1.arg);
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
        return Arg.of(argument[0]);
    }

    public static Arg of(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                return new ArgDoubleToDouble(e);
            }
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                return new ArgDoubleToComplex(e);
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                return new ArgDoubleToVector(e);
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                return new ArgDoubleToMatrix(e);
            }
            default: {
                throw new IllegalArgumentException("Unsupported type "+e.getType());
            }
        }
    }


}
class ArgDoubleToDouble extends Arg implements DoubleToDoubleDefaults.DoubleToDoubleUnaryDD {
    public ArgDoubleToDouble(Expr arg) {
        super(arg);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Arg");
    }

    @Override
    public double evalDoubleSimple(double x) {
        return Maths.arg(x);
    }
}

class ArgDoubleToComplex extends Arg implements DoubleToComplexDefaults.DoubleToComplexUnaryDC {
    public ArgDoubleToComplex(Expr arg) {
        super(arg);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Arg");
    }

    @Override
    public Complex aggregateComplex(Complex a) {
        return a.arg();
    }
}

class ArgDoubleToVector extends Arg implements DoubleToVectorDefaults.DoubleToVectorUnaryDV {
    public ArgDoubleToVector(Expr arg) {
        super(arg);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Arg");
    }

    @Override
    public ComplexVector aggregateVector(ComplexVector v) {
        return v.arg();
    }
}

class ArgDoubleToMatrix extends Arg implements DoubleToMatrixDefaults.DoubleToMatrixUnaryDM {
    public ArgDoubleToMatrix(Expr arg) {
        super(arg);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Arg");
    }

    @Override
    public ComplexMatrix aggregateMatrix(ComplexMatrix m) {
        return m.arg();
    }

}
