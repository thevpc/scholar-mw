package net.vpc.scholar.hadrumaths.symbolic.double2double;

import net.vpc.scholar.hadrumaths.Double2Filter;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.DoubleParamValues;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;

public class DoubleParamFromTo2 implements DoubleParamValues {
    private final DoubleParamFromTo1 a;
    private final DoubleParamFromTo1 b;
    private final Double2Filter filter;

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
                if (t.isNarrow(ExprType.DOUBLE_NBR)) {
                    return t.toDouble() != 0;
                }
                throw new RuntimeException("Unable to evaluate " + filter + " to a valid boolean. Best Effort was : " + t);
            }
        });
    }

    public DoubleParamFromTo1 getB() {
        return b;
    }

    public DoubleParamFromTo3 and(DoubleParamFromTo1 m) {
        return cross(m);
    }

    public DoubleParamFromTo3 cross(DoubleParamFromTo1 m) {
        return new DoubleParamFromTo3(
                a, b, m, null
        );
    }

    @Override
    public DoubleParam[] getParams() {
        return new DoubleParam[]{getParam1(), getParam2()};
    }

    public DoubleParam getParam1() {
        return a.getParam();
    }

    public DoubleParam getParam2() {
        return b.getParam();
    }

    @Override
    public double[][] getParamValues() {
        return values();
    }

    public double[][] values() {
        return Maths.cross(a.values(), b.values(), filter);
    }
}
