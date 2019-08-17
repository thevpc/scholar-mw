package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;
import net.vpc.scholar.hadrumaths.symbolic.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ExprVectorSpace extends AbstractVectorSpace<Expr> {

//    @Override
//    public <R> R convertTo(Expr value,Class<R> t) {
//        if(t.equals(Complex.class)){
//            return (R) value.toComplex();
//        }
//        if(t.equals(Double.class)){
//            return (R) Double.valueOf(value.toDouble());
//        }
//        if(t.equals(Matrix.class)){
//            return (R) Maths.matrix(1, 1, new MatrixCell() {
//                @Override
//                public Complex get(int row, int column) {
//                    return value.toComplex();
//                }
//            });
//        }
//        if(t.equals(TMatrix.class)){
//            return (R) Maths.tmatrix(Expr.class, 1, 1, new TMatrixCell<Expr>() {
//                @Override
//                public Expr get(int row, int column) {
//                    return value;
//                }
//            });
//        }
//        if(t.equals(Vector.class)){
//            return (R) Maths.columnVector(new Complex[]{value.toComplex()});
//        }
//        if(t.equals(TVector.class)){
//            return (R) Maths.columnTVector(Expr.class, 1, new TVectorCell<Expr>() {
//                @Override
//                public Expr get(int index) {
//                    return value;
//                }
//            });
//        }
//        throw new ClassCastException();
//    }
//
//    @Override
//    public <R> Expr convertFrom(R value, Class<R> t) {
//        if(t.equals(Complex.class)){
//            return (Complex) value;
//        }
//        if(t.equals(Double.class)){
//            return (Complex) Complex.valueOf((Double)value);
//        }
//        if(t.equals(Matrix.class)){
//            return (Complex) ((Matrix)value).toComplex();
//        }
//        if(t.equals(TMatrix.class)){
//            return (Complex) ((TMatrix)value).toComplex();
//        }
//        if(t.equals(Vector.class)){
//            return (Complex) ((Vector)value).toComplex();
//        }
//        if(t.equals(TVector.class)){
//            return (Complex) ((TVector)value).toComplex();
//        }
//        throw new ClassCastException();
//    }

    @Override
    public Expr convert(double d) {
        return Complex.valueOf(d);
    }

    @Override
    public Expr convert(Complex d) {
        return d;
    }

    @Override
    public Expr convert(TMatrix d) {
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
                return new Plus(all.toArray(new Expr[0]));
            } else {
                List<Expr> aa = a.getSubExpressions();
                List<Expr> all = new ArrayList<>(aa.size() + 1);
                all.addAll(aa);
                all.add(b);
                return new Plus(all.toArray(new Expr[0]));
            }
        } else {
            if (b instanceof Plus) {
                List<Expr> bb = b.getSubExpressions();
                List<Expr> all = new ArrayList<>(bb.size() + 1);
                all.add(a);
                all.addAll(bb);
                return new Plus(all.toArray(new Expr[0]));
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
        TList<Expr> all = Maths.elist();
        //this is needed not to provoke StackOverFlow Exception on evaluation mainly if a "plus" is performed in a loop!
        for (Expr expr : new Expr[]{a, b}) {
            if (expr instanceof Mul) {
                all.appendAll(expr.getSubExpressions());
            } else {
                all.append(expr);
            }
        }
        return new Mul(all);
    }

    @Override
    public Expr div(Expr a, Expr b) {
        return new Div(a, b);
    }

    @Override
    public Expr rem(Expr a, Expr b) {
        return new Reminder(a, b);
    }

    @Override
    public Expr real(Expr a) {
        if (a.isDoubleExpr()) {
            return a;
        }
        if (a.isComplex()) {
            return ((a.toComplex().real()));
        }
        return new Real(a.toDC());
    }

    @Override
    public Expr imag(Expr a) {
        if (a.isDoubleExpr()) {
            return Maths.DDZERO;
        }
        if (a.isComplex()) {
            return ((a.toComplex().imag()));
        }
        return new Imag(a.toDC());
    }

    @Override
    public double absdbl(Expr a) {
        if (a.isDouble()) {
            return Math.abs(a.toDouble());
        }
        if (a.isComplex()) {
            return (a.toComplex().absdbl());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public double absdblsqr(Expr a) {
        if (a.isDouble()) {
            double d = a.toDouble();
            return d * d;
        }
        if (a.isComplex()) {
            return (a.toComplex().absdblsqr());
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr abssqr(Expr a) {
        if (a.isDouble()) {
            return Complex.valueOf(Maths.DOUBLE_VECTOR_SPACE.abssqr(a.toDouble()));
        } else if (a.isDoubleExpr()) {
            return ComplexValue.valueOf(Maths.DOUBLE_VECTOR_SPACE.abssqr(a.toDouble()), a.getDomain());
        } else if (a.isComplex()) {
            return (a.toComplex().abssqr());
        } else if (a.isComplexExpr()) {
            return ComplexValue.valueOf(a.toComplex().abssqr(), a.getDomain());
        } else if (a instanceof Discrete) {
            return ((Discrete) a).abssqr();
        } else if (a instanceof VDiscrete) {
            return ((VDiscrete) a).abssqr();
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr lt(Expr a, Expr b) {
        if (a.isDouble() && b.isDouble()) {
            return DoubleValue.valueOf(Double.compare(a.toDouble(), b.toDouble()) < 0 ? 1 : 0);
        }
        if (a.isDoubleExpr() && b.isDoubleExpr()) {
            return DoubleValue.valueOf(Double.compare(a.toDouble(), b.toDouble()) < 0 ? 1 : 0, a.getDomain().intersect(b.getDomain()));
        }
        if (a.isComplex() && b.isComplex()) {
            return ((a.toComplex().compareTo(b.toComplex()))) < 0 ? one() : zero();
        }
        return new LtExpr(a, b);
    }

    @Override
    public Expr lte(Expr a, Expr b) {
        if (a.isDouble() && b.isDouble()) {
            return DoubleValue.valueOf(Double.compare(a.toDouble(), b.toDouble()) <= 0 ? 1 : 0);
        }
        if (a.isDoubleExpr() && b.isDoubleExpr()) {
            return DoubleValue.valueOf(Double.compare(a.toDouble(), b.toDouble()) <= 0 ? 1 : 0, a.getDomain().intersect(b.getDomain()));
        }
        if (a.isComplex() && b.isComplex()) {
            return ((a.toComplex().compareTo(b.toComplex()))) <= 0 ? one() : zero();
        }
        return new LteExpr(a, b);
    }

    @Override
    public Expr gt(Expr a, Expr b) {
        if (a.isDouble()) {
            return DoubleValue.valueOf(Double.compare(a.toDouble(), b.toDouble()) < 0 ? 1 : 0);
        }
        if (a.isDoubleExpr()) {
            return DoubleValue.valueOf(Double.compare(a.toDouble(), b.toDouble()) < 0 ? 1 : 0, a.getDomain().intersect(b.getDomain()));
        }
        if (a.isComplex() && b.isComplex()) {
            return ((a.toComplex().compareTo(b.toComplex()))) > 0 ? one() : zero();
        }
        return new GtExpr(a, b);
    }

    @Override
    public Expr gte(Expr a, Expr b) {
        if (a.isDouble() && b.isDouble()) {
            return DoubleValue.valueOf(Double.compare(a.toDouble(), b.toDouble()) >= 0 ? 1 : 0);
        }
        if (a.isDoubleExpr() && b.isDoubleExpr()) {
            return DoubleValue.valueOf(Double.compare(a.toDouble(), b.toDouble()) >= 0 ? 1 : 0, a.getDomain().intersect(b.getDomain()));
        }
        if (a.isComplex() && b.isComplex()) {
            return ((a.toComplex().compareTo(b.toComplex()))) <= 0 ? one() : zero();
        }
        return new GteExpr(a, b);
    }

    @Override
    public Expr eq(Expr a, Expr b) {
        if (a.isDouble() && b.isDouble()) {
            return DoubleValue.valueOf(Double.compare(a.toDouble(), b.toDouble()) == 0 ? 1 : 0);
        }
        if (a.isDoubleExpr() && b.isDoubleExpr()) {
            return DoubleValue.valueOf(Double.compare(a.toDouble(), b.toDouble()) == 0 ? 1 : 0, a.getDomain().intersect(b.getDomain()));
        }
        if (a.isComplex() && b.isComplex()) {
            return ((a.toComplex().compareTo(b.toComplex()))) == 0 ? one() : zero();
        }
        return new EqExpr(a, b);
    }

    @Override
    public Expr ne(Expr a, Expr b) {
        if (a.isDouble() && b.isDouble()) {
            return DoubleValue.valueOf(Double.compare(a.toDouble(), b.toDouble()) != 0 ? 1 : 0);
        }
        if (a.isDoubleExpr() && b.isDoubleExpr()) {
            return DoubleValue.valueOf(Double.compare(a.toDouble(), b.toDouble()) != 0 ? 1 : 0, a.getDomain().intersect(b.getDomain()));
        }
        if (a.isComplex() && b.isComplex()) {
            return ((a.toComplex().compareTo(b.toComplex()))) != 0 ? one() : zero();
        }
        return new NeExpr(a, b);
    }

    @Override
    public Expr and(Expr a, Expr b) {
        if (a.isDouble() && b.isDouble()) {
            return DoubleValue.valueOf((a.toDouble() != 0 && b.toDouble() != 0) ? 1 : 0);
        }
        if (a.isDoubleExpr() && b.isDoubleExpr()) {
            return DoubleValue.valueOf((a.toDouble() != 0 && b.toDouble() != 0) ? 1 : 0, a.getDomain().intersect(b.getDomain()));
        }
        if (a.isComplex() && b.isComplex()) {
            return ((!a.toComplex().isZero() && !b.toComplex().isZero())) ? one() : zero();
        }
        return new AndExpr(a, b);
    }

    @Override
    public Expr or(Expr a, Expr b) {
        if (a.isDouble() && b.isDouble()) {
            return DoubleValue.valueOf((a.toDouble() != 0 || b.toDouble() != 0) ? 1 : 0);
        }
        if (a.isDoubleExpr() && b.isDoubleExpr()) {
            return DoubleValue.valueOf((a.toDouble() != 0 || b.toDouble() != 0) ? 1 : 0, a.getDomain().intersect(b.getDomain()));
        }
        if (a.isComplex() && b.isComplex()) {
            return ((!a.toComplex().isZero() || !b.toComplex().isZero())) ? one() : zero();
        }
        return new OrExpr(a, b);
    }

    @Override
    public Expr If(Expr cond, Expr exp1, Expr exp2) {
        return new IfThenElse(cond, exp1, exp2);
    }

    @Override
    public Expr not(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(e.toDouble() == 0 ? 1 : 0);
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(e.toDouble() == 0 ? 1 : 0, e.getDomain());
        }
        if (e.isComplex()) {
            return ((e.toComplex().isZero())) ? one() : zero();
        }
        return new NotExpr(e);
    }

    @Override
    public Expr abs(Expr e) {
        if (e.isDouble()) {
            double a = e.toDouble();
            if (a < 0) {
                return DoubleValue.valueOf(Math.abs(a));
            }
            return e;
        }
        if (e.isDoubleExpr()) {
            double a = e.toDouble();
            if (a < 0) {
                return DoubleValue.valueOf(Math.abs(a), e.getDomain());
            }
            return e;
        }
        if (e.isComplex()) {
            return ((e.toComplex().abs()));
        }
        return new Abs(e);
    }

    @Override
    public Expr neg(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(-(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(-(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().neg());
        }
        return new Neg(e);
    }

    @Override
    public Expr conj(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Maths.conj(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Maths.conj(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().conj());
        }
        return new Conj(e);
    }

    @Override
    public Expr inv(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Maths.inv(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Maths.inv(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().inv());
        }
        return new Inv(e);
    }

    @Override
    public Expr sin(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Maths.sin(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Maths.sin(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().sin());
        }
        return new Sin(e);
    }

    @Override
    public Expr cos(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Math.cos(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Math.cos(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().cos());
        }
        return new Cos(e);
    }

    @Override
    public Expr tan(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Math.tan(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Math.tan(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().tan());
        }
        return new Tan(e);
    }

    @Override
    public Expr cotan(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Maths.cotan(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Maths.cotan(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().cotan());
        }
        return new Cotan(e);
    }

    @Override
    public Expr sinh(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Math.sinh(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Math.sinh(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().sinh());
        }
        return new Sinh(e);
    }

    @Override
    public Expr sincard(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Maths.sincard(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Maths.sincard(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().sincard());
        }
        return new Sincard(e);
    }

    @Override
    public Expr cosh(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Math.cosh(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Math.cosh(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().cosh());
        }
        return new Cosh(e);
    }

    @Override
    public Expr tanh(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Math.tanh(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Math.tanh(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().tanh());
        }
        return new Tanh(e);
    }

    @Override
    public Expr cotanh(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Maths.cotanh(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Maths.cotanh(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().cotanh());
        }
        return new Cotanh(e);
    }

    @Override
    public Expr asinh(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Maths.asinh(e.toDouble()));
        }
        if (e.isDouble()) {
            return DoubleValue.valueOf(Maths.asinh(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().asinh());
        }
        return new Asinh(e);
    }

    @Override
    public Expr acosh(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Maths.acosh(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Maths.acosh(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().acosh());
        }
        return new Acosh(e);
    }

    @Override
    public Expr asin(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Math.asin(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Math.asin(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().asin());
        }
        return new Asin(e);
    }

    @Override
    public Expr acos(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Math.acos(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Math.acos(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().acos());
        }
        return new Acos(e);
    }

    @Override
    public Expr atan(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Math.atan(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Math.atan(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().atan());
        }
        return new Atan(e);
    }

    @Override
    public Expr arg(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Maths.arg(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Maths.arg(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().arg());
        }
        return new Arg(e);
    }

    @Override
    public Expr acotan(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Maths.acotan(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Maths.acotan(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().acotan());
        }
        return new Acotan(e);
    }

    @Override
    public Expr exp(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Maths.exp(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Maths.exp(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().exp());
        }
        return new Exp(e);
    }

    @Override
    public Expr log(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Math.log(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Math.log(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().log());
        }
        return new Log(e);
    }

    @Override
    public Expr log10(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Math.log10(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Math.log10(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().log10());
        }
        return new Log10(e);
    }

    @Override
    public Expr db(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Maths.db(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Maths.db(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().db());
        }
        return new Db(e);
    }

    @Override
    public Expr db2(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Maths.db2(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Maths.db2(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().db2());
        }
        return new Db2(e);
    }

    @Override
    public Expr sqr(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Maths.sqr(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Maths.sqr(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().sqr());
        }
        if (e instanceof Discrete) {
            return ((Discrete) e).sqr();
        }
        if (e instanceof VDiscrete) {
            return ((VDiscrete) e).sqr();
        }
        return new Sqr(e);
    }

    @Override
    public Expr sqrt(Expr e) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Maths.sqrt(e.toDouble()));
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Maths.sqrt(e.toDouble()), e.getDomain());
        }
        if (e.isComplex()) {
            return (e.toComplex().sqrt());
        }
        if (e instanceof Discrete) {
            return MathsSampler.sqrt((Discrete) e);
        }
        if (e instanceof VDiscrete) {
            return MathsSampler.sqrt((VDiscrete) e);
        }
        return new Sqrt(e);
    }

    @Override
    public Expr sqrt(Expr e, int n) {
        if (e.isDouble()) {
            return DoubleValue.valueOf(Maths.sqrt(e.toDouble(), n), e.getDomain());
        }
        if (e.isDoubleExpr()) {
            return DoubleValue.valueOf(Maths.sqrt(e.toDouble(), n));
        }
        if (e.isComplex()) {
            return (e.toComplex().sqrt(n));
        }
        return new Sqrtn(e, n);
    }

    @Override
    public Expr pow(Expr a, Expr b) {
        if (a.isDouble()) {
            return DoubleValue.valueOf(Maths.pow(a.toDouble(), b.toDouble()));
        }
        if (a.isDoubleExpr()) {
            return DoubleValue.valueOf(Maths.pow(a.toDouble(), b.toDouble()), a.getDomain());
        }
        if (a.isComplex()) {
            return (a.toComplex().pow(b.toComplex()));
        }
        return new Pow(a, b);
    }

    @Override
    public Expr pow(Expr a, double b) {
        if (a.isDouble()) {
            return DoubleValue.valueOf(Maths.pow(a.toDouble(), b));
        }
        if (a.isDoubleExpr()) {
            return DoubleValue.valueOf(Maths.pow(a.toDouble(), b), a.getDomain());
        }
        if (a.isComplex()) {
            return (a.toComplex().pow(b));
        }
        return new Pow(a, Maths.expr(b));
    }

    @Override
    public Expr npow(Expr a, int b) {
        if (a.isDouble()) {
            return DoubleValue.valueOf(Maths.pow(a.toDouble(), b));
        }
        if (a.isDoubleExpr()) {
            return DoubleValue.valueOf(Maths.pow(a.toDouble(), b), a.getDomain());
        }
        if (a.isComplex()) {
            return (a.toComplex().pow(b));
        }
        return new Pow(a, Maths.expr(b));
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
    public Expr parse(String string) {
        return Complex.valueOf(string);
    }

    @Override
    public TypeName<Expr> getItemType() {
        return Maths.$EXPR;
    }

    @Override
    public Expr scalarProduct(Expr a, Expr b) {
        return Maths.Config.getScalarProductOperator().eval(a, b);
    }

    @Override
    public Expr setParam(Expr a, String paramName, Object b) {
        if (b instanceof Expr) {
            return a.setParam(paramName, ((Expr) b));
        }
        if (b instanceof Number) {
            return a.setParam(paramName, ((Number) b).doubleValue());
        }
        throw new IllegalArgumentException("Unsupported param type");
    }

    @Override
    public RepeatableOp<Expr> addRepeatableOp() {
        return new ExprAddRepeatableOp();
    }

    @Override
    public RepeatableOp<Expr> mulRepeatableOp() {
        return new ExprMulRepeatableOp();
    }

    private static class ExprAddRepeatableOp implements RepeatableOp<Expr> {
        MutableComplex c = new MutableComplex();
        Queue<Expr> t = new LinkedList<>();
        List<Expr> all = new ArrayList<>();

        public ExprAddRepeatableOp() {
        }

        @Override
        public void append(Expr item) {
            t.add(item);
            while (!t.isEmpty()) {
                Expr e2 = t.remove();
                if (e2 instanceof Plus) {
                    t.addAll(e2.getSubExpressions());
                } else {
                    if (e2.isComplex()) {
                        Complex v = e2.toComplex();
                        c.add(v);
                    } else {
                        all.add(e2);
                    }
                }
            }
        }

        @Override
        public Expr eval() {
            Complex complex = c.toComplex();
            if (all.isEmpty()) {
                return complex;
            }
            if (!complex.equals(Maths.CONE)) {
                all.add(0, complex);
            }
            return new Plus(all.toArray(new Expr[0]));
        }
    }

    private static class ExprMulRepeatableOp implements RepeatableOp<Expr> {
        MutableComplex c = new MutableComplex(1, 0);
        Queue<Expr> t = new LinkedList<>();
        List<Expr> all = new ArrayList<>();
        boolean zero = false;

        public ExprMulRepeatableOp() {
//            System.out.println(">>>>>>>>>>>>>>>>>>>>>       ExprMulRepeatableOp");
        }

        @Override
        public void append(Expr item) {
            if (zero) {
                return;
            }
            t.add(item);
            while (!t.isEmpty()) {
                Expr e2 = t.remove();
                if (e2 instanceof Mul) {
                    t.addAll(e2.getSubExpressions());
                } else {
                    if (e2.isComplex()) {
                        Complex v = e2.toComplex();
                        if (c.isZero()) {
                            zero = true;
                            return;
                        }
                        c.mul(v);
                    } else {
                        all.add(e2);
                    }
                }
            }
        }

        @Override
        public Expr eval() {
            Complex complex = c.toComplex();
            if (all.isEmpty()) {
                return complex;
            }
            if (!complex.equals(Maths.CONE)) {
                all.add(0, complex);
            }
            return new Mul(all.toArray(new Expr[0]));
        }
    }

    @Override
    public <R> boolean is(Expr value, TypeName<R> type) {
        if (Maths.$EXPR.equals(type)) {
            return true;
        }
        if (Maths.$COMPLEX.equals(type)) {
            return value.isComplex();
        }
        if (Maths.$DOUBLE.equals(type)) {
            return value.isDouble();
        }
        if (Maths.$INTEGER.equals(type)) {
            return value.isDouble() && value.toDouble() == (int) value.toDouble();
        }
        if (Maths.$LONG.equals(type)) {
            return value.isDouble() && value.toDouble() == (long) value.toDouble();
        }
        return false;
    }
}
