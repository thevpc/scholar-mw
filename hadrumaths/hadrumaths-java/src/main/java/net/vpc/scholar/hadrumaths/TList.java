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

    @Override
    TList<T> scalarProduct(boolean hermitian, T other);

    @Override
    TList<T> scalarProduct(T other);

    @Override
    TList<T> hscalarProduct(T other);

    @Override
    TVector<T> rscalarProduct(boolean hermitian, T other);

    @Override
    <R> TList<R> to(Class<R> other);

    @Override
    TList<T> vscalarProduct(TVector<T>... other);

    @Override
    TList<T> vhscalarProduct(TVector<T>... other);

    @Override
    TList<T> vscalarProduct(boolean hermitian, TVector<T>... other);

    @Override
    TList<T> dotmul(TVector<T> other);

    @Override
    TList<T> dotdiv(TVector<T> other);

    @Override
    TList<T> dotpow(TVector<T> other);

    @Override
    TList<T> add(TVector<T> other);

    @Override
    TList<T> sub(TVector<T> other);

    @Override
    TList<T> add(T other);

    @Override
    TList<T> sub(T other);

    @Override
    TList<T> mul(T other);

    @Override
    TList<T> div(T other);

    @Override
    TList<T> dotpow(T other);

    @Override
    TList<T> conj();

    @Override
    TList<T> cos();

    @Override
    TList<T> cosh();

    @Override
    TList<T> sin();

    @Override
    TList<T> sinh();

    @Override
    TList<T> tan();

    @Override
    TList<T> tanh();

    @Override
    TList<T> cotan();

    @Override
    TList<T> cotanh();

    @Override
    TList<T> getReal();

    @Override
    TList<T> real();

    @Override
    TList<T> getImag();

    @Override
    TList<T> imag();

    @Override
    TList<T> db();

    @Override
    TList<T> db2();

    @Override
    TList<T> abs();

    @Override
    TList<T> abssqr();

    @Override
    TList<T> log();

    @Override
    TList<T> log10();

    @Override
    TList<T> exp();

    @Override
    TList<T> sqrt();

    @Override
    TList<T> sqr();

    @Override
    TList<T> acosh();

    @Override
    TList<T> acos();

    @Override
    TList<T> asinh();

    @Override
    TList<T> asin();

    @Override
    TList<T> atan();

    @Override
    TList<T> acotan();
}
