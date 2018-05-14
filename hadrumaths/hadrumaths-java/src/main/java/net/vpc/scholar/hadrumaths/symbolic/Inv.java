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
public class Inv extends AbstractExprOperator implements Cloneable {
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

    private Expr expression;


    public Inv(Expr expression) {
        this.expression = expression;
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


//    public boolean isDDx() {
//        return expression.isDDx();
//    }

    public Expr getExpression() {
        return expression;
    }

    public boolean isDDImpl() {
        return expression.isDD();
    }

//    public DoubleToComplex toDC() {
//        if (!isDC()) {
//            throw new ClassCastException();
//        }
//        return this;
//    }

//    public DoubleToDouble toDD() {
//        if (!isDD()) {
//            throw new ClassCastException();
//        }
//        return this;
//    }
//
//    public IDDx toDDx() {
//        if (!isDDx()) {
//            throw new ClassCastException();
//        }
//        return this;
//    }

    public boolean isDMImpl() {
        return expression.isDM();
    }

    //    public DoubleToMatrix toDM() {
//        if (!isDM()) {
//            throw new ClassCastException();
//        }
//        return this;
//    }
//


//    @Override
//    public Domain getDomain() {
//        Domain d = Domain.NaNX;
//        if (isDDx()) {
//            return expression.toDDx().getDomain();
//        } else {
//            throw new ClassCastException();
//        }
//    }

    @Override
    public Domain getDomainImpl() {
        return expression.getDomain();
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return expression.getComponentDimension();
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
    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, exprHelper, x, d0, ranges);
    }

    @Override
    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, exprHelper, x, y, d0, ranges);
    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, exprHelper, x, y, z, d0, ranges);
    }

    @Override
    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
        return Expressions.computeDouble(this, exprHelper, x, d0, range);
    }

    @Override
    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, exprHelper, x, y, d0, ranges);
    }

    @Override
    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, exprHelper, x, y, z, d0, ranges);
    }

    @Override
    public Matrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, exprHelper, x, d0, ranges);
    }

    @Override
    public Matrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, exprHelper, x, y, d0, ranges);
    }


//    public Complex[] computeComplexArg(double[] x, double y, Domain d0, Out<Range> ranges) {
//        return Expressions.computeComplexArg(this, x, y, d0, ranges);
//    }
//
//    public Complex[] computeComplexArg(double x, double[] y, Domain d0, Out<Range> ranges) {
//        return Expressions.computeComplexArg(this, x, y, d0, ranges);
//    }
//
//    public Complex computeComplexArg(double x, double y) {
//        return Expressions.computeComplexArg(this, x, y);
//    }
//
//    public Matrix[] computeMatrix(double[] x, double y, Domain d0, Out<Range> ranges) {
//        return Expressions.computeMatrix(this, x, y, d0, ranges);
//    }
//
//    public Matrix[] computeMatrix(double x, double[] y, Domain d0, Out<Range> ranges) {
//        return Expressions.computeMatrix(this, x, y, d0, ranges);
//    }
//
//    public Matrix computeMatrix(double x, double y) {
//        return Expressions.computeMatrix(this, x, y);
//    }
//
//    public double[] computeDouble(double[] x, double y, Domain d0, Out<Range> ranges) {
//        return Expressions.computeDouble(this, x, y, d0, ranges);
//    }
//
//    public double[] computeDouble(double x, double[] y, Domain d0, Out<Range> ranges) {
//        return Expressions.computeDouble(this, x, y, d0, ranges);
//    }
//
//    public double computeDouble(double x, double y) {
//        return Expressions.computeDouble(this, x, y);
//    }
//
//    public double computeDouble(double x) {
//        return Expressions.computeDouble(this, x);
//    }

    @Override
    public Matrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, exprHelper, x, y, z, d0, ranges);
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

    public List<Expr> getSubExpressions() {
        return Arrays.asList(expression);
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

    @Override
    public Expr setParam(String name, Expr value) {
        Expr updated = expression.setParam(name, value);
        if (updated != expression) {
            Expr e = new Inv(updated);
            e = Any.copyProperties(this, e);
            return Any.updateTitleVars(e, name, value);
        }
        return this;
    }

    @Override
    public Expr composeX(Expr xreplacement) {
        Expr updated = expression.composeX(xreplacement);
        if (updated != expression) {
            Expr e = new Inv(updated);
            e = Any.copyProperties(this, e);
            return e;
        }
        return this;
    }

    @Override
    public Expr composeY(Expr yreplacement) {
        Expr updated = expression.composeY(yreplacement);
        if (updated != expression) {
            Expr e = new Inv(updated);
            e = Any.copyProperties(this, e);
            return e;
        }
        return this;
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
    public String getComponentTitle(int row, int col) {
        return Expressions.getMatrixExpressionTitleByChildren(this, row, col);
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
    public DoubleToVector toDV() {
        if (!isDV()) {
            throw new ClassCastException();
        }
        return this;
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

    @Override
    public int getDomainDimension() {
        return expression.getDomainDimension();
    }

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
        BooleanRef rdefined= BooleanMarker.ref();
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
        BooleanRef rdefined= BooleanMarker.ref();
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
        BooleanRef rdefined= BooleanMarker.ref();
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
        BooleanRef rdefined= BooleanMarker.ref();
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
        BooleanRef rdefined= BooleanMarker.ref();
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
        BooleanRef rdefined= BooleanMarker.ref();
        double v = doubleToDouble.computeDouble(x, rdefined);
        if (!rdefined.get()) {
            return 0;
        }
        defined.set();
        return 1 / v;
    }


    @Override
    public Matrix computeMatrix(double x, double y, double z) {
        BooleanRef rdefined= BooleanMarker.ref();
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
    }
}
