package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.ComponentDimension;
import net.vpc.scholar.hadrumaths.Maths;

import java.io.Serializable;

/**
 * Created by vpc on 8/19/14.
 */
public abstract class AbstractDoubleToComplex extends AbstractExprPropertyAware implements Serializable, DoubleToComplex{

    @Override
    public Complex[] computeComplex(double[] x) {
        return computeComplex(x,(Domain) null,null);
    }

    @Override
    public Complex[][] computeComplex(double[] x, double[] y) {
        return computeComplex(x,y,(Domain) null,null);
    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z) {
        return computeComplex(x,y,z,(Domain) null,null);
    }


    @Override
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }

    public boolean isDCImpl() {
        return true;
    }

    @Override
    public DoubleToComplex toDC() {
        return this;
    }

    @Override
    public boolean isDVImpl() {
        return true;
    }

    @Override
    public DoubleToVector toDV() {
        return Maths.vector(this);
    }

    public boolean isDMImpl() {
        return true;
    }

    public DoubleToMatrix toDM() {
        return toDV();
    }


    @Override
    public Complex[] computeComplex(double[] x, Domain d0) {
        return computeComplex(x,d0,null);
    }

    @Override
    public Complex[] computeComplex(double[] x, double y, Domain d0) {
        return computeComplex(x,y,d0,null);
    }

    @Override
    public Complex[] computeComplex(double x, double[] y, Domain d0) {
        return computeComplex(x,y,d0,null);
    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0) {
        return computeComplex(x,y,z,d0,null);
    }

    @Override
    public Complex[] computeComplex(double x, double[] y) {
        return computeComplex(x,y,(Domain)null,null);
    }

    @Override
    public Complex[] computeComplex(double[] x, double y) {
        return computeComplex(x,y,(Domain)null,null);
    }

    @Override
    public Complex[][] computeComplex(double[] x, double[] y, Domain d0) {
        return computeComplex(x,y,d0,null);
    }
}
