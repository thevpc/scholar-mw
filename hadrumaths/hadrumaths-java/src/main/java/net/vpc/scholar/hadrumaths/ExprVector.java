package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.util.InflatedExprTitleResolver;

import java.util.Collection;

public interface ExprVector extends TVector<Expr> {
    @Override
    ExprVector eval(ElementOp<Expr> op);

    @Override
    ExprVector setParam(TParam param, Object value);

    @Override
    ExprVector setParam(String name, Object value);

    @Override
    ExprVector transpose();

    @Override
    ExprVector scalarProduct(Expr other);

    @Override
    ExprVector rscalarProduct(Expr other);

    @Override
    <R> TVector<R> to(TypeName<R> other);

    @Override
    ExprVector vscalarProduct(TVector<Expr>... other);

    @Override
    ExprVector dotmul(TVector<Expr> other);

    @Override
    ExprVector dotdiv(TVector<Expr> other);

    @Override
    ExprVector dotpow(TVector<Expr> other);

    @Override
    ExprVector add(TVector<Expr> other);

    @Override
    ExprVector sub(TVector<Expr> other);

    @Override
    ExprVector add(Expr other);

    @Override
    ExprVector sub(Expr other);

    @Override
    ExprVector mul(Expr other);

    @Override
    ExprVector div(Expr other);

    @Override
    ExprVector rem(Expr other);

    @Override
    ExprVector dotpow(Expr other);

    @Override
    ExprVector conj();

    @Override
    ExprVector cos();

    @Override
    ExprVector cosh();

    @Override
    ExprVector sin();

    @Override
    ExprVector sinh();

    @Override
    ExprVector tan();

    @Override
    ExprVector tanh();

    @Override
    ExprVector cotan();

    @Override
    ExprVector cotanh();

    @Override
    ExprVector getReal();

    @Override
    ExprVector real();

    @Override
    ExprVector getImag();

    @Override
    ExprVector imag();

    @Override
    ExprVector db();

    @Override
    ExprVector db2();

    @Override
    ExprVector abs();

    @Override
    ExprVector abssqr();

    @Override
    ExprVector log();

    @Override
    ExprVector log10();

    @Override
    ExprVector exp();

    @Override
    ExprVector sqrt();

    @Override
    ExprVector sqr();

    @Override
    ExprVector acosh();

    @Override
    ExprVector acos();

    @Override
    ExprVector asinh();

    @Override
    ExprVector asin();

    @Override
    ExprVector atan();

    @Override
    ExprVector acotan();

    @Override
    ExprVector sublist(int fromIndex, int toIndex);

    @Override
    ExprVector sort();

    @Override
    ExprVector removeDuplicates();

    @Override
    ExprVector concat(Expr e);

    @Override
    ExprVector concat(TVector<Expr> e);

    @Override
    ExprVector neg();

    @Override
    ExprVector sincard();

    @Override
    ExprVector sqrt(int n);

    @Override
    ExprVector pow(TVector<Expr> b);

    @Override
    ExprVector pow(double n);

    @Override
    ExprVector arg();

    @Override
    ExprVector transform(TTransform<Expr, Expr> op);

    ExprVector append(Expr e);

    ExprVector appendAll(Collection<? extends Expr> e);

    ExprVector appendAll(TVector<Expr> e);

    ExprVector copy();

    @Override
    ExprVector filter(TFilter<Expr> filter);

    @Override
    ExprMatrix toMatrix();

    @Override
    ExprVector set(int i, Expr value);

    @Override
    ExprVector set(Enum i, Expr value);

    @Override
    ExprVector update(int i, Expr value);

    @Override
    ExprVector update(Enum i, Expr value);

    @Override
    ExprVector rem(TVector<Expr> other);

    @Override
    ExprVector inv();

    ExprVector setParams(DoubleParamValues p);

    ExprVector inflate(DoubleParamValues d);

    ExprVector inflate(DoubleParamValues d, InflatedExprTitleResolver title);

    ExprVector inflate(DoubleParamValues d,String title);

    ExprVector normalize();

    ExprVector simplify();

    ExprVector simplify(SimplifyOptions options);


}
