/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.FunctionFactory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransform;
import net.vpc.scholar.hadrumaths.ExpressionTransformFactory;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransformer;

/**
 *
 * @author vpc
 */
public class Neg extends AbstractExprOperator implements Cloneable {

    static {
        ExpressionTransformFactory.setExpressionTransformer(Neg.class, ExpressionTransform.class, new ExpressionTransformer() {

            public Expr transform(Expr expression, ExpressionTransform transform) {
                Neg e = (Neg) expression;
                return new Neg(ExpressionTransformFactory.transform(e.expression, transform));
            }
        });
    }

    private Expr expression;

    public Neg(Expr expression) {
        this.expression = expression;
    }

    public boolean isZeroImpl() {
        if (!expression.isZero()) {
            return false;
        }
        return true;
    }

    public Expr getExpression() {
        return expression;
    }


//    public boolean isDDx() {
//        return expression.isDDx();
//    }
//
//    public boolean isDD() {
//        return expression.isDC();
//    }



    @Override
    public Domain getDomainImpl() {
        return expression.getDomain();
    }

//    @Override
//    public Domain getDomain() {
////        Domain d = Domain.NaNX;
//        if (isDDx()) {
//            return expression.toDDx().getDomain();
//        } else {
//            throw new ClassCastException();
//        }
//    }

    @Override
    public ComponentDimension getComponentDimension() {
        return expression.getComponentDimension();
    }

    public Expr getComponent(int row, int col) {
        if (isDM()) {
            if (isScalarExpr() && (row != col || col != 0)) {
                return FunctionFactory.DZEROXY;
            }
            return new Neg(expression.toDM().getComponent(row, col));
        } else {
            throw new ClassCastException();
        }
    }

//    public Complex[] computeComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
//        return Expressions.computeComplex(this, x, y, d0, ranges);
//    }
//
//    public Complex[] computeComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
//        return Expressions.computeComplex(this, x, y, d0, ranges);
//    }
//
//    public Complex computeComplex(double x, double y) {
//        return Expressions.computeComplex(this, x, y);
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

    public double computeDouble(double x) {
        return Expressions.computeDouble(this, x);
    }

    public Expr clone() {
        Neg cloned = (Neg) super.clone();
        cloned.expression = expression.clone();
        return cloned;
    }

    public List<Expr> getSubExpressions() {
        return Arrays.asList(expression);
    }

    @Override
    public Complex toComplex() {
        return expression.toComplex().neg();
    }

    @Override
    public double toDouble() {
        return -expression.toDouble();
    }

    @Override
    public Matrix toMatrix() {
        return expression.toMatrix().neg();
    }

    @Override
    public Expr setParam(String name, Expr value) {
        Expr updated = expression.setParam(name, value);
        if(updated!=expression) {
            Expr e = new Neg(updated);
            e=copyProperties(this, e);
            return AbstractExprPropertyAware.updateNameVars(e,name,value);
        }
        return this;
    }

    @Override
    public Expr composeX(Expr xreplacement) {
        Expr updated = expression.composeX(xreplacement);
        if(updated!=expression) {
            Expr e = new Neg(updated);
            e=copyProperties(this, e);
            return e;
        }
        return this;
    }

    @Override
    public Expr composeY(Expr yreplacement) {
        Expr updated = expression.composeY(yreplacement);
        if(updated!=expression) {
            Expr e = new Neg(updated);
            e=copyProperties(this, e);
            return e;
        }
        return this;
    }

    @Override
    public String getComponentTitle(int row, int col) {
        return Expressions.getMatrixExpressionTitleByChildren(this, row, col);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Neg)) return false;

        Neg neg = (Neg) o;

        if (expression != null ? !expression.equals(neg.expression) : neg.expression != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return expression != null ? expression.hashCode() : 0;
    }


    @Override
    public DoubleToComplex getComponent(Axis a) {
        switch (a){
            case X:{
                return getComponent(0, 0).toDC();
            }
            case Y:{
                return getComponent(1, 0).toDC();
            }
            case Z:{
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
    public int getDomainDimension() {
        return expression.getDomainDimension();
    }

    @Override
    public Complex computeComplex(double x, double y, double z) {
        return expression.toDC().computeComplex(x,y,z).neg();
    }

    @Override
    public double computeDouble(double x, double y, double z) {
        return -expression.toDD().computeDouble(x,y,z);
    }

    @Override
    public Matrix computeMatrix(double x, double y, double z) {
        return expression.toDM().computeMatrix(x,y,z).neg();
    }

    @Override
    public Complex computeComplex(double x) {
        return computeComplex(new double[]{x})[0];
    }

    public DoubleToDouble getReal() {
        if(expression.isDD()){
            return this;
        }
        return new Real(toDC());
    }

    public DoubleToDouble getImag() {
        if(expression.isDD()){
            return FunctionFactory.DZERO(getDomainDimension());
        }
        return new Imag(toDC());
    }


    @Override
    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, exprHelper, x, d0, ranges) ;
    }
    @Override
    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, exprHelper, x, y, d0, ranges) ;
    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, exprHelper, x, y, z, d0, ranges);
    }

    @Override
    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
        return Expressions.computeDouble(this, exprHelper, x, d0, range) ;
    }
    @Override
    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, exprHelper, x, y, d0, ranges) ;
    }

    @Override
    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, exprHelper, x, y, z, d0, ranges) ;
    }

    @Override
    public Matrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, exprHelper, x, d0, ranges) ;
    }
    @Override
    public Matrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, exprHelper, x, y, d0, ranges) ;
    }

    @Override
    public Matrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, exprHelper, x, y, z, d0, ranges) ;
    }

    private static Expressions.UnaryExprHelper<Neg> exprHelper = new NegUnaryExprHelper();

    private static class NegUnaryExprHelper implements Expressions.UnaryExprHelper<Neg> , Serializable{

        @Override
        public Expr getBaseExpr(Neg expr) {
            return expr.expression;
        }

        @Override
        public double computeDouble(double x) {
            return - x;
        }

        @Override
        public Complex computeComplex(Complex x) {
            return x.neg();
        }

        @Override
        public Matrix computeMatrix(Matrix x) {
            return x.neg();
        }
    }
}
