package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.DoubleParam;
import net.vpc.scholar.hadrumaths.symbolic.ExprCellIterator;

import java.util.Arrays;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class SimpleSeq2b implements TMatrixCell<Expr> {
    private final Expr pattern;
    private final DoubleParam m;
    private final DoubleParam n;
    private final double[] mvalues;
    private final double[] nvalues;

    public SimpleSeq2b(Expr pattern, DoubleParam m, double[] mvalues, DoubleParam n, double[] nvalues) {
        this.pattern = pattern;
        this.m = m;
        this.mvalues = mvalues;
        this.n = n;
        this.nvalues = nvalues;
    }

    @Override
    public Expr get(int row, int col) {
        return pattern.setParam(m, mvalues[row]).setParam(n, nvalues[col]);
    }

    @Override
    public String toString() {
        return "SimpleSeq2b{" +
                "pattern=" + pattern +
                ", m=" + m +
                ", n=" + n +
                ", mvalues=" + Arrays.toString(mvalues) +
                ", nvalues=" + Arrays.toString(nvalues) +
                '}';
    }
}
