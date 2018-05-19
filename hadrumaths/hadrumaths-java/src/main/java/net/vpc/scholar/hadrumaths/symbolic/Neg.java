/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransform;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransformer;

import java.io.Serializable;

/**
 * @author vpc
 */
public class Neg extends AbstractUnaryExpOperator implements Cloneable {
    private static final long serialVersionUID = 1L;

    static {
        ExpressionTransformFactory.setExpressionTransformer(Neg.class, ExpressionTransform.class, new ExpressionTransformer() {

            public Expr transform(Expr expression, ExpressionTransform transform) {
                Neg e = (Neg) expression;
                return new Neg(ExpressionTransformFactory.transform(e.getExpression(), transform));
            }
        });
    }


    public Neg(Expr expression) {
        super(expression);
    }

    @Override
    protected Neg newInstance(Expr e) {
        return new Neg(e);
    }

    @Override
    public Expr mul(Domain domain) {
        return newInstance(getExpression().mul(domain));
    }

    @Override
    public Expr mul(double other) {
        if (other >= 0) {
            return newInstance(getExpression().mul(other));
        } else {
            return newInstance(getExpression().mul(-other));
        }
    }

    @Override
    public Expr mul(Complex other) {
        if (other.isReal()) {
            return mul(other.toDouble());
        }
        return super.mul(other);
    }

    @Override
    public Complex computeComplex(double x, double y, double z, BooleanMarker defined) {
        BooleanRef rdefined = BooleanMarker.ref();
        Complex complex = getExpression().toDC().computeComplex(x, y, z, rdefined);
        if (!rdefined.get()) {
            return Complex.ZERO;
        }
        defined.set();
        return complex.neg();
    }

    @Override
    public double computeDouble(double x, double y, double z, BooleanMarker defined) {
        BooleanRef rdefined = BooleanMarker.ref();
        double v = getExpression().toDD().computeDouble(x, y, z, rdefined);
        if (!rdefined.get()) {
            return 0;
        }
        defined.set();
        return -v;
    }

    @Override
    public Complex computeComplex(double x, double y, BooleanMarker defined) {
        BooleanRef rdefined = BooleanMarker.ref();
        Complex complex = getExpression().toDC().computeComplex(x, y, rdefined);
        if (!rdefined.get()) {
            return Complex.ZERO;
        }
        defined.set();
        return complex.neg();
    }

    @Override
    public double computeDouble(double x, double y, BooleanMarker defined) {
        BooleanRef rdefined = BooleanMarker.ref();
        double v = getExpression().toDD().computeDouble(x, y, rdefined);
        if (!rdefined.get()) {
            return 0;
        }
        defined.set();
        return -v;
    }

    @Override
    public Complex computeComplex(double x, BooleanMarker defined) {
        BooleanRef rdefined = BooleanMarker.ref();
        Complex complex = getExpression().toDC().computeComplex(x, rdefined);
        if (!rdefined.get()) {
            return Complex.ZERO;
        }
        defined.set();
        return complex.neg();
    }

    @Override
    public double computeDouble(double x, BooleanMarker defined) {
        BooleanRef rdefined = BooleanMarker.ref();
        double v = getExpression().toDD().computeDouble(x, rdefined);
        if (!rdefined.get()) {
            return 0;
        }
        defined.set();
        return -v;
    }

    @Override
    protected Expressions.UnaryExprHelper<? extends AbstractUnaryExpOperator> getExprHelper() {
        return exprHelper;
    }

    private static Expressions.UnaryExprHelper<Neg> exprHelper = new NegUnaryExprHelper();

    private static class NegUnaryExprHelper implements Expressions.UnaryExprHelper<Neg>, Serializable {

        @Override
        public Expr getBaseExpr(Neg expr) {
            return expr.getExpression();
        }

        @Override
        public double computeDouble(double x, BooleanMarker defined) {
            defined.set();
            return -x;
        }

        @Override
        public Complex computeComplex(Complex x, BooleanMarker defined) {
            defined.set();
            return x.neg();
        }

        @Override
        public Matrix computeMatrix(Matrix x) {
            //defined.set();
            return x.neg();
        }
    }
}
