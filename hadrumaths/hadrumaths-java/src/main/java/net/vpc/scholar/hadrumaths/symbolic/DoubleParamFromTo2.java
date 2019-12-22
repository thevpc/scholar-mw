package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Double2Filter;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.MathsBase;

public class DoubleParamFromTo2 implements DoubleParamValues {
    private DoubleParamFromTo1 a;
    private DoubleParamFromTo1 b;
    private Double2Filter filter;

    public DoubleParamFromTo2(DoubleParamFromTo1 a, DoubleParamFromTo1 b, Double2Filter filter) {
        this.a = a;
        this.b = b;
        this.filter = filter;
    }

////    public DoubleParamFromTo2 to(double t) {
////        return new DoubleParamFromTo2(a, b.to(t), filter);
////    }
////
////    public DoubleParamFromTo2 times(int count) {
////        double step = (to - from) / (times - 1);
////        return new DoubleParamFromTo2(a, b.to(t), filter);
////    }
//
//    public DoubleParamFromTo2 by(double b) {
//        return new DoubleParamFromTo2(a, this.b.by(b), filter);
//    }

    public DoubleParamFromTo2 where(Double2Filter filter) {
        return new DoubleParamFromTo2(a, this.b, filter);
    }

    public DoubleParamFromTo2 where(Expr filter) {
        return new DoubleParamFromTo2(a, this.b, filter == null ? null : new Double2Filter() {
            @Override
            public boolean accept(double aVal, double bVal) {
                Expr t = filter.setParam(a.getParam(), aVal).setParam(b.getParam(), bVal).simplify();
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

    public DoubleParamFromTo1 getB() {
        return b;
    }

    public DoubleParamFromTo3 and(DoubleParamFromTo1 m) {
        return cross(m);
    }

    public DoubleParamFromTo3 cross(DoubleParamFromTo1 m) {
        return new DoubleParamFromTo3(
                a,b,m,null
        );
    }

    public double[][] values() {
        return MathsBase.cross(a.values(), b.values(), filter);
    }

    @Override
    public DoubleParam[] getParams() {
        return new DoubleParam[]{getParam1(), getParam2()};
    }

    @Override
    public double[][] getParamValues() {
        return values();
    }
}
