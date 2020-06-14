package net.vpc.scholar.hadrumaths.symbolic.double2double;

import net.vpc.scholar.hadrumaths.Double3Filter;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.DoubleParamValues;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;

public class DoubleParamFromTo3 implements DoubleParamValues {
    private final DoubleParamFromTo1 a;
    private final DoubleParamFromTo1 b;
    private final DoubleParamFromTo1 c;
    private final Double3Filter filter;

    public DoubleParamFromTo3(DoubleParamFromTo1 a, DoubleParamFromTo1 b, DoubleParamFromTo1 c, Double3Filter filter) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.filter = filter;
    }

    public DoubleParamFromTo3 where(Double3Filter filter) {
        return new DoubleParamFromTo3(a, b, c, filter);
    }

    public DoubleParamFromTo3 where(Expr filter) {
        return new DoubleParamFromTo3(a, b, c, filter == null ? null : new Double3Filter() {
            @Override
            public boolean accept(double aVal, double bVal, double cVal) {
                Expr t = filter.setParam(a.getParam(), aVal).setParam(b.getParam(), bVal).setParam(c.getParam(), cVal).simplify();
                if (t.isNarrow(ExprType.DOUBLE_NBR)) {
                    return t.toDouble() != 0;
                }

                throw new RuntimeException("Unable to evaluate " + filter + " to a valid boolean");
            }
        });
    }

    @Override
    public DoubleParam[] getParams() {
        return new DoubleParam[]{getParam1(), getParam2(), getParam3()};
    }

    public DoubleParam getParam1() {
        return a.getParam();
    }

    public DoubleParam getParam2() {
        return b.getParam();
    }

    public DoubleParam getParam3() {
        return c.getParam();
    }

    @Override
    public double[][] getParamValues() {
        return values();
    }

    public double[][] values() {
        return Maths.cross(a.values(), b.values(), c.values(), filter);
    }
}
