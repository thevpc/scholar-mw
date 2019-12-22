package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

/**
 * Created by vpc on 8/24/14.
 */
public abstract class AbstractPolymorphExpr extends AbstractExprPropertyAware
        implements DoubleToDouble, DoubleToComplex, DoubleToVector, DoubleToMatrix {
    private static final long serialVersionUID = 1L;



    @Override
    public ComplexVector computeVector(double x, double y, double z, BooleanMarker defined) {
        return Expressions.computeVector(this,x,y,z,defined);
    }

    @Override
    public ComplexVector computeVector(double x, double y, BooleanMarker defined) {
        return Expressions.computeVector(this,x,y,defined);
    }

    @Override
    public ComplexVector computeVector(double x, BooleanMarker defined) {
        return Expressions.computeVector(this,x,defined);
    }

//
//    @Override
//    public int getComponentSize() {
//        if (isDV()) {
//            ComponentDimension d = getComponentDimension();
//            return d.rows;
//        }
//        throw new ClassCastException();
//    }
//
//    @Override
//    public Matrix computeMatrix(double x) {
//        return computeMatrix(new double[]{x}, (Domain) null, null)[0];
//    }
//
//    @Override
//    public Matrix computeMatrix(double x, double y, double z) {
//        return computeMatrix(new double[]{x}, new double[]{y}, new double[]{z}, null, null)[0][0][0];
//    }
//
//    @Override
//    public double[] computeDouble(double x, double[] y) {
//        return computeDouble(x, y, (Domain) null, null);
//    }
//
//
//    @Override
//    public Matrix[] computeMatrix(double[] x) {
//        return computeMatrix(x, (Domain) null, null);
//    }
//
//    @Override
//    public Matrix[][] computeMatrix(double[] x, double[] y) {
//        return computeMatrix(x, y, (Domain) null, null);
//    }
//
//    @Override
//    public Matrix[][][] computeMatrix(double[] x, double[] y, double[] z) {
//        return computeMatrix(x, y, z, (Domain) null, null);
//    }
//
//    @Override
//    public double[] computeDouble(double[] x) {
//        return computeDouble(x, (Domain) null, null);
//    }
//
//    @Override
//    public double[][] computeDouble(double[] x, double[] y) {
//        return computeDouble(x, y, (Domain) null, null);
//    }
//
//    @Override
//    public double[][][] computeDouble(double[] x, double[] y, double[] z) {
//        return computeDouble(x, y, z, (Domain) null, null);
//    }
//
//    @Override
//    public boolean contains(double x) {
//        return getDomain().contains(x);
//    }
//
//    @Override
//    public boolean contains(double x, double y) {
//        return getDomain().contains(x, y);
//    }
//
//    @Override
//    public boolean contains(double x, double y, double z) {
//        return getDomain().contains(x, y, z);
//    }
}
