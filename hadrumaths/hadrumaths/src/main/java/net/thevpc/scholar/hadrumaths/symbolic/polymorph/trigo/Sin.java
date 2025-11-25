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
public abstract class Sin implements FunctionExpr {
    private static final long serialVersionUID = 1L;
    private final Expr arg;

    protected Sin(Expr arg) {
        this.arg = arg;
    }

    @Override
    public String getName() {
        return "sin";
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode()*31+arg.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sin sin = (Sin) o;
        return Objects.equals(arg, sin.arg);
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
        return Sin.of(argument[0]);
    }

    public static Sin of(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                return new SinDoubleToDouble(e);
            }
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                return new SinDoubleToComplex(e);
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                return new SinDoubleToVector(e);
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                return new SinDoubleToMatrix(e);
            }
            default: {
                throw new IllegalArgumentException("Unsupported type "+e.getType());
            }
        }
    }


}

class SinDoubleToDouble extends Sin implements DoubleToDoubleDefaults.DoubleToDoubleUnaryDD {
    public SinDoubleToDouble(Expr arg) {
        super(arg);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Sin");
    }

    @Override
    public double evalDoubleSimple(double x) {
        return Maths.sin(x);
    }
}

class SinDoubleToComplex extends Sin implements DoubleToComplexDefaults.DoubleToComplexUnaryDC {
    public SinDoubleToComplex(Expr arg) {
        super(arg);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Sin");
    }

    @Override
    public Complex aggregateComplex(Complex a) {
        return a.sin();
    }

}

class SinDoubleToVector extends Sin implements DoubleToVectorDefaults.DoubleToVectorUnaryDV {
    public SinDoubleToVector(Expr arg) {
        super(arg);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Sin");
    }

    @Override
    public ComplexVector aggregateVector(ComplexVector v) {
        return v.sin();
    }

}

class SinDoubleToMatrix extends Sin implements DoubleToMatrixDefaults.DoubleToMatrixUnaryDM {
    public SinDoubleToMatrix(Expr arg) {
        super(arg);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Sin");
    }

    @Override
    public ComplexMatrix aggregateMatrix(ComplexMatrix m) {
        return m.sin();
    }

}
