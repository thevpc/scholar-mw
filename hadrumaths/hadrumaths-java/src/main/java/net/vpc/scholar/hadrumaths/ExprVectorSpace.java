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
        for (Expr expr : new Expr[]{a, b}) {
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
    public Expr lt(Expr a, Expr b) {
        if (a.isComplex() && b.isComplex()) {
            return ((a.toComplex().compareTo(b.toComplex()))) < 0 ? one() : zero();
        }
        if (a instanceof DoubleValue && b instanceof DoubleValue) {
            return DoubleValue.valueOf(Double.compare(((DoubleValue) a).getValue(), (((DoubleValue) b).getValue())) < 0 ? 1 : 0, a.getDomain().intersect(b.getDomain()));
        }
        return new LtExpr(a, b);
    }

    @Override
    public Expr lte(Expr a, Expr b) {
        if (a.isComplex() && b.isComplex()) {
            return ((a.toComplex().compareTo(b.toComplex()))) <= 0 ? one() : zero();
        }
        if (a instanceof DoubleValue && b instanceof DoubleValue) {
            return DoubleValue.valueOf(Double.compare(((DoubleValue) a).getValue(), (((DoubleValue) b).getValue())) <= 0 ? 1 : 0, a.getDomain().intersect(b.getDomain()));
        }
        return new LteExpr(a, b);
    }

    @Override
    public Expr gt(Expr a, Expr b) {
        if (a.isComplex() && b.isComplex()) {
            return ((a.toComplex().compareTo(b.toComplex()))) > 0 ? one() : zero();
        }
        if (a instanceof DoubleValue && b instanceof DoubleValue) {
            return DoubleValue.valueOf(Double.compare(((DoubleValue) a).getValue(), (((DoubleValue) b).getValue())) < 0 ? 1 : 0, a.getDomain().intersect(b.getDomain()));
        }
        return new GtExpr(a, b);
    }

    @Override
    public Expr gte(Expr a, Expr b) {
        if (a.isComplex() && b.isComplex()) {
            return ((a.toComplex().compareTo(b.toComplex()))) <= 0 ? one() : zero();
        }
        if (a instanceof DoubleValue && b instanceof DoubleValue) {
            return DoubleValue.valueOf(Double.compare(((DoubleValue) a).getValue(), (((DoubleValue) b).getValue())) >= 0 ? 1 : 0, a.getDomain().intersect(b.getDomain()));
        }
        return new GteExpr(a, b);
    }
    @Override
    public Expr eq(Expr a, Expr b) {
        if (a.isComplex() && b.isComplex()) {
            return ((a.toComplex().compareTo(b.toComplex()))) == 0 ? one() : zero();
        }
        if (a instanceof DoubleValue && b instanceof DoubleValue) {
            return DoubleValue.valueOf(Double.compare(((DoubleValue) a).getValue(), (((DoubleValue) b).getValue())) == 0 ? 1 : 0, a.getDomain().intersect(b.getDomain()));
        }
        return new EqExpr(a, b);
    }

    @Override
    public Expr ne(Expr a, Expr b) {
        if (a.isComplex() && b.isComplex()) {
            return ((a.toComplex().compareTo(b.toComplex()))) != 0 ? one() : zero();
        }
        if (a instanceof DoubleValue && b instanceof DoubleValue) {
            return DoubleValue.valueOf(Double.compare(((DoubleValue) a).getValue(), (((DoubleValue) b).getValue())) != 0 ? 1 : 0, a.getDomain().intersect(b.getDomain()));
        }
        return new NeExpr(a, b);
    }

    @Override
    public Expr and(Expr a, Expr b) {
        if (a.isComplex() && b.isComplex()) {
            return ((!a.toComplex().isZero() && !b.toComplex().isZero())) ? one() : zero();
        }
        if (a instanceof DoubleValue && b instanceof DoubleValue) {
            return DoubleValue.valueOf((((DoubleValue) a).getValue()!=0 && (((DoubleValue) b).getValue())!=0) ? 1 : 0, a.getDomain().intersect(b.getDomain()));
        }
        return new AndExpr(a, b);
    }

    @Override
    public Expr or(Expr a, Expr b) {
        if (a.isComplex() && b.isComplex()) {
            return ((!a.toComplex().isZero() || !b.toComplex().isZero())) ? one() : zero();
        }
        if (a instanceof DoubleValue && b instanceof DoubleValue) {
            return DoubleValue.valueOf((((DoubleValue) a).getValue()!=0 || (((DoubleValue) b).getValue())!=0) ? 1 : 0, a.getDomain().intersect(b.getDomain()));
        }
        return new OrExpr(a, b);
    }

    @Override
    public Expr If(Expr cond, Expr exp1, Expr exp2) {
        return new IfThenElse(cond, exp1, exp2);
    }

    @Override
    public Expr not(Expr e) {
        if (e.isComplex()) {
            return ((e.toComplex().isZero()))?one():zero();
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(((DoubleValue) e).getValue()==0?1:0, e.getDomain());
        }
        return new NotExpr(e);
    }

    @Override
    public Expr abs(Expr e) {
        if (e.isComplex()) {
            return ((e.toComplex().abs()));
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.abs(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Abs(e);
    }

    @Override
    public Expr neg(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().atan());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.atan(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Neg(e);
    }

    @Override
    public Expr conj(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().conj());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.conj(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Conj(e);
    }

    @Override
    public Expr inv(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().inv());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.inv(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Inv(e);
    }

    @Override
    public Expr sin(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().sin());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.sin(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Sin(e);
    }

    @Override
    public Expr cos(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().cos());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.cos(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Cos(e);
    }

    @Override
    public Expr tan(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().tan());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.tan(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Tan(e);
    }

    @Override
    public Expr cotan(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().cotan());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.cotan(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Cotan(e);
    }

    @Override
    public Expr sinh(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().sinh());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.sinh(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Sinh(e);
    }

    @Override
    public Expr sincard(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().sincard());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.sincard(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Sincard(e);
    }

    @Override
    public Expr cosh(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().cosh());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.cosh(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Cosh(e);
    }

    @Override
    public Expr tanh(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().tanh());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.tanh(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Conj(e);
    }

    @Override
    public Expr cotanh(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().cotanh());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.cotanh(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Cotanh(e);
    }

    @Override
    public Expr asinh(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().asinh());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.asinh(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Asinh(e);
    }

    @Override
    public Expr acosh(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().acosh());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.acosh(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Acosh(e);
    }

    @Override
    public Expr asin(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().asin());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.asin(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Asin(e);
    }

    @Override
    public Expr acos(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().acos());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.acos(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Acos(e);
    }

    @Override
    public Expr atan(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().atan());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.atan(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Atan(e);
    }

    @Override
    public Expr arg(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().arg());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.arg(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Arg(e);
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
        return new Acotan(e);
    }

    @Override
    public Expr exp(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().exp());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.exp(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Exp(e);
    }

    @Override
    public Expr log(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().log());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.log(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Log(e);
    }

    @Override
    public Expr log10(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().log10());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.log10(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Log10(e);
    }

    @Override
    public Expr db(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().db());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.db(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Db(e);
    }

    @Override
    public Expr db2(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().db2());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.db2(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Db2(e);
    }

    @Override
    public Expr sqr(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().sqr());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.sqr(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Sqr(e);
    }

    @Override
    public Expr sqrt(Expr e) {
        if (e.isComplex()) {
            return (e.toComplex().sqrt());
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.sqrt(((DoubleValue) e).getValue()), e.getDomain());
        }
        return new Sqrt(e);
    }

    @Override
    public Expr sqrt(Expr e, int n) {
        if (e.isComplex()) {
            return (e.toComplex().sqrt(n));
        }
        if (e instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.sqrt(((DoubleValue) e).getValue(), n), e.getDomain());
        }
        return new Sqrtn(e, n);
    }

    @Override
    public Expr pow(Expr a, Expr b) {
        if (a.isComplex()) {
            return (a.toComplex().pow(b.toComplex()));
        }
        if (a instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.pow(((DoubleValue) a).getValue(), b.toDouble()), a.getDomain());
        }
        return new Pow(a, b);
    }

    @Override
    public Expr pow(Expr a, double b) {
        if (a.isComplex()) {
            return (a.toComplex().pow(b));
        }
        if (a instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.pow(((DoubleValue) a).getValue(), b), a.getDomain());
        }
        return new Pow(a, Maths.expr(b));
    }

    @Override
    public Expr npow(Expr a, int b) {
        if (a.isComplex()) {
            return (a.toComplex().pow(b));
        }
        if (a instanceof DoubleValue) {
            return DoubleValue.valueOf(Maths.pow(((DoubleValue) a).getValue(), b), a.getDomain());
        }
        return new Pow(a, Maths.expr(b));
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
