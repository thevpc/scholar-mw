package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 2/14/15.
 */
public interface ExprSequenceFactory {
    ExprList newSequence(Expr pattern, DoubleParam[] vars, int[] max);

    ExprList newSequence(Expr pattern, DoubleParam[] vars, double[][] values);

    ExprList newSequence(int size, ExprSeqCellIterator it);

    ExprList newPreloadedSequence(int size, ExprSeqCellIterator it);

    ExprList newCachedSequence(int size, ExprSeqCellIterator it);

    ExprList newUnmodifiableSequence(int size, ExprSeqCellIterator it);
}
