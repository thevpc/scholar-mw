package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.conv.DC2DM;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by vpc on 8/19/14.
 */
public abstract class AxisFunction extends AbstractExpBase implements DoubleToDouble,DoubleToComplex{
    private static final long serialVersionUID = 1L;
    protected Domain domain;
    protected Axis axis;

    protected AxisFunction(Domain domain, Axis axis) {
        super();
        this.domain = domain;
        this.axis = axis;
    }


    @Override
    public boolean isDoubleTyped() {
        return true;
    }

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
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }

    @Override
    public Expr setParam(String name, Expr value) {
        return this;
    }

    @Override
    public boolean isInfinite() {
        return false;
    }

    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public boolean isNaN() {
        return false;
    }

    @Override
    public DoubleToComplex toDC() {
        return this;
    }

    @Override
    public DoubleToDouble toDD() {
        return this;
    }

//    @Override
//    public IDDx toDDx() {
//        return this;
//    }

    @Override
    public DoubleToMatrix toDM() {
        return new DC2DM(this);
    }


    @Override
    public boolean isScalarExpr() {
        return true;
    }

//    @Override
//    public boolean isDoubleExprImpl() {
//        return false;
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AxisFunction that = (AxisFunction) o;

        return domain != null ? domain.equals(that.domain) : that.domain == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (domain != null ? domain.hashCode() : 0);
        return result;
    }

    public Axis getAxis(){
        return axis;
    }

//    @Override
//    public boolean isComplexImpl() {
//        return false;
//    }

//    @Override
//    public boolean isDoubleImpl() {
//        return false;
//    }

    @Override
    public boolean isMatrix() {
        return false;
    }

    @Override
    public boolean isDC() {
        return true;
    }

    @Override
    public boolean isDD() {
        return true;
    }

    @Override
    public boolean isDV() {
        return false;
    }

    @Override
    public DoubleToDouble getRealDD() {
        return this;
    }

    @Override
    public DoubleToDouble getImagDD() {
        return Maths.DDZERO;
    }

    @Override
    public boolean isInvariant(Axis axis) {
        return false;
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public boolean isDoubleExpr() {
        return false;
    }

    @Override
    public boolean isComplex() {
        return false;
    }

    @Override
    public boolean isDM() {
        return false;
    }

    @Override
    public boolean hasParams() {
        return false;
    }

    @Override
    public List<Expr> getSubExpressions() {
        return Collections.emptyList();
    }

    @Override
    public Expr composeX(Expr xreplacement) {
        return xreplacement;
    }

    @Override
    public Expr composeY(Expr yreplacement) {
        return yreplacement;
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

}
