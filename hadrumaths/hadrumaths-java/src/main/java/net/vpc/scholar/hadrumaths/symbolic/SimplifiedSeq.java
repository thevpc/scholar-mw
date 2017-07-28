package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Expr;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class SimplifiedSeq implements ExprSeqCellIterator {
    private AbstractExprList sequence;

    public SimplifiedSeq(AbstractExprList sequence) {
        this.sequence = sequence;
    }

    @Override
    public Expr get(int row) {
        return sequence.get(row).simplify();
    }

    @Override
    public String toString() {
        return "SimplifiedSeq{" +
                "seq=" + sequence +
                '}';
    }
}
