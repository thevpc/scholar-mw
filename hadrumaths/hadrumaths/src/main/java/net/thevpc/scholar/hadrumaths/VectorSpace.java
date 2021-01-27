package net.thevpc.scholar.hadrumaths;

import net.thevpc.common.util.TypeName;

import java.util.List;
import java.util.function.Function;

public interface VectorSpace<T> {
    TypeName<T> getItemType();

    <R> Function<R, T> getConverterFrom(TypeName<R> t);

    <R> Function<T, R> getConverterTo(TypeName<R> t);

    <R> Function<R, T> getConverterFrom(Class<R> t);

    <R> Function<T, R> getConverterTo(Class<R> t);

    <R> R convertTo(T value, Class<R> t);

    <R> T convertFrom(R value, Class<R> t);

    T convert(double d);

    T convert(Complex d);

    T convert(Matrix d);

    T zero();

    T one();

    T nan();

    T add(T a, T b);


    RepeatableOp<T> addRepeatableOp();

    RepeatableOp<T> mulRepeatableOp();

    T addAll(List<T> b);

    T mulAll(List<T> b);

    T minus(T a, T b);

    T mul(T a, T b);

    T div(T a, T b);

    T rem(T a, T b);

    T real(T a);

    T imag(T a);

    T abs(T a);

    double absdbl(T a);

    double absdblsqr(T a);

    T abssqr(T a);

    T neg(T t);

    T conj(T t);

    T inv(T t);

    T sin(T t);

    T cos(T t);

    T tan(T t);

    T cotan(T t);

    T sinh(T t);

    T sincard(T t);

    T cosh(T t);

    T tanh(T t);

    T cotanh(T t);

    T asinh(T t);

    T acosh(T t);

    T asin(T t);

    T acos(T t);

    T atan(T t);

    T arg(T t);

    boolean isZero(T a);

    <R> boolean is(T value, TypeName<R> type);

    boolean isComplex(T a);

    Complex toComplex(T a);

    T acotan(T t);

    T exp(T t);

    T log(T t);

    T log10(T t);

    T db(T t);

    T db2(T t);

    T sqr(T t);

    T sqrt(T t);

    T sqrt(T t, int n);

    T pow(T a, T b);

    T pow(T a, double b);

    T npow(T t, int n);

    T lt(T a, T b);

    T lte(T a, T b);

    T gt(T a, T b);

    T gte(T a, T b);

    T eq(T a, T b);

    T ne(T a, T b);

    T not(T a);

    T and(T a, T b);

    T or(T a, T b);

    T If(T cond, T exp1, T exp2);

    T parse(String string);

    T scalarProduct(T a, T b);

    T setParam(T a, String paramName, Object b);

    Ops<T> ops();

    interface Ops<T> {
        VectorOp<T> real();

        VectorOp<T> imag();

        VectorOp<T> abs();

        VectorOp<T> abssqr();

        VectorOp<T> neg();

        VectorOp<T> conj();

        VectorOp<T> inv();

        VectorOp<T> sin();

        VectorOp<T> cos();

        VectorOp<T> tan();

        VectorOp<T> cotan();

        VectorOp<T> sinh();

        VectorOp<T> sincard();

        VectorOp<T> cosh();

        VectorOp<T> tanh();

        VectorOp<T> cotanh();

        VectorOp<T> asinh();

        VectorOp<T> acosh();

        VectorOp<T> asin();

        VectorOp<T> acos();

        VectorOp<T> atan();

        VectorOp<T> arg();

        VectorOp<T> acotan();

        VectorOp<T> exp();

        VectorOp<T> log();

        VectorOp<T> log10();

        VectorOp<T> db();

        VectorOp<T> db2();

        VectorOp<T> sqr();

        VectorOp<T> sqrt();

        VectorOp<T> not();
    }
}
