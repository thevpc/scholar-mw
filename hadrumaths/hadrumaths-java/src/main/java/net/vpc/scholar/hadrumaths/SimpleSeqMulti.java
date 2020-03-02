package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.double2double.DoubleParam;

import java.util.Arrays;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class SimpleSeqMulti implements VectorModel<Expr> {
    private final Expr pattern;
    private final DoubleParam[] m;
    private final double[][] values;

    public SimpleSeqMulti(Expr pattern, DoubleParam[] m, double[][] values) {
        this.values = values;
        this.m = m;
        this.pattern = pattern;
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public Expr get(int index) {
        double[] value = values[index];
        String[] mnames = new String[m.length];
        for (int i = 0; i < mnames.length; i++) {
            mnames[i] = m[i].getName();
        }
        Expr e = pattern;
        for (int i = 0; i < mnames.length; i++) {
            e = e.setParam(m[i].getName(), value[i]);
            e = e.setProperty(m[i].getName(), value[i]);
        }
//                Map<String, Object> props = e.getProperties();
//                for (int i = 0; i < mnames.length; i++) {
//                    props.put(m[i].getName(), value[i]);
//                }
        return e;
    }

    @Override
    public String toString() {
        return "SimpleSeqMulti{" +
                "pattern=" + pattern +
                ", m=" + Arrays.toString(m) +
                ", values=" + Arrays.deepToString(values) +
                '}';
    }
}
