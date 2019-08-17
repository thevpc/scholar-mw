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
import java.util.Arrays;
import java.util.List;

/**
 * @author vpc
 */
public class Inv extends AbstractExprOperatorUnary implements Cloneable {
    private static final long serialVersionUID = 1L;

    private static Expressions.UnaryExprHelper<Inv> exprHelper = new InvUnaryExprHelper();

    static {
        ExpressionTransformFactory.setExpressionTransformer(Inv.class, ExpressionTransform.class, new ExpressionTransformer() {

            public Expr transform(Expr expression, ExpressionTransform transform) {
                Inv e = (Inv) expression;
                return new Inv(ExpressionTransformFactory.transform(e.expression, transform));
            }
        });
    }



    public Inv(Expr expression) {
        super(expression);
    }

    @Override
    protected Expressions.UnaryExprHelper getUnaryExprHelper() {
        return exprHelper;
    }


    protected boolean isZeroImpl() {
        if (expression.isInfinite()) {
            return true;
        }
        return false;
    }

    protected boolean isNaNImpl() {
        if (expression.isNaN()) {
            return true;
        }
        return false;
    }

    protected boolean isInfiniteImpl() {
        if (expression.isZero()) {
            return true;
        }
        return false;
    }


    public boolean isDDImpl() {
        return expression.isDD();
    }

    public boolean isDMImpl() {
        return expression.isDM();
    }


    public Expr getComponent(int row, int col) {
        if (isDM()) {
            if (isScalarExpr() && (row != col || col != 0)) {
                return FunctionFactory.DZEROXY;
            }
            return new Inv(expression.toDM().getComponent(row, col));
        } else {
            throw new ClassCastException();
        }
    }


    @Override
    public DoubleToDouble getRealDD() {
        Expr a = expression;
        if (a.isDC() && a.toDC().getImagDD().isZero()) {
            return this;
        }
        return new Real(toDC());
    }

    @Override
    public DoubleToDouble getImagDD() {
        Expr a = expression;
        if (a.isDC() && a.toDC().getImagDD().isZero()) {
            return FunctionFactory.DZEROXY;
        }
        return new Imag(toDC());
    }

    public Expr clone() {
        Inv cloned = (Inv) super.clone();
        cloned.expression = expression.clone();
        return cloned;
    }

    @Override
    public Complex toComplex() {
        return expression.toComplex().inv();
    }

    @Override
    public double toDouble() {
        return 1.0 / expression.toDouble();
    }

    @Override
    public Matrix toMatrix() {
        return expression.toMatrix().inv();
    }

    protected Expr newInstance(Expr e){
        return new Inv(e);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inv)) return false;

        Inv inv = (Inv) o;

        if (expression != null ? !expression.equals(inv.expression) : inv.expression != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = expression != null ? expression.hashCode() : 0;
        return result;
    }


    @Override
    public DoubleToComplex getComponent(Axis a) {
        switch (a) {
            case X: {
                return getComponent(0, 0).toDC();
            }
            case Y: {
                return getComponent(1, 0).toDC();
            }
            case Z: {
                return getComponent(2, 0).toDC();
            }
        }
        throw new IllegalArgumentException("Illegal axis");
    }


    @Override
    public Expr mul(Domain domain) {
        return new Inv(expression.mul(domain));
    }

    @Override
    public Expr mul(double other) {
        if (other == 0) {
            return Maths.DDZERO;
        }
        return new Inv(expression.mul(1 / other));
    }

    @Override
    public Expr mul(Complex other) {
        if (other.isZero()) {
            if (expression.isZero()) {
                return Maths.DDNAN;
            }
            return Maths.DDZERO;
        }
        return new Inv(expression.mul(other.inv()));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////


//    @Override
//    public Complex computeComplexArg(double x,BooleanMarker defined) {
//        Out<Range> ranges = new Out<>();
//        Complex complex = computeComplexArg(new double[]{x}, null, ranges)[0];
//        defined.set(ranges.get().getDefined1().get(0));
//        return complex;
//    }

    @Override
    public Complex computeComplex(double x, double y, double z, BooleanMarker defined) {
        DoubleToComplex c = expression.toDC();
        BooleanRef rdefined = BooleanMarker.ref();
        Complex cc = c.computeComplex(x, y, z, rdefined);
        if (!rdefined.get()) {
            return Complex.ZERO;
        }
        defined.set();
        return cc.inv();
    }

    @Override
    public Complex computeComplex(double x, double y, BooleanMarker defined) {
        DoubleToComplex c = expression.toDC();
        BooleanRef rdefined = BooleanMarker.ref();
        Complex cc = c.computeComplex(x, y, rdefined);
        if (!rdefined.get()) {
            return Complex.ZERO;
        }
        defined.set();
        return cc.inv();
    }

    @Override
    public Complex computeComplex(double x, BooleanMarker defined) {
        DoubleToComplex c = expression.toDC();
        BooleanRef rdefined = BooleanMarker.ref();
        Complex cc = c.computeComplex(x, rdefined);
        if (!rdefined.get()) {
            return Complex.ZERO;
        }
        defined.set();
        return cc.inv();
    }

    @Override
    public double computeDouble(double x, double y, double z, BooleanMarker defined) {
        DoubleToDouble doubleToDouble = expression.toDD();
        BooleanRef rdefined = BooleanMarker.ref();
        double v = doubleToDouble.computeDouble(x, y, z, rdefined);
        if (!rdefined.get()) {
            return 0;
        }
        defined.set();
        return 1 / v;
    }

    @Override
    public double computeDouble(double x, double y, BooleanMarker defined) {
        DoubleToDouble doubleToDouble = expression.toDD();
        BooleanRef rdefined = BooleanMarker.ref();
        double v = doubleToDouble.computeDouble(x, y, rdefined);
        if (!rdefined.get()) {
            return 0;
        }
        defined.set();
        return 1 / v;
    }

    @Override
    public double computeDouble(double x, BooleanMarker defined) {
        DoubleToDouble doubleToDouble = expression.toDD();
        BooleanRef rdefined = BooleanMarker.ref();
        double v = doubleToDouble.computeDouble(x, rdefined);
        if (!rdefined.get()) {
            return 0;
        }
        defined.set();
        return 1 / v;
    }


    @Override
    public Matrix computeMatrix(double x, double y, double z) {
        Matrix matrix = expression.toDM().computeMatrix(x, y, z);
        return matrix.inv();
    }

    private static class InvUnaryExprHelper implements Expressions.UnaryExprHelper<Inv>, Serializable {

        @Override
        public Expr getBaseExpr(Inv expr) {
            return expr.expression;
        }

        @Override
        public double computeDouble(double x, BooleanMarker defined) {
            defined.set();
            return 1 / x;
        }

        @Override
        public Complex computeComplex(Complex x, BooleanMarker defined) {
            defined.set();
            return x.inv();
        }

        @Override
        public Matrix computeMatrix(Matrix x) {
            return x.inv();
        }

        @Override
        public Vector computeVector(Vector x) {
            return x.inv();
        }
    }
}
