package net.vpc.scholar.hadrumaths;

import net.vpc.common.util.TypeName;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;
import net.vpc.scholar.hadrumaths.symbolic.conv.Imag;
import net.vpc.scholar.hadrumaths.symbolic.conv.Real;
import net.vpc.scholar.hadrumaths.symbolic.double2complex.CDiscrete;
import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.cond.*;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.*;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.trigo.*;

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
//        if(t.equals(Matrix.class)){
//            return (R) Maths.tmatrix(Expr.class, 1, 1, new MatrixCell<Expr>() {
//                @Override
//                public Expr get(int row, int column) {
//                    return value;
//                }
//            });
//        }
//        if(t.equals(Vector.class)){
//            return (R) Maths.columnVector(new Complex[]{value.toComplex()});
//        }
//        if(t.equals(Vector.class)){
//            return (R) Maths.columnTVector(Expr.class, 1, new VectorCell<Expr>() {
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
//        if(t.equals(Matrix.class)){
//            return (Complex) ((Matrix)value).toComplex();
//        }
//        if(t.equals(Vector.class)){
//            return (Complex) ((Vector)value).toComplex();
//        }
//        if(t.equals(Vector.class)){
//            return (Complex) ((Vector)value).toComplex();
//        }
//        throw new ClassCastException();
//    }

    @Override
    public TypeName<Expr> getItemType() {
        return Maths.$EXPR;
    }

    @Override
    public Expr convert(double d) {
        return Complex.of(d);
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
                List<Expr> aa = a.getChildren();
                List<Expr> bb = b.getChildren();
                List<Expr> all = new ArrayList<>(aa.size() + bb.size());
                all.addAll(aa);
                all.addAll(bb);
                return Plus.of(all.toArray(new Expr[0]));
            } else {
                List<Expr> aa = a.getChildren();
                List<Expr> all = new ArrayList<>(aa.size() + 1);
                all.addAll(aa);
                all.add(b);
                return Plus.of(all.toArray(new Expr[0]));
            }
        } else {
            if (b instanceof Plus) {
                List<Expr> bb = b.getChildren();
                List<Expr> all = new ArrayList<>(bb.size() + 1);
                all.add(a);
                all.addAll(bb);
                return Plus.of(all.toArray(new Expr[0]));
            } else {
                return Plus.of(a, b);
            }
        }
    }

    @Override
    public RepeatableOp<Expr> addRepeatableOp() {
        return new ExprAddRepeatableOp();
    }

    @Override
    public RepeatableOp<Expr> mulRepeatableOp() {
        return new ExprMulRepeatableOp();
    }

    @Override
    public Expr sub(Expr a, Expr b) {
        return Sub.of(a, b);
    }

    @Override
    public Expr mul(Expr a, Expr b) {
        return Mul.of(a,b);
    }

    @Override
    public Expr div(Expr a, Expr b) {
        return Div.of(a, b);
    }

    @Override
    public Expr rem(Expr a, Expr b) {
        return Reminder.of(a, b);
    }

    @Override
    public Expr real(Expr a) {
        switch (a.getType()) {
            case DOUBLE_EXPR: {
                return a;
            }
            case COMPLEX_EXPR: {
                if (a.getDomain().isUnbounded()) {
                    return ((a.toComplex().real()));
                }
                return a.getDomain().mul(a.toComplex().getReal());
            }
        }
        return new Real(a.toDC());
    }

    @Override
    public Expr imag(Expr a) {
        switch (a.getType()) {
            case DOUBLE_EXPR: {
                return Maths.ZERO;
            }
            case COMPLEX_EXPR: {
                if (a.getDomain().isUnbounded()) {
                    return ((a.toComplex().real()));
                }
                return a.getDomain().mul(a.toComplex().getReal());
            }
        }
        return new Imag(a.toDC());
    }

    @Override
    public Expr abs(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR: {
                double a = e.toDouble();
                if (a < 0) {
                    return Maths.expr(Math.abs(a), e.getDomain());
                }
                return e;
            }
            case COMPLEX_NBR: {
                return Maths.expr(((e.toComplex().abs())), e.getDomain());
            }
        }
        return Abs.of(e);
    }

    @Override
    public double absdbl(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR: {
                return Math.abs(e.toDouble());
            }
            case COMPLEX_NBR: {
                return Math.abs(e.toDouble());
            }
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public double absdblsqr(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR: {
                double d = e.toDouble();
                return d * d;
            }
            case COMPLEX_NBR: {
                return (e.toComplex().absdblsqr());
            }
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr abssqr(Expr a) {
        switch (a.getType()) {
            case DOUBLE_NBR: {
                return Maths.expr(Maths.abssqr(a.toDouble()), a.getDomain());
            }
            case COMPLEX_NBR: {
                return Maths.expr(a.toDouble(), a.getDomain());
            }
        }
        if (a instanceof CDiscrete) {
            return ((CDiscrete) a).abssqr();
        } else if (a instanceof VDiscrete) {
            return ((VDiscrete) a).abssqr();
        }
        throw new IllegalArgumentException("Not Supported Yet");
    }

    @Override
    public Expr neg(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR: {
                return Maths.expr(-(e.toDouble()), e.getDomain());
            }
            case COMPLEX_NBR: {
                return Maths.expr((e.toComplex().neg()), e.getDomain());
            }
        }
        return Neg.of(e);
    }

    @Override
    public Expr conj(Expr e) {
        switch (e.getType()) {
            case DOUBLE_EXPR:
                return Maths.expr(Maths.conj(e.toDouble()), e.getDomain());
            case COMPLEX_EXPR:
                return Maths.expr((e.toComplex().conj()), e.getDomain());
        }
        return Conj.of(e);
    }

    @Override
    public Expr inv(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
                return Maths.expr(Maths.inv(e.toDouble()), e.getDomain());
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
                return Maths.expr(e.toComplex().inv(), e.getDomain());
        }
        return Neg.of(e);
    }

    @Override
    public Expr sin(Expr e) {
        switch (e.getType()) {
            case DOUBLE_EXPR:
                return Maths.expr(Maths.sin(e.toDouble()), e.getDomain());
            case COMPLEX_EXPR:
                return Maths.expr(e.toComplex().sin(), e.getDomain());
        }
        return Sin.of(e);
    }

    @Override
    public Expr cos(Expr e) {
        switch (e.getType()) {
            case DOUBLE_EXPR:
                return Maths.expr(Maths.cos(e.toDouble()), e.getDomain());
            case COMPLEX_EXPR:
                return Maths.expr(e.toComplex().cos(), e.getDomain());
        }
        return Cos.of(e);
    }

    @Override
    public Expr tan(Expr e) {
        switch (e.getType()) {
            case DOUBLE_EXPR:
                return Maths.expr(Maths.tan(e.toDouble()), e.getDomain());
            case COMPLEX_EXPR:
                return Maths.expr(e.toComplex().tan(), e.getDomain());
        }
        return Tan.of(e);
    }

    @Override
    public Expr cotan(Expr e) {
        switch (e.getType()) {
            case DOUBLE_EXPR:
                return Maths.expr(Maths.cotan(e.toDouble()), e.getDomain());
            case COMPLEX_EXPR:
                return Maths.expr(e.toComplex().cotan(), e.getDomain());
        }
        return Cotan.of(e);
    }

    @Override
    public Expr sinh(Expr e) {
        switch (e.getType()) {
            case DOUBLE_EXPR:
                return Maths.expr(Maths.sinh(e.toDouble()), e.getDomain());
            case COMPLEX_EXPR:
                return Maths.expr(e.toComplex().sinh(), e.getDomain());
        }
        return Sinh.of(e);
    }

    @Override
    public Expr sincard(Expr e) {
        switch (e.getType()) {
            case DOUBLE_EXPR:
                return Maths.expr(Maths.sincard(e.toDouble()), e.getDomain());
            case COMPLEX_EXPR:
                return Maths.expr(e.toComplex().sincard(), e.getDomain());
        }
        return Sincard.of(e);
    }

    @Override
    public Expr cosh(Expr e) {
        switch (e.getType()) {
            case DOUBLE_EXPR:
                return Maths.expr(Maths.cosh(e.toDouble()), e.getDomain());
            case COMPLEX_EXPR:
                return Maths.expr(e.toComplex().cosh(), e.getDomain());
        }
        return Cosh.of(e);
    }

    @Override
    public Expr tanh(Expr e) {
        switch (e.getType()) {
            case DOUBLE_EXPR:
                return Maths.expr(Maths.tanh(e.toDouble()), e.getDomain());
            case COMPLEX_EXPR:
                return Maths.expr(e.toComplex().tanh(), e.getDomain());
        }
        return Tanh.of(e);
    }

    @Override
    public Expr cotanh(Expr e) {
        switch (e.getType()) {
            case DOUBLE_EXPR:
                return Maths.expr(Maths.cotanh(e.toDouble()), e.getDomain());
            case COMPLEX_EXPR:
                return Maths.expr(e.toComplex().cotanh(), e.getDomain());
        }
        return Cotanh.of(e);
    }

    @Override
    public Expr asinh(Expr e) {
        switch (e.getType()) {
            case DOUBLE_EXPR:
                return Maths.expr(Maths.asinh(e.toDouble()), e.getDomain());
            case COMPLEX_EXPR:
                return Maths.expr(e.toComplex().asinh(), e.getDomain());
        }
        return Asinh.of(e);
    }

    @Override
    public Expr acosh(Expr e) {
        switch (e.getType()) {
            case DOUBLE_EXPR:
                return Maths.expr(Maths.acosh(e.toDouble()), e.getDomain());
            case COMPLEX_EXPR:
                return Maths.expr(e.toComplex().acosh(), e.getDomain());
        }
        return Acosh.of(e);
    }

    @Override
    public Expr asin(Expr e) {
        if (e.isNarrow(ExprNumberType.DOUBLE_TYPE)) {
            return Maths.expr(Maths.asin(e.toDouble()), e.getDomain());
        }
        if (e.isNarrow(ExprNumberType.COMPLEX_TYPE)) {
            return Maths.expr(e.toComplex().asin(), e.getDomain());
        }
        return Asin.of(e);
    }

    @Override
    public Expr acos(Expr e) {
        switch (e.getType()) {
            case DOUBLE_EXPR:
                return Maths.expr(Maths.acos(e.toDouble()), e.getDomain());
            case COMPLEX_EXPR:
                return Maths.expr(e.toComplex().acos(), e.getDomain());
        }
        return Acos.of(e);
    }

    @Override
    public Expr atan(Expr e) {
        switch (e.getType()) {
            case DOUBLE_EXPR:
                return Maths.expr(Maths.atan(e.toDouble()), e.getDomain());
            case COMPLEX_EXPR:
                return Maths.expr(e.toComplex().atan(), e.getDomain());
        }
        return Atan.of(e);
    }

    @Override
    public Expr arg(Expr e) {
        switch (e.getType()) {
            case DOUBLE_EXPR:
                return Maths.expr(Maths.arg(e.toDouble()), e.getDomain());
            case COMPLEX_EXPR:
                return Maths.expr(e.toComplex().arg(), e.getDomain());
        }
        return Arg.of(e);
    }

    @Override
    public boolean isZero(Expr a) {
        return a.isZero();
    }

    @Override
    public <R> boolean is(Expr value, TypeName<R> type) {
        if (Maths.$EXPR.equals(type)) {
            return true;
        }
        if (Maths.$COMPLEX.equals(type)) {
            return value.isNarrow(ExprType.COMPLEX_NBR);
        }
        if (Maths.$DOUBLE.equals(type)) {
            return value.isNarrow(ExprType.DOUBLE_NBR);
        }
        if (Maths.$INTEGER.equals(type)) {
            return value.isNarrow(ExprType.DOUBLE_NBR) && value.toDouble() == (int) value.toDouble();
        }
        if (Maths.$LONG.equals(type)) {
            return value.isNarrow(ExprType.DOUBLE_NBR) && value.toDouble() == (long) value.toDouble();
        }
        return false;
    }

    @Override
    public boolean isComplex(Expr a) {
        return a.isNarrow(ExprType.COMPLEX_EXPR);
    }

    @Override
    public Complex toComplex(Expr a) {
        return a.toComplex();
    }

    @Override
    public Expr acotan(Expr e) {
        switch (e.getType()) {
            case DOUBLE_EXPR:
                return Maths.expr(Maths.acotan(e.toDouble()), e.getDomain());
            case COMPLEX_EXPR:
                return Maths.expr(e.toComplex().acotan(), e.getDomain());
        }
        return Acotan.of(e);
    }

    @Override
    public Expr exp(Expr e) {
        switch (e.getType()) {
            case DOUBLE_EXPR:
                return Maths.expr(Maths.exp(e.toDouble()), e.getDomain());
            case COMPLEX_EXPR:
                return Maths.expr(e.toComplex().exp(), e.getDomain());
        }
        return Exp.of(e);
    }

    @Override
    public Expr log(Expr e) {
        switch (e.getType()) {
            case DOUBLE_EXPR:
                return Maths.expr(Maths.log(e.toDouble()), e.getDomain());
            case COMPLEX_EXPR:
                return Maths.expr(e.toComplex().log(), e.getDomain());
        }
        return Log.of(e);
    }

    @Override
    public Expr log10(Expr e) {
        switch (e.getType()) {
            case DOUBLE_EXPR:
                return Maths.expr(Maths.log10(e.toDouble()), e.getDomain());
            case COMPLEX_EXPR:
                return Maths.expr(e.toComplex().log10(), e.getDomain());
        }
        return Log10.of(e);
    }

    @Override
    public Expr db(Expr e) {
        switch (e.getType()) {
            case DOUBLE_EXPR:
                return Maths.expr(Maths.db(e.toDouble()), e.getDomain());
            case COMPLEX_EXPR:
                return Maths.expr(e.toComplex().db(), e.getDomain());
        }
        return Db.of(e);
    }

    @Override
    public Expr db2(Expr e) {
        switch (e.getType()) {
            case DOUBLE_EXPR:
                return Maths.expr(Maths.db2(e.toDouble()), e.getDomain());
            case COMPLEX_EXPR:
                return Maths.expr(e.toComplex().db2(), e.getDomain());
        }
        return Db2.of(e);
    }

    @Override
    public Expr sqr(Expr e) {
        switch (e.getType()) {
            case DOUBLE_EXPR:
                return Maths.expr(Maths.sqr(e.toDouble()), e.getDomain());
            case COMPLEX_EXPR:
                return Maths.expr(e.toComplex().sqr(), e.getDomain());
        }
        if (e instanceof CDiscrete) {
            return ((CDiscrete) e).sqr();
        }
        if (e instanceof VDiscrete) {
            return ((VDiscrete) e).sqr();
        }
        return Sqr.of(e);
    }

    @Override
    public Expr sqrt(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case COMPLEX_NBR:
            case COMPLEX_EXPR: {
                Complex c = e.toComplex().sqrt();
                if(c.isReal()){
                    return Maths.expr(c.toDouble(), e.getDomain());
                }
                return Maths.expr(c.toComplex(), e.getDomain());
            }
        }
        if (e instanceof CDiscrete) {
            return MathsSampler.sqrt((CDiscrete) e);
        }
        if (e instanceof VDiscrete) {
            return MathsSampler.sqrt((VDiscrete) e);
        }
        return Sqrt.of(e);
    }

    @Override
    public Expr sqrt(Expr e, int n) {
        switch (e.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
            case COMPLEX_NBR:
            case COMPLEX_EXPR: {
                Complex c = e.toComplex().sqrt(n);
                if(c.isReal()){
                    return Maths.expr(c.toDouble(), e.getDomain());
                }
                return Maths.expr(c.toComplex(), e.getDomain());
            }
        }
        return Sqrtn.of(e, n);
    }

    @Override
    public Expr pow(Expr a, Expr b) {
        switch (a.getType()) {
            case DOUBLE_NBR:
            case DOUBLE_EXPR:
                switch (b.getType()) {
                    case DOUBLE_EXPR: {
                        Complex x = a.toComplex().pow(b.toComplex());
                        if(x.isReal()){
                            return Maths.expr(x.toDouble(), a.getDomain().intersect(b.getDomain()));
                        }
                        return Maths.expr(x, a.getDomain().intersect(b.getDomain()));
                    }
                    case COMPLEX_EXPR:
                        return (a.toComplex().pow(b.toComplex())).mul(a.getDomain().intersect(b.getDomain()));
                }
            case COMPLEX_NBR:
            case COMPLEX_EXPR:
                switch (b.getType()) {
                    case DOUBLE_NBR:
                    case DOUBLE_EXPR:
                    case COMPLEX_NBR:
                    case COMPLEX_EXPR:
                        return (a.toComplex().pow(b.toComplex())).mul(a.getDomain().intersect(b.getDomain()));
                }
        }
        return Pow.of(a, b);
    }

    @Override
    public Expr pow(Expr a, double b) {
        switch (a.getType()) {
            case DOUBLE_NBR: {
                Complex x = a.toComplex().pow(b);
                if(x.isReal()){
                    return Maths.expr(x.toDouble());
                }
                return x;
            }
            case DOUBLE_EXPR: {
                Complex x = a.toComplex().pow(b);
                if(x.isReal()){
                    return Maths.expr(x.toDouble(),a.getDomain());
                }
                return Maths.expr(x,a.getDomain());
            }
            case COMPLEX_NBR:
            case COMPLEX_EXPR: {
                return Maths.expr(a.toComplex().pow(b), a.getDomain());
            }
        }
        return Pow.of(a, Maths.expr(b));
    }

    @Override
    public Expr npow(Expr a, int b) {
        return pow(a,b);
//        switch (a.getType()) {
//            case DOUBLE_EXPR:
//                return Maths.expr(Maths.pow(a.toDouble(), b));
//            case COMPLEX_EXPR:
//                return Maths.expr(a.toComplex().pow(b), a.getDomain());
//        }
//        return Pow.of(a, Maths.expr(b));
    }

    @Override
    public Expr lt(Expr a, Expr b) {
        switch (a.getType()) {
            case DOUBLE_NBR: {
                switch (b.getType()) {
                    case DOUBLE_NBR: {
                        return Maths.expr(Double.compare(a.toDouble(), b.toDouble()) < 0 ? 1 : 0, a.getDomain().intersect(b.getDomain()));
                    }
                    case COMPLEX_NBR: {
                        return ((a.toComplex().compareTo(b.toComplex()))) < 0 ? one() : zero();
                    }
                }
            }
            case COMPLEX_NBR: {
                switch (b.getType()) {
                    case DOUBLE_NBR:
                    case COMPLEX_NBR: {
                        return ((a.toComplex().compareTo(b.toComplex()))) < 0 ? one() : zero();
                    }
                }
            }
        }
        return LtExpr.of(a.toDD(), b.toDD());
    }

    @Override
    public Expr lte(Expr a, Expr b) {
        switch (a.getType()) {
            case DOUBLE_NBR: {
                switch (b.getType()) {
                    case DOUBLE_NBR: {
                        return Maths.expr(Double.compare(a.toDouble(), b.toDouble()) <= 0 ? 1 : 0, a.getDomain().intersect(b.getDomain()));
                    }
                    case COMPLEX_NBR: {
                        return ((a.toComplex().compareTo(b.toComplex()))) <= 0 ? one() : zero();
                    }
                }
            }
            case COMPLEX_NBR: {
                switch (b.getType()) {
                    case DOUBLE_NBR:
                    case COMPLEX_NBR: {
                        return ((a.toComplex().compareTo(b.toComplex()))) <= 0 ? one() : zero();
                    }
                }
            }
        }
        return LteExpr.of(a.toDD(), b.toDD());
    }

    @Override
    public Expr gt(Expr a, Expr b) {
        switch (a.getType()) {
            case DOUBLE_NBR: {
                switch (b.getType()) {
                    case DOUBLE_NBR: {
                        return Maths.expr(Double.compare(a.toDouble(), b.toDouble()) < 0 ? 1 : 0, a.getDomain().intersect(b.getDomain()));
                    }
                    case COMPLEX_NBR: {
                        return ((a.toComplex().compareTo(b.toComplex()))) > 0 ? one() : zero();
                    }
                }
            }
            case COMPLEX_NBR: {
                switch (b.getType()) {
                    case DOUBLE_NBR:
                    case COMPLEX_NBR: {
                        return ((a.toComplex().compareTo(b.toComplex()))) > 0 ? one() : zero();
                    }
                }
            }
        }
        return GtExpr.of(a.toDD(), b.toDD());
    }

    @Override
    public Expr gte(Expr a, Expr b) {
        switch (a.getType()) {
            case DOUBLE_NBR: {
                switch (b.getType()) {
                    case DOUBLE_NBR: {
                        return Maths.expr(Double.compare(a.toDouble(), b.toDouble()) >= 0 ? 1 : 0, a.getDomain().intersect(b.getDomain()));
                    }
                    case COMPLEX_NBR: {
                        return ((a.toComplex().compareTo(b.toComplex()))) <= 0 ? one() : zero();
                    }
                }
            }
            case COMPLEX_NBR: {
                switch (b.getType()) {
                    case DOUBLE_NBR:
                    case COMPLEX_NBR: {
                        return ((a.toComplex().compareTo(b.toComplex()))) <= 0 ? one() : zero();
                    }
                }
            }
        }
        return GteExpr.of(a.toDD(), b.toDD());
    }

    @Override
    public Expr eq(Expr a, Expr b) {
        switch (a.getType()) {
            case DOUBLE_NBR: {
                switch (b.getType()) {
                    case DOUBLE_NBR: {
                        return Maths.expr(Double.compare(a.toDouble(), b.toDouble()) == 0 ? 1 : 0, a.getDomain().intersect(b.getDomain()));
                    }
                    case COMPLEX_NBR: {
                        return ((a.toComplex().compareTo(b.toComplex()))) == 0 ? one() : zero();
                    }
                }
            }
            case COMPLEX_NBR: {
                switch (b.getType()) {
                    case DOUBLE_NBR:
                    case COMPLEX_NBR: {
                        return ((a.toComplex().compareTo(b.toComplex()))) == 0 ? one() : zero();
                    }
                }
            }
        }
        return EqExpr.of(a.toDD(), b.toDD());
    }

    @Override
    public Expr ne(Expr a, Expr b) {
        switch (a.getType()) {
            case DOUBLE_NBR: {
                switch (b.getType()) {
                    case DOUBLE_NBR: {
                        return Maths.expr(Double.compare(a.toDouble(), b.toDouble()) != 0 ? 1 : 0, a.getDomain().intersect(b.getDomain()));
                    }
                    case COMPLEX_NBR: {
                        return ((a.toComplex().compareTo(b.toComplex()))) != 0 ? one() : zero();
                    }
                }
            }
            case COMPLEX_NBR: {
                switch (b.getType()) {
                    case DOUBLE_NBR:
                    case COMPLEX_NBR: {
                        return ((a.toComplex().compareTo(b.toComplex()))) != 0 ? one() : zero();
                    }
                }
            }
        }
        return NeExpr.of(a.toDD(), b.toDD());
    }

    @Override
    public Expr not(Expr e) {
        switch (e.getType()) {
            case DOUBLE_NBR: {
                return Maths.expr(e.toComplex().equals(Complex.ZERO) ? 1 : 0, e.getDomain());
            }
            case COMPLEX_NBR: {
                return Maths.expr(e.toComplex().equals(Complex.ZERO) ? 1 : 0, e.getDomain());
            }
        }
        return NotExpr.of(e);
    }

    @Override
    public Expr and(Expr a, Expr b) {
        switch (a.getType()) {
            case DOUBLE_NBR: {
                switch (b.getType()) {
                    case DOUBLE_NBR: {
                        return Maths.expr((a.toDouble() != 0 && b.toDouble() != 0) ? 1 : 0, a.getDomain().intersect(b.getDomain()));
                    }
                    case COMPLEX_NBR: {
                        return ((!a.toComplex().isZero() && !b.toComplex().isZero())) ? one() : zero();
                    }
                }
            }
            case COMPLEX_NBR: {
                switch (b.getType()) {
                    case DOUBLE_NBR:
                    case COMPLEX_NBR: {
                        return ((!a.toComplex().isZero() && !b.toComplex().isZero())) ? one() : zero();
                    }
                }
            }
        }
        return AndExpr.of(a.toDD(), b.toDD());
    }

    @Override
    public Expr or(Expr a, Expr b) {
        switch (a.getType()) {
            case DOUBLE_NBR: {
                switch (b.getType()) {
                    case DOUBLE_NBR: {
                        return Maths.expr((a.toDouble() != 0 || b.toDouble() != 0) ? 1 : 0, a.getDomain().intersect(b.getDomain()));
                    }
                    case COMPLEX_NBR: {
                        return Maths.expr((!a.toComplex().equals(Complex.ZERO) || !b.equals(Complex.ZERO)) ? 1 : 0, a.getDomain().intersect(b.getDomain()));
                    }
                }
            }
            case COMPLEX_NBR: {
                switch (b.getType()) {
                    case DOUBLE_NBR:
                    case COMPLEX_NBR: {
                        return Maths.expr((!a.toComplex().equals(Complex.ZERO) || !b.equals(Complex.ZERO)) ? 1 : 0, a.getDomain().intersect(b.getDomain()));
                    }
                }
            }
        }
        return OrExpr.of(a.toDD(), b.toDD());
    }

    @Override
    public IfExpr If(Expr cond, Expr exp1, Expr exp2) {
        return IfExpr.of(cond, exp1, exp2);
    }

    @Override
    public Expr parse(String string) {
        return Complex.of(string);
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
                    t.addAll(e2.getChildren());
                } else {
                    if (e2.isNarrow(ExprNumberType.COMPLEX_TYPE)) {
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
            return Plus.of(all.toArray(new Expr[0]));
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
                    t.addAll(e2.getChildren());
                } else {
                    if (e2.isNarrow(ExprNumberType.COMPLEX_TYPE)) {
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
            return Mul.of(all.toArray(new Expr[0]));
        }
    }
}
