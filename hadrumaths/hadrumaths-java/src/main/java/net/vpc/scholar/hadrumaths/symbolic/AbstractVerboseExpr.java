package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

/**
 * Created by vpc on 8/24/14.
 */
public abstract class AbstractVerboseExpr extends AbstractExprPropertyAware implements DoubleToDouble, DoubleToComplex, DoubleToVector, DoubleToMatrix {
    private static final long serialVersionUID = 1L;

    @Override
    public Complex[] computeComplex(double[] x, Domain d0) {
        return computeComplex(x, d0, null);
    }

    @Override
    public Complex[] computeComplex(double[] x, double y, Domain d0) {
        return computeComplex(x, y, d0, null);
    }

    @Override
    public Complex[] computeComplex(double x, double[] y, Domain d0) {
        return computeComplex(x, y, d0, null);
    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0) {
        return computeComplex(x, y, z, d0, null);
    }

    @Override
    public Complex[] computeComplex(double x, double[] y) {
        return computeComplex(x, y, (Domain) null, null);
    }

    @Override
    public Complex[] computeComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, x, y, d0, ranges);
    }

    @Override
    public Complex[] computeComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, x, y, d0, ranges);
    }

//    @Override
//    public Complex computeComplex(double x, double y) {
//        return Expressions.computeComplex(this, x, y);
//    }

//    @Override
//    public double computeDouble(double x) {
//        return Expressions.computeDouble(this, x);
//    }

    @Override
    public double[] computeDouble(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, x, y, d0, ranges);
    }

    @Override
    public double[] computeDouble(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, x, y, d0, ranges);
    }

//    @Override
//    public double computeDouble(double x, double y) {
//        return Expressions.computeDouble(this, x, y);
//    }


    @Override
    public Matrix[] computeMatrix(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, x, y, d0, ranges);
    }

    @Override
    public Matrix[] computeMatrix(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, x, y, d0, ranges);
    }

    @Override
    public Matrix computeMatrix(double x, double y) {
        return Expressions.computeMatrix(this, x, y);
    }

    @Override
    public int getComponentSize() {
        if (isDV()) {
            ComponentDimension d = toDM().getComponentDimension();
            return d.rows;
        }
        throw new ClassCastException();
    }

//    @Override
//    public Complex computeComplexArg(double x,BooleanMarker defined) {
//        Out<Range> ranges = new Out<>();
//        Complex complex = computeComplexArg(new double[]{x}, null, ranges)[0];
//        defined.set(ranges.get().getDefined1().get(0));
//        return complex;
//    }

//    @Override
//    public Complex computeComplex(double x, double y, double z) {
//        return computeComplex(new double[]{x}, new double[]{y}, new double[]{z}, null, null)[0][0][0];
//    }

//    @Override
//    public double computeDouble(double x, double y, double z) {
//        return computeDouble(new double[]{x}, new double[]{y}, new double[]{z}, null, null)[0][0][0];
//    }

    @Override
    public Matrix computeMatrix(double x) {
        return computeMatrix(new double[]{x}, (Domain) null, null)[0];
    }

    @Override
    public Matrix computeMatrix(double x, double y, double z) {
        return computeMatrix(new double[]{x}, new double[]{y}, new double[]{z}, null, null)[0][0][0];
    }

    @Override
    public double[] computeDouble(double x, double[] y) {
        return computeDouble(x, y, (Domain) null, null);
    }


    @Override
    public Matrix[] computeMatrix(double[] x) {
        return computeMatrix(x, (Domain) null, null);
    }

    @Override
    public Matrix[][] computeMatrix(double[] x, double[] y) {
        return computeMatrix(x, y, (Domain) null, null);
    }

    @Override
    public Matrix[][][] computeMatrix(double[] x, double[] y, double[] z) {
        return computeMatrix(x, y, z, (Domain) null, null);
    }

    @Override
    public double[] computeDouble(double[] x) {
        return computeDouble(x, (Domain) null, null);
    }

    @Override
    public double[][] computeDouble(double[] x, double[] y) {
        return computeDouble(x, y, (Domain) null, null);
    }

    @Override
    public double[][][] computeDouble(double[] x, double[] y, double[] z) {
        return computeDouble(x, y, z, (Domain) null, null);
    }

    @Override
    public Complex[] computeComplex(double[] x) {
        return computeComplex(x, (Domain) null, null);
    }

    @Override
    public Complex[][] computeComplex(double[] x, double[] y) {
        return computeComplex(x, y, (Domain) null, null);
    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z) {
        return computeComplex(x, y, z, (Domain) null, null);
    }

    @Override
    public Complex[][] computeComplex(double[] x, double[] y, Domain d0) {
        return computeComplex(x, y, d0, null);
    }

    @Override
    public Complex[] computeComplex(double[] x, double y) {
        return computeComplex(x, y, (Domain) null, null);
    }

    @Override
    public Expr getX() {
        return getComponent(Axis.X);
    }

    @Override
    public Expr getY() {
        return getComponent(Axis.Y);
    }

    @Override
    public Expr getZ() {
        return getComponent(Axis.Z);
    }

    @Override
    public boolean contains(double x) {
        return getDomain().contains(x);
    }

    @Override
    public boolean contains(double x, double y) {
        return getDomain().contains(x, y);
    }

    @Override
    public boolean contains(double x, double y, double z) {
        return getDomain().contains(x, y, z);
    }
}
