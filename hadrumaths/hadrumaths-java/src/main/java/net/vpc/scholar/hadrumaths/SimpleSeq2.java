package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.DoubleParam;
import net.vpc.scholar.hadrumaths.symbolic.ExprSeqCellIterator;

import java.util.Arrays;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class SimpleSeq2 implements ExprSeqCellIterator {
    private final Expr pattern;
    private final DoubleParam m;
    private final DoubleParam n;
    private final double[][] values;

    public SimpleSeq2(double[][] values, DoubleParam m, DoubleParam n, Expr pattern) {
        this.values = values;
        this.m = m;
        this.n = n;
        this.pattern = pattern;
    }

    @Override
    public Expr get(int index) {
        double[] value = values[index];
        String mname = m.getName();
        String nname = n.getName();
        Expr e = pattern.setParam(mname, value[0]).setParam(nname, value[1]);
//                Map<String, Object> props = e.getProperties();
//                props.put(mname, value[0]);
//                props.put(nname, value[1]);
        return e;
    }

    @Override
    public String toString() {
        return "SimpleSeq2{" +
                "pattern=" + pattern +
                ", m=" + m +
                ", n=" + n +
                ", values=" + Arrays.deepToString(values) +
                '}';
    }
}
