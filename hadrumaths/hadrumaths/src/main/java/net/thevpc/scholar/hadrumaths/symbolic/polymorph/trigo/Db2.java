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
public abstract class Db2 implements FunctionExpr {
    private static final long serialVersionUID = 1L;
    private final Expr arg;

    protected Db2(Expr arg) {
        this.arg = arg;
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode()*31+arg.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Db2 db2 = (Db2) o;
        return Objects.equals(arg, db2.arg);
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
    public String getName() {
        return "db2";
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
        return Db2.of(argument[0]);
    }

    @Override
    public Domain getDomain() {
        return getChild(0).getDomain();
    }

    public static Db2 of(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                return new Db2DoubleToDouble(e);
            }
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                return new Db2DoubleToComplex(e);
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                return new Db2DoubleToVector(e);
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                return new Db2DoubleToMatrix(e);
            }
            default: {
                throw new IllegalArgumentException("Unsupported type "+e.getType());
            }
        }
    }


}
class Db2DoubleToDouble extends Db2 implements DoubleToDoubleDefaults.DoubleToDoubleUnaryDD {
    public Db2DoubleToDouble(Expr arg) {
        super(arg);
    }

    @Override
    public double evalDoubleSimple(double x) {
        return Maths.db2(x);
    }

}

class Db2DoubleToComplex extends Db2 implements DoubleToComplexDefaults.DoubleToComplexUnaryDC {
    public Db2DoubleToComplex(Expr arg) {
        super(arg);
    }

    @Override
    public Complex aggregateComplex(Complex a) {
        return a.db2();
    }


}

class Db2DoubleToVector extends Db2 implements DoubleToVectorDefaults.DoubleToVectorUnaryDV {
    public Db2DoubleToVector(Expr arg) {
        super(arg);
    }

    @Override
    public ComplexVector aggregateVector(ComplexVector v) {
        return v.db2();
    }
}

class Db2DoubleToMatrix extends Db2 implements DoubleToMatrixDefaults.DoubleToMatrixUnaryDM {
    public Db2DoubleToMatrix(Expr arg) {
        super(arg);
    }

    @Override
    public ComplexMatrix aggregateMatrix(ComplexMatrix m) {
        return m.db2();
    }
}
