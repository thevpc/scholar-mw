package net.thevpc.scholar.hadrumaths.symbolic.polymorph.num;

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
@CanProduceClass({/*DoubleToDouble.class,*/DoubleToComplex.class, DoubleToVector.class, DoubleToMatrix.class})
public abstract class Sqrt implements FunctionExpr {
    private static final long serialVersionUID = 1L;
    private final Expr arg;

    protected Sqrt(Expr arg) {
        this.arg = arg;
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode() * 31 + arg.hashCode();
    }

    @Override
    public String toLatex() {
        StringBuilder sb = new StringBuilder();
        sb.append("\\sqrt{").append(arg.toLatex()).append("}");
        return sb.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sqrt sqrt = (Sqrt) o;
        return Objects.equals(arg, sqrt.arg);
    }

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }

    @Override
    public String getName() {
        return "sqrt";
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
        return Sqrt.of(argument[0]);
    }

    @Override
    public Domain getDomain() {
        return getChild(0).getDomain();
    }

//    public static class SqrtDoubleToDouble extends Sqrt implements DoubleToDouble {
//        public SqrtDoubleToDouble(Expr arg) {
//            super(arg);
//        }
//
//        @Override
//        public DoubleToDouble toDD() {
//            return this;
//        }
//    }

    public static Sqrt of(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: //sqrt is a complex function { return new SqrtDoubleToDouble(e);}
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                return new SqrtDoubleToComplex(e);
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                return new SqrtDoubleToVector(e);
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                return new SqrtDoubleToMatrix(e);
            }
            default: {
                throw new IllegalArgumentException("Unsupported type " + e.getType());
            }
        }
    }

}

class SqrtDoubleToComplex extends Sqrt implements DoubleToComplexDefaults.DoubleToComplexUnaryDC {
    public SqrtDoubleToComplex(Expr arg) {
        super(arg);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Sqrt");
    }

    @Override
    public Complex aggregateComplex(Complex a) {
        return a.sqrt();
    }
}

class SqrtDoubleToVector extends Sqrt implements DoubleToVectorDefaults.DoubleToVectorUnaryDV {
    public SqrtDoubleToVector(Expr arg) {
        super(arg);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Sqrt");
    }

    @Override
    public ComplexVector aggregateVector(ComplexVector v) {
        return v.sqrt();
    }

}

class SqrtDoubleToMatrix extends Sqrt implements DoubleToMatrixDefaults.DoubleToMatrixUnaryDM {
    public SqrtDoubleToMatrix(Expr arg) {
        super(arg);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Sqrt");
    }

    @Override
    public ComplexMatrix aggregateMatrix(ComplexMatrix m) {
        return m.sqrt();
    }


}
