package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.*;

import java.util.ArrayList;
import java.util.List;

public class ExprVectorSpace implements VectorSpace<Expr> {
    @Override
    public Expr convert(double d) {
        return Complex.valueOf(d);
    }

    @Override
    public Expr convert(Complex d) {
        return d;
    }

    @Override
    public Expr convert(Matrix d) {
        return d.toComplex();
    }

    @Override
    public Expr zero() {
        return Complex.ZERO;
    }

    @Override
    public Expr one() {
        return Complex.ONE;
    }

    @Override
    public Expr nan() {
        return Complex.NaN;
    }

    @Override
    public Expr add(Expr a, Expr b) {
        if (a instanceof Plus) {
            if (b instanceof Plus) {
                List<Expr> aa = a.getSubExpressions();
                List<Expr> bb = b.getSubExpressions();
                List<Expr> all = new ArrayList<>(aa.size() + bb.size());
                all.addAll(aa);
                all.addAll(bb);
                return new Plus(all.toArray(new Expr[all.size()]));
            } else {
                List<Expr> aa = a.getSubExpressions();
                List<Expr> all = new ArrayList<>(aa.size() + 1);
                all.addAll(aa);
                all.add(b);
                return new Plus(all.toArray(new Expr[all.size()]));
            }
        } else {
            if (b instanceof Plus) {
                List<Expr> bb = b.getSubExpressions();
                List<Expr> all = new ArrayList<>(bb.size() + 1);
                all.add(a);
                all.addAll(bb);
                return new Plus(all.toArray(new Expr[all.size()]));
            } else {
                return new Plus(a, b);
            }
        }
    }

    @Override
    public Expr sub(Expr a, Expr b) {
        return new Sub(a, b);
    }

    @Override
    public Expr mul(Expr a, Expr b) {
        ExprList all = new ArrayExprList();
        //this is needed not to provoke StackOverFlow Exception on evaluation mainly if a "plus" is performed in a loop!
        for (Expr expr : new Expr[]{a,b}) {
            if (expr instanceof Mul) {
                all.addAll(expr.getSubExpressions());
            } else {
                all.add(expr);
            }
        }
        return new Mul(all);
    }

    @Override
    public Expr div(Expr a, Expr b) {
        return new Div(a, b);
    }

    @Override
    public Expr real(Expr a) {
        if (a.isComplex()) {
            return ((a.toComplex().real()));
        }
        if (a instanceof DoubleValue) {
            return a;
        }
        return new Real(a.toDC());
    }

    @Override
    public Expr imag(Expr a) {
        if (a.isComplex()) {
            return ((a.toComplex().imag()));
        }
        if (a instanceof DoubleValue) {
            return Maths.DDZERO;
        }
        if (a.isDouble()) {
            return Maths.DDZERO;
        }
        return new Imag(a.toDC());
    }

    @Override
    public double absdbl(Expr a) {
        if (a.isComplex()) {
            return (a.toComplex().absdbl());
        }
        if (a.isDouble()) {
            return Maths.abs(a.toDouble());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr abs(Expr a) {
        if (a.isComplex()) {
            return ((a.toComplex().abs()));
        }
        if (a instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.abs(((DoubleValue) a).getValue()), a.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr neg(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().atan());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.atan(((DoubleValue) e).getValue()), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr conj(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().conj());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.conj(((DoubleValue) e).getValue()), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr inv(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().inv());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.inv(((DoubleValue) e).getValue()), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr sin(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().sin());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.sin(((DoubleValue) e).getValue()), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr cos(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().cos());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.cos(((DoubleValue) e).getValue()), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr tan(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().tan());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.tan(((DoubleValue) e).getValue()), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr cotan(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().cotan());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.cotan(((DoubleValue) e).getValue()), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr sinh(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().sinh());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.sinh(((DoubleValue) e).getValue()), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr cosh(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().cosh());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.cosh(((DoubleValue) e).getValue()), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr tanh(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().tanh());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.tanh(((DoubleValue) e).getValue()), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr cotanh(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().cotanh());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.cotanh(((DoubleValue) e).getValue()), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");    }

    @Override
    public Expr asinh(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().asinh());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.asinh(((DoubleValue) e).getValue()), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr acosh(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().acosh());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.acosh(((DoubleValue) e).getValue()), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr asin(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().asin());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.asin(((DoubleValue) e).getValue()), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr acos(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().acos());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.acos(((DoubleValue) e).getValue()), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr atan(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().atan());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.atan(((DoubleValue) e).getValue()), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr arg(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().arg());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.arg(((DoubleValue) e).getValue()), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public boolean isZero(Expr a) {
        return a.isZero();
    }

    @Override
    public boolean isComplex(Expr a) {
        return a.isComplex();
    }

    @Override
    public Complex toComplex(Expr a) {
        return a.toComplex();
    }

    @Override
    public Expr acotan(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().acotan());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.acotan(((DoubleValue) e).getValue()), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr exp(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().exp());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.exp(((DoubleValue) e).getValue()), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr log(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().log());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.log(((DoubleValue) e).getValue()), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr log10(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().log10());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.log10(((DoubleValue) e).getValue()), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr db(Expr a) {
        if (a.isComplex()) {
            return (a.toComplex().db());
        }
        if (a instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.db(((DoubleValue) a).getValue()), a.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr db2(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().db2());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.db2(((DoubleValue) e).getValue()), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr sqr(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().sqr());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.sqr(((DoubleValue) e).getValue()), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr sqrt(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().sqrt());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.sqrt(((DoubleValue) e).getValue()), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr sqrt(Expr e, int n) {
        if (e.isComplex()) {
            return (e.toComplex().sqrt(n));
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.sqrt(((DoubleValue) e).getValue(),n), e.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr pow(Expr a, Expr b) {
        if (a.isComplex()) {
            return (a.toComplex().pow(b.toComplex()));
        }
        if (a instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.pow(((DoubleValue) a).getValue(),b.toDouble()), a.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr pow(Expr a, double b) {
        if (a.isComplex()) {
            return (a.toComplex().pow(b));
        }
        if (a instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.pow(((DoubleValue) a).getValue(),b), a.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr npow(Expr a, int b) {
        if (a.isComplex()) {
            return (a.toComplex().pow(b));
        }
        if (a instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.pow(((DoubleValue) a).getValue(),b), a.getDomain());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr parse(String string) {
        return Complex.valueOf(string);
    }

    @Override
    public Class<Expr> getItemType() {
        return Expr.class;
    }
}
