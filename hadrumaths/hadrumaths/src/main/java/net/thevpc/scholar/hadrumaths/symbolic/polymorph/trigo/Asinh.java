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
@CanProduceClass({/*DoubleToDouble.class,*/DoubleToComplex.class,DoubleToVector.class,DoubleToMatrix.class})
public abstract class Asinh implements FunctionExpr {
    private static final long serialVersionUID = 1L;
    private final Expr arg;

    protected Asinh(Expr arg) {
        this.arg = arg;
    }

    @Override
    public String getName() {
        return "asinh";
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
        Asinh asinh = (Asinh) o;
        return Objects.equals(arg, asinh.arg);
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
        return Asinh.of(argument[0]);
    }

    @Override
    public Domain getDomain() {
        return getChild(0).getDomain();
    }

//    public static class AsinhDoubleToDouble extends Asinh implements DoubleToDouble {
//        public AsinhDoubleToDouble(Expr arg) {
//            super(arg);
//        }
//        @Override
//        public DoubleToDouble toDD() {
//            return this;
//        }
//    }

    public static Asinh of(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE://{return new AsinhDoubleToDouble(e);}
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                return new AsinhDoubleToComplex(e);
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                return new AsinhDoubleToVector(e);
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                return new AsinhDoubleToMatrix(e);
            }
            default: {
                throw new IllegalArgumentException("Unsupported type "+e.getType());
            }
        }
    }

}
class AsinhDoubleToComplex extends Asinh implements DoubleToComplexDefaults.DoubleToComplexUnaryDC {
    public AsinhDoubleToComplex(Expr arg) {
        super(arg);
    }

    @Override
    public Complex aggregateComplex(Complex a) {
        return a.asinh();
    }


}

class AsinhDoubleToVector extends Asinh implements DoubleToVectorDefaults.DoubleToVectorUnaryDV {
    public AsinhDoubleToVector(Expr arg) {
        super(arg);
    }

    @Override
    public ComplexVector aggregateVector(ComplexVector v) {
        return v.asinh();
    }
}

class AsinhDoubleToMatrix extends Asinh implements DoubleToMatrixDefaults.DoubleToMatrixUnaryDM {
    public AsinhDoubleToMatrix(Expr arg) {
        super(arg);
    }

    @Override
    public ComplexMatrix aggregateMatrix(ComplexMatrix m) {
        return m.asinh();
    }

}
