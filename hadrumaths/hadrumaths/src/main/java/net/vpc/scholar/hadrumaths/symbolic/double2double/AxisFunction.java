package net.vpc.scholar.hadrumaths.symbolic.double2double;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.util.internal.IgnoreRandomGeneration;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDoubleDefaults;
import net.vpc.scholar.hadrumaths.symbolic.ExprDefaults;

import java.util.Collections;
import java.util.List;

/**
 * Created by vpc on 8/19/14.
 */
@IgnoreRandomGeneration
public abstract class AxisFunction implements DoubleToDoubleDefaults.DoubleToDoubleSimple {

    private static final long serialVersionUID = 1L;
    protected Domain domain;
    protected Axis axis;

    protected AxisFunction(Domain domain, Axis axis) {
        super();
        this.domain = domain;
        this.axis = axis;
    }

//    @Override
//    public boolean isDoubleTyped() {
//        return true;
//    }
//    protected Complex evalComplex(Complex c) {
//        return c;
//    }
//
//    protected double evalDouble(double c) {
//        return c;
//    }
//
//    @Override
//    public Expr[] getArguments() {
//        return ArrayUtils.EMPTY_EXPR_ARRAY;
//    }
//
//    @Override
//    public Expr getComponent(int row, int col) {
//        if (isScalarExpr() && (row != col || col != 0)) {
//            return FunctionFactory.DZEROXY;
//        }
//        return this;
//    }
//
//    @Override
//    public boolean isDDImpl() {
//        return true;
//    }
//
//    @Override
//    public boolean isDCImpl() {
//        return true;
//    }
//
//    @Override
//    public boolean isDMImpl() {
//        return true;
//    }
//
//    @Override
//    public Domain getDomainImpl() {
//        return domain;
//    }
//
//    @Override
//    public DoubleToDouble getRealDD() {
//        return this;
//    }
//
//    @Override
//    public DoubleToDouble getImagDD() {
//        return FunctionFactory.DZEROXY;
//    }
    @Override
    public int hashCode() {
        int result = getClass().getName().hashCode();
        result = 31 * result + (domain != null ? domain.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AxisFunction that = (AxisFunction) o;

        return domain != null ? domain.equals(that.domain) : that.domain == null;
    }

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }

    public Axis getAxis() {
        return axis;
    }

    @Override
    public boolean isInvariant(Axis axis) {
        return this.axis != axis;
    }

//    @Override
//    public IDDx toDDx() {
//        return this;
//    }
//    @Override
//    public DoubleToMatrix toDM() {
//        return new DC2DM(this);
//    }
    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public boolean isNaN() {
        return false;
    }

    @Override
    public boolean hasParams() {
        return false;
    }

//    @Override
//    public boolean isComplexImpl() {
//        return false;
//    }
//    @Override
//    public boolean isDoubleImpl() {
//        return false;
//    }
//    @Override
//    public boolean isMatrix() {
//        return false;
//    }
//    @Override
//    public boolean isDC() {
//        return true;
//    }
//    @Override
//    public boolean isDV() {
//        return false;
//    }
//    @Override
//    public DoubleToDouble getRealDD() {
//        return this;
//    }
//
//    @Override
//    public DoubleToDouble getImagDD() {
//        return Maths.DDZERO;
//    }
    @Override
    public Expr setParam(String name, Expr value) {
        return this;
    }

//    @Override
//    public boolean isDouble() {
//        return false;
//    }
//    @Override
//    public boolean isDoubleExpr() {
//        return false;
//    }
//
//    @Override
//    public boolean isComplex() {
//        return false;
//    }
//    @Override
//    public boolean isDM() {
//        return false;
//    }
    @Override
    public boolean isInfinite() {
        return false;
    }

    @Override
    public List<Expr> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

}
