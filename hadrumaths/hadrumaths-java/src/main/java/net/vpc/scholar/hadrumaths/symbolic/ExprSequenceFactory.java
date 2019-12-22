package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.common.util.TypeName;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.TVector;
import net.vpc.scholar.hadrumaths.TVectorCell;

/**
 * Created by vpc on 2/14/15.
 */
public interface ExprSequenceFactory<T extends Expr> {
    TypeName<T> getComponentType();

    TVector<T> newSequence(T pattern, DoubleParam[] vars, int[] max);

    TVector<T> newSequence(T pattern, DoubleParam[] vars, double[][] values);

    TVector<T> newSequence(int size, TVectorCell<T> it);

    TVector<T> newPreloadedSequence(int size, TVectorCell<T> it);

    TVector<T> newCachedSequence(int size, TVectorCell<T> it);

    TVector<T> newUnmodifiableSequence(int size, TVectorCell<T> it);
}
