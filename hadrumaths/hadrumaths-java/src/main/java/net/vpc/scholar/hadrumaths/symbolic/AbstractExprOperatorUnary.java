package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class AbstractExprOperatorUnary extends AbstractExprOperator {
    protected Expr expression;

    public AbstractExprOperatorUnary(Expr expression) {
        this.expression = expression;
    }

    protected abstract Expressions.UnaryExprHelper getUnaryExprHelper();

    protected abstract Expr newInstance(Expr e);

    public Expr getExpression() {
        return expression;
    }

    @Override
    public Domain getDomainImpl() {
        return expression.getDomain();
    }

    @Override
    public int getComponentSize() {
        return expression.toDV().getComponentSize();
    }

    @Override
    public String getComponentTitle(int row, int col) {
        return Expressions.getMatrixExpressionTitleByChildren(this, row, col);
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

    public List<Expr> getSubExpressions() {
        return Arrays.asList(expression);
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return expression.getComponentDimension();
    }

    @Override
    public Expr setParam(String name, Expr value) {
        Expr updated = expression.setParam(name, value);
        if (updated != expression) {
            Expr e = newInstance(updated);
            e = Any.copyProperties(this, e);
            return Any.updateTitleVars(e, name, value);
        }
        return this;
    }

    @Override
    public Expr compose(Axis axis, Expr xreplacement) {
        Expr updated = expression.compose(axis, xreplacement);
        if (updated != expression) {
            Expr e = newInstance(updated);
            e = Any.copyProperties(this, e);
            return e;
        }
        return this;
    }


    @Override
    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, getUnaryExprHelper(), x, d0, ranges);
    }

    @Override
    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, getUnaryExprHelper(), x, y, d0, ranges);
    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, getUnaryExprHelper(), x, y, z, d0, ranges);
    }

    @Override
    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
        return Expressions.computeDouble(this, getUnaryExprHelper(), x, d0, range);
    }

    @Override
    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, getUnaryExprHelper(), x, y, d0, ranges);
    }

    @Override
    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, getUnaryExprHelper(), x, y, z, d0, ranges);
    }

    @Override
    public ComplexMatrix[] computeMatrix(double[] x, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, getUnaryExprHelper(), x, d0, ranges);
    }

    @Override
    public ComplexVector[] computeVector(double[] x, Domain d0, Out<Range> ranges) {
        return Expressions.computeVector(this, getUnaryExprHelper(), x, d0, ranges);
    }

    @Override
    public ComplexMatrix[][] computeMatrix(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, getUnaryExprHelper(), x, y, d0, ranges);
    }


    @Override
    public ComplexVector[][] computeVector(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeVector(this, getUnaryExprHelper(), x, y, d0, ranges);
    }


    @Override
    public ComplexVector computeVector(double x, double y, double z, BooleanMarker defined) {
        return Expressions.computeVector(this, x, y, z, defined);
    }

    @Override
    public ComplexVector computeVector(double x, double y, BooleanMarker defined) {
        return Expressions.computeVector(this, x, y, defined);
    }

    @Override
    public ComplexVector computeVector(double x, BooleanMarker defined) {
        return Expressions.computeVector(this, x, defined);
    }

    @Override
    public ComplexMatrix[][][] computeMatrix(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, getUnaryExprHelper(), x, y, z, d0, ranges);
    }

    @Override
    public ComplexVector[][][] computeVector(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return Expressions.computeVector(this, getUnaryExprHelper(), x, y, z, d0, ranges);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AbstractExprOperatorUnary that = (AbstractExprOperatorUnary) o;
        return Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), expression);
    }
}
