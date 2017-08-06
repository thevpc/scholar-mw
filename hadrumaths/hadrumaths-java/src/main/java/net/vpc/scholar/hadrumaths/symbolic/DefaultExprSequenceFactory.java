package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.TList;
import net.vpc.scholar.hadrumaths.TVectorCell;

/**
 * Created by vpc on 2/14/15.
 */
public class DefaultExprSequenceFactory<T extends Expr> extends AbstractExprSequenceFactory<T>{
    public static final ExprSequenceFactory INSTANCE=new DefaultExprSequenceFactory();
    @Override
    public TList<T> newSequence(final int size, final TVectorCell<T> it) {
        return newPreloadedSequence(size,it);
    }

    @Override
    public TList<T> newPreloadedSequence(final int size, final TVectorCell<T> it) {

        return new PreloadedSequence(getComponentType(), size, it);
    }

    @Override
    public TList<T> newUnmodifiableSequence(final int size, final TVectorCell<T> it) {
        return new UnmodifiableSequence(getComponentType(),size, it);
    }

    @Override
    public TList<T> newCachedSequence(final int size, final TVectorCell<T> it) {
        final Expr[] cache=new Expr[size];
        return new CachedSequence(getComponentType(),size, cache, it);
    }

}
