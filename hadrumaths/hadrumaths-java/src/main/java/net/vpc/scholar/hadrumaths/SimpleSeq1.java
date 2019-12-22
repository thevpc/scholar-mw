package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.DoubleParam;

import java.util.Arrays;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class SimpleSeq1 implements TVectorCell<Expr> {
    private final Expr pattern;
    private final DoubleParam m;
    private final double[] values;

    public SimpleSeq1(double[] values, DoubleParam m, Expr pattern) {
        this.values = values;
        this.m = m;
        this.pattern = pattern;
    }

    public Expr get(int index) {
        double value = values[index];
        String mname = m.getParamName();
        Expr e = pattern.setParam(mname, value);
//                e.getProperties().put(mname, value);
        return e;
    }

    @Override
    public String toString() {
        return "SimpleSeq1{" +
                "pattern=" + pattern +
                ", m=" + m +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
