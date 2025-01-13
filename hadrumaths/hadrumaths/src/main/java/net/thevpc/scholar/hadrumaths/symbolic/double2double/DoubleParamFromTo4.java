package net.thevpc.scholar.hadrumaths.symbolic.double2double;

import net.thevpc.scholar.hadrumaths.Double3Filter;
import net.thevpc.scholar.hadrumaths.Double4Filter;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleParamValues;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;

public class DoubleParamFromTo4 implements DoubleParamValues {
    private final DoubleParamFromTo1 a;
    private final DoubleParamFromTo1 b;
    private final DoubleParamFromTo1 c;
    private final DoubleParamFromTo1 d;
    private final Double4Filter filter;

    public DoubleParamFromTo4(DoubleParamFromTo1 a, DoubleParamFromTo1 b, DoubleParamFromTo1 c, DoubleParamFromTo1 d, Double4Filter filter) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.filter = filter;
    }

    public DoubleParamFromTo4 where(Double4Filter filter) {
        return new DoubleParamFromTo4(a, b, c, d,filter);
    }

    public DoubleParamFromTo4 where(Expr filter) {
        return new DoubleParamFromTo4(a, b, c, d,filter == null ? null : new Double4Filter() {
            @Override
            public boolean accept(double aVal, double bVal, double cVal, double dVal) {
                Expr t = filter.setParam(a.getParam(), aVal).setParam(b.getParam(), bVal).setParam(c.getParam(), cVal).setParam(d.getParam(), dVal).simplify();
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

    public DoubleParam getParam4() {
        return d.getParam();
    }

    @Override
    public double[][] getParamValues() {
        return values();
    }

    public double[][] values() {
        return Maths.cross(a.values(), b.values(), c.values(), d.values(), filter);
    }
}
