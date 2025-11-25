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
public abstract class Db implements FunctionExpr {
    private static final long serialVersionUID = 1L;
    private final Expr arg;

    protected Db(Expr arg) {
        this.arg = arg;
    }

    @Override
    public String getName() {
        return "db";
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode()*31+arg.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Db db = (Db) o;
        return Objects.equals(arg, db.arg);
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
    public Expr newInstance(Expr[] argument) {
        return Db.of(argument[0]);
    }

    @Override
    public Domain getDomain() {
        return getChild(0).getDomain();
    }

    public static Db of(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case DOUBLE_DOUBLE: {
                return new DbDoubleToDouble(e);
            }
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
            case DOUBLE_COMPLEX: {
                return new DbDoubleToComplex(e);
            }
            case CVECTOR_NBR:
            case CVECTOR_EXPR:
            case DOUBLE_CVECTOR: {
                return new DbDoubleToVector(e);
            }
            case CMATRIX_NBR:
            case CMATRIX_EXPR:
            case DOUBLE_CMATRIX: {
                return new DbDoubleToMatrix(e);
            }
            default: {
                throw new IllegalArgumentException("Unsupported");
            }
        }
    }

}

class DbDoubleToDouble extends Db implements DoubleToDoubleDefaults.DoubleToDoubleUnaryDD {
    public DbDoubleToDouble(Expr arg) {
        super(arg);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Div");
    }

    @Override
    public double evalDoubleSimple(double x) {
        return Maths.db(x);
    }

}

class DbDoubleToComplex extends Db implements DoubleToComplexDefaults.DoubleToComplexUnaryDC {
    public DbDoubleToComplex(Expr arg) {
        super(arg);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Div");
    }

    @Override
    public Complex aggregateComplex(Complex a) {
        return a.db();
    }
}

class DbDoubleToVector extends Db implements DoubleToVectorDefaults.DoubleToVectorUnaryDV {
    public DbDoubleToVector(Expr arg) {
        super(arg);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Div");
    }
    @Override
    public ComplexVector aggregateVector(ComplexVector v) {
        return v.db();
    }
}

class DbDoubleToMatrix extends Db implements DoubleToMatrixDefaults.DoubleToMatrixUnaryDM {
    public DbDoubleToMatrix(Expr arg) {
        super(arg);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedObject("Div");
    }
    @Override
    public ComplexMatrix aggregateMatrix(ComplexMatrix m) {
        return m.db();
    }

}
