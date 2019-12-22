package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

/**
 * Created by vpc on 2/14/15.
 */
public class DefaultExprSequenceFactory<T extends Expr> extends AbstractExprSequenceFactory<T> {
    public static final ExprSequenceFactory INSTANCE = new DefaultExprSequenceFactory();

    @Override
    public TVector<T> newSequence(final int size, final TVectorCell<T> it) {
        return newPreloadedSequence(size, it);
    }

    @Override
    public TVector<T> newPreloadedSequence(final int size, final TVectorCell<T> it) {

        return new PreloadedList(getComponentType(), false, size, it);
    }

    @Override
    public TVector<T> newUnmodifiableSequence(final int size, final TVectorCell<T> it) {
        return new UnmodifiableList(getComponentType(), false, size, it);
    }

    @Override
    public TVector<T> newCachedSequence(final int size, final TVectorCell<T> it) {
        final Expr[] cache = new Expr[size];
        return new CachedList(getComponentType(), false, size, cache, it);
    }

}
