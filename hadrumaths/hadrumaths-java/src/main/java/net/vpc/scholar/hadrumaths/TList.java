package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.TParam;

import java.util.Collection;

/**
 * Created by vpc on 5/30/14.
 */
public interface TList<T> extends Iterable<T>, TVector<T> {


    int length();

    TList<T> eval(ElementOp<T> op);

    <R> TList<R> transform(Class<R> toType, TTransform<T, R> op);

    void appendAll(TVector<T> e);

    void append(T e);

    void appendAll(Collection<? extends T> e);

    TList<T> copy();

    TList<T> setParam(TParam param, Object value);

    TList<T> setParam(String name, Object value);
    TList<T> transpose();

}
