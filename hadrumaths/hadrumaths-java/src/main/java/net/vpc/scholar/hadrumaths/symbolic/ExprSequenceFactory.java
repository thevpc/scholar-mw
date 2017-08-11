package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.TList;
import net.vpc.scholar.hadrumaths.TVectorCell;
import net.vpc.scholar.hadrumaths.TypeReference;

/**
 * Created by vpc on 2/14/15.
 */
public interface ExprSequenceFactory<T extends Expr> {
    TypeReference<T> getComponentType();

    TList<T> newSequence(T pattern, DoubleParam[] vars, int[] max);

    TList<T> newSequence(T pattern, DoubleParam[] vars, double[][] values);

    TList<T> newSequence(int size, TVectorCell<T> it);

    TList<T> newPreloadedSequence(int size, TVectorCell<T> it);

    TList<T> newCachedSequence(int size, TVectorCell<T> it);

    TList<T> newUnmodifiableSequence(int size, TVectorCell<T> it);
}
