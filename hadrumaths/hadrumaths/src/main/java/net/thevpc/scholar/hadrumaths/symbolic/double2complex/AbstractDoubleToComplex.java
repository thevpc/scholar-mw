package net.thevpc.scholar.hadrumaths.symbolic.double2complex;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.ComponentDimension;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.util.internal.IgnoreRandomGeneration;
import net.thevpc.scholar.hadrumaths.symbolic.*;
import net.thevpc.scholar.hadrumaths.symbolic.double2matrix.DefaultDoubleToMatrix;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.DefaultDoubleToVector;

/**
 * Created by vpc on 8/19/14.
 */
@IgnoreRandomGeneration
public abstract class AbstractDoubleToComplex implements DoubleToComplex {
    private static final long serialVersionUID = 1L;

    @Override
    public Complex[] evalComplex(double[] x) {
        return evalComplex(x, (Domain) null, null);
    }

    @Override
    public Complex[][] evalComplex(double[] x, double[] y) {
        return evalComplex(x, y, (Domain) null, null);
    }

    @Override
    public Complex[][][] evalComplex(double[] x, double[] y, double[] z) {
        return evalComplex(x, y, z, null, null);
    }

    @Override
    public DoubleToComplex toDC() {
        return this;
    }

//    public boolean isDCImpl() {
//        return true;
//    }

    @Override
    public DoubleToVector toDV() {
        return DefaultDoubleToVector.of(this);
    }

//    @Override
//    public boolean isDVImpl() {
//        return true;
//    }

    public DoubleToMatrix toDM() {
        return DefaultDoubleToMatrix.of(this);
    }

//    public boolean isDMImpl() {
//        return true;
//    }

    @Override
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }

}
