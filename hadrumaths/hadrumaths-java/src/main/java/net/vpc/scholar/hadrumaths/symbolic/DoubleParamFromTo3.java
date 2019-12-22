package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

public class DoubleParamFromTo3 implements DoubleParamValues {
    private DoubleParamFromTo1 a;
    private DoubleParamFromTo1 b;
    private DoubleParamFromTo1 c;
    private Double3Filter filter;

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
                if (t instanceof Complex) {
                    if (t.isDouble()) {
                        return t.toDouble() != 0;
                    }
                }
                throw new RuntimeException("Unable to evaluate " + filter + " to a valid boolean");
            }
        });
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

    public double[][] values() {
        return MathsBase.cross(a.values(), b.values(), c.values(), filter);
    }

    @Override
    public DoubleParam[] getParams() {
        return new DoubleParam[]{getParam1(),getParam2(),getParam3()};
    }

    @Override
    public double[][] getParamValues() {
        return values();
    }
}
