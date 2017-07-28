package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 2/14/15.
 */
public class DefaultExprSequenceFactory extends AbstractExprSequenceFactory{
    public static final ExprSequenceFactory INSTANCE=new DefaultExprSequenceFactory();
    @Override
    public ExprList newSequence(final int size, final ExprSeqCellIterator it) {
        return newPreloadedSequence(size,it);
    }

    @Override
    public ExprList newPreloadedSequence(final int size, final ExprSeqCellIterator it) {

        return new PreloadedSequence(size, it);
    }

    @Override
    public ExprList newUnmodifiableSequence(final int size, final ExprSeqCellIterator it) {
        return new UnmodifiableSequence(size, it);
    }

    @Override
    public ExprList newCachedSequence(final int size, final ExprSeqCellIterator it) {
        final Expr[] cache=new Expr[size];
        return new CachedSequence(size, cache, it);
    }

}
