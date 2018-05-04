package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.ComponentDimension;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 4/29/14.
 */
public class DoubleParam extends ParamExpr implements Cloneable{
    public DoubleParam(String name) {
        super(name);
    }

    @Override
    public int getComponentSize() {
        return 1;
    }

    @Override
    public boolean isDCImpl() {
        return true;
    }


    @Override
    public boolean isDDImpl() {
        return true;
    }


    //    @Override
//    public boolean isDDx() {
//        return true;
//    }


    @Override
    public boolean isDMImpl() {
        return true;
    }


    @Override
    public Expr setParam(String name, double value) {
        return setParam(name, DoubleValue.valueOf(value, Domain.FULLX));
    }


    @Override
    public Complex toComplex() {
        throw new IllegalArgumentException("Cannot process param " + getParamName() + " as Complex");
    }

    @Override
    public Matrix toMatrix() {
        throw new IllegalArgumentException("Cannot process param " + getParamName() + " as Matrix");
    }

    @Override
    public Expr setParam(String name, Expr value) {
        if (getParamName().equals(name)) {
            if (value.isDD()) {
                return value;
            }
            throw new IllegalArgumentException("Cannot process param " + getParamName() + " as " + value);
        }
        return this;
    }

//    @Override
//    public Domain getDomain() {
//        return Domain.FULLX;
//    }

    @Override
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }

    @Override
    public Expr getComponent(int row, int col) {
        if (row == 0 && col == 0) {
            return this;
        }
        return FunctionFactory.DZEROXY;
    }

    @Override
    public DoubleToComplex getComponent(Axis a) {
        if (a == Axis.X) {
            return this;
        }
        return FunctionFactory.CZEROXY;
    }

    @Override
    public Domain getDomainImpl() {
        return Domain.FULLX;
    }

    @Override
    public DoubleToDouble getRealDD() {
        return this;
    }

    @Override
    public String getComponentTitle(int row, int col) {
        return null;
    }


    @Override
    public DoubleToDouble getImagDD() {
        return FunctionFactory.DZEROXY;
    }


    @Override
    public boolean isScalarExprImpl() {
        return true;
    }

    @Override
    public boolean isDoubleExprImpl() {
        return true;
    }

    @Override
    public boolean isDVImpl() {
        return true;
    }

    @Override
    public DoubleToVector toDV() {
        return this;
    }

}
