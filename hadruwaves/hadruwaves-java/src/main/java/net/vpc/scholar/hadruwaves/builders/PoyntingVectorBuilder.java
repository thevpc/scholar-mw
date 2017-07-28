package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface PoyntingVectorBuilder extends ValueBuilder{
    public PoyntingVectorBuilder monitor(ComputationMonitor monitor);
    public PoyntingVectorBuilder converge(ConvergenceEvaluator convergenceEvaluator) ;

    Matrix computeMatrix(Axis axis, double[] x, double[] y, double z);

    VDiscrete computeVDiscrete(Samples samples);

    VDiscrete computeVDiscrete(double[] x, double[] y, double[] z);

    Vector computeVector(Axis axis, double[] x, double y, double z);

    Vector computeVector(Axis axis, double x, double[] y, double z);

    Vector computeVector(Axis axis, double x, double y, double[] z);

}
