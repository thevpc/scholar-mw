package net.vpc.scholar.hadrumaths.symbolic.polymorph.trigo;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.vpc.scholar.hadrumaths.util.internal.CanProduceClass;

/**
 * Created by vpc on 4/30/14.
 */
@CanProduceClass({DoubleToDouble.class,DoubleToComplex.class,DoubleToVector.class,DoubleToMatrix.class})
public abstract class Acos implements FunctionExpr {
    private static final long serialVersionUID = 1L;
    private final Expr arg;

    protected Acos(Expr arg) {
        this.arg = arg;
    }

    @Override
    public String getName() {
        return "acos";
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode()*31+arg.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Acos acos = (Acos) o;
        return Objects.equals(arg, acos.arg);
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
        return Acos.of(argument[0]);
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
    public Domain getDomain() {
        return getChild(0).getDomain();
    }

    public static Acos of(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                return new AcosDoubleToDouble(e);
            }
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                return new AcosDoubleToComplex(e);
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                return new AcosDoubleToVector(e);
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                return new AcosDoubleToMatrix(e);
            }
            default: {
                throw new IllegalArgumentException("Unsupported type "+e.getType());
            }
        }
    }

}

class AcosDoubleToDouble extends Acos implements DoubleToDoubleDefaults.DoubleToDoubleUnaryDD {
    public AcosDoubleToDouble(Expr arg) {
        super(arg);
    }

    @Override
    public double evalDoubleSimple(double x) {
        return Maths.acos(x);
    }
}

class AcosDoubleToComplex extends Acos implements DoubleToComplexDefaults.DoubleToComplexUnaryDC {
    public AcosDoubleToComplex(Expr arg) {
        super(arg);
    }

    @Override
    public Complex aggregateComplex(Complex a) {
        return a.acos();
    }

}

class AcosDoubleToVector extends Acos implements DoubleToVectorDefaults.DoubleToVectorUnaryDV {
    public AcosDoubleToVector(Expr arg) {
        super(arg);
    }

    @Override
    public ComplexVector aggregateVector(ComplexVector v) {
        return v.acos();
    }

}

class AcosDoubleToMatrix extends Acos implements DoubleToMatrixDefaults.DoubleToMatrixUnaryDM {
    public AcosDoubleToMatrix(Expr arg) {
        super(arg);
    }

    @Override
    public ComplexMatrix aggregateMatrix(ComplexMatrix m) {
        return m.acos();
    }
}
