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
 *
 * @author vpc
 */
public abstract class AbstractUnaryExpOperator extends AbstractExprOperator implements Cloneable {

    private Expr expression;

    public AbstractUnaryExpOperator(Expr expression) {
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
            return newInstance(expression.toDM().getComponent(row, col));
        } else {
            throw new ClassCastException();
        }
    }

    protected abstract AbstractUnaryExpOperator newInstance(Expr e);
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
        AbstractUnaryExpOperator cloned = (AbstractUnaryExpOperator) super.clone();
        cloned.expression = expression.clone();
        return cloned;
    }

    public List<Expr> getSubExpressions() {
        return Arrays.asList(expression);
    }

    @Override
    public Expr setParam(String name, Expr value) {
        Expr updated = expression.setParam(name, value);
        if(updated!=expression) {
            Expr e = newInstance(updated);
            e=copyProperties(this, e);
            return AbstractExprPropertyAware.updateNameVars(e,name,value);
        }
        return this;
    }

    @Override
    public Expr composeX(Expr xreplacement) {
        Expr updated = expression.composeX(xreplacement);
        if(updated!=expression) {
            Expr e = newInstance(updated);
            e=copyProperties(this, e);
            return e;
        }
        return this;
    }

    @Override
    public Expr composeY(Expr yreplacement) {
        Expr updated = expression.composeY(yreplacement);
        if(updated!=expression) {
            Expr e = newInstance(updated);
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
        if (!(o instanceof AbstractUnaryExpOperator)) return false;

        AbstractUnaryExpOperator neg = (AbstractUnaryExpOperator) o;

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
        return Expressions.computeComplex(this, getExprHelper(), x, d0, ranges) ;
    }
    @Override
    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, getExprHelper(), x, y, d0, ranges) ;
    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, getExprHelper(), x, y, z, d0, ranges);
    }

    @Override
    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
        return Expressions.computeDouble(this, getExprHelper(), x, d0, range) ;
    }
    @Override
    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, getExprHelper(), x, y, d0, ranges) ;
    }

    @Override
    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, getExprHelper(), x, y, z, d0, ranges) ;
    }

    @Override
    public Matrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, getExprHelper(), x, d0, ranges) ;
    }
    @Override
    public Matrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, getExprHelper(), x, y, d0, ranges) ;
    }

    @Override
    public Matrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, getExprHelper(), x, y, z, d0, ranges) ;
    }

    @Override
    public Complex toComplex() {
        return getExprHelper().computeComplex(getExpression().toComplex());
    }

    @Override
    public double toDouble() {
        return getExprHelper().computeDouble(getExpression().toDouble());
    }

    @Override
    public Matrix toMatrix() {
        return getExprHelper().computeMatrix(getExpression().toMatrix());
    }


    protected abstract Expressions.UnaryExprHelper<? extends AbstractUnaryExpOperator> getExprHelper();


}
