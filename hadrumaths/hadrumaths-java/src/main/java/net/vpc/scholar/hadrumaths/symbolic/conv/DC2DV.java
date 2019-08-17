package net.vpc.scholar.hadrumaths.symbolic.conv;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.AdaptedExpr;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.Range;

public class DC2DV extends AdaptedExpr implements DoubleToVector {
    private DC2DVHelper dc2dv;

    public DC2DV(DoubleToComplex base) {
        super(base);
        this.dc2dv = new DC2DVHelper(base);
    }
    @Override
    protected Expr newInstance(Expr base) {
        return new DC2DV((DoubleToComplex)base);
    }



    //DC2DM START
    @Override
    public Vector[][][] computeVector(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return dc2dv.computeVector(x,y,z,d0,ranges);
    }

    @Override
    public Vector[][] computeVector(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return dc2dv.computeVector(x,y,d0,ranges);
    }

    @Override
    public Vector[] computeVector(double[] x, Domain d0, Out<Range> ranges) {
        return dc2dv.computeVector(x,d0,ranges);
    }

    @Override
    public Expr getComponent(Axis a) {
        return dc2dv.getComponent(a);
    }

    @Override
    public int getComponentSize() {
        return dc2dv.getComponentSize();
    }

    @Override
    public Vector computeVector(double x, double y, double z, BooleanMarker defined) {
        return dc2dv.computeVector(x,y,z,defined);
    }

    @Override
    public Vector computeVector(double x, double y, BooleanMarker defined) {
        return dc2dv.computeVector(x,y,defined);
    }

    @Override
    public Vector computeVector(double x, BooleanMarker defined) {
        return dc2dv.computeVector(x,defined);
    }

    //DC2DM END

}
