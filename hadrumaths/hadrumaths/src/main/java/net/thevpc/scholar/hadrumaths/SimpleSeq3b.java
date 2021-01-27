package net.thevpc.scholar.hadrumaths;

import net.thevpc.scholar.hadrumaths.symbolic.ExprCubeCellIterator;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.DoubleParam;

import java.util.Arrays;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class SimpleSeq3b implements ExprCubeCellIterator {
    private final Expr pattern;
    private final DoubleParam m;
    private final DoubleParam n;
    private final DoubleParam p;
    private final double[] mvalues;
    private final double[] nvalues;
    private final double[] pvalues;

    public SimpleSeq3b(Expr pattern, DoubleParam m, double[] mvalues, DoubleParam n, double[] nvalues, DoubleParam p, double[] pvalues) {
        this.pattern = pattern;
        this.m = m;
        this.mvalues = mvalues;
        this.n = n;
        this.nvalues = nvalues;
        this.p = p;
        this.pvalues = pvalues;
    }

    @Override
    public Expr get(int row, int col, int height) {
        return pattern.setParam(m, mvalues[row]).setParam(n, nvalues[col]).setParam(p, pvalues[height]);
    }

    @Override
    public String toString() {
        return "SimpleSeq3b{" +
                "pattern=" + pattern +
                ", m=" + m +
                ", n=" + n +
                ", p=" + p +
                ", mvalues=" + Arrays.toString(mvalues) +
                ", nvalues=" + Arrays.toString(nvalues) +
                ", pvalues=" + Arrays.toString(pvalues) +
                '}';
    }
}
