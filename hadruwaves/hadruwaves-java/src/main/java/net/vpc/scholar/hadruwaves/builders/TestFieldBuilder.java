package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.Samples;
import net.vpc.scholar.hadrumaths.Vector;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface TestFieldBuilder extends ValueBuilder {
    public TestFieldBuilder monitor(ComputationMonitor monitor);

    public TestFieldBuilder converge(ConvergenceEvaluator convergenceEvaluator);

    Matrix computeMatrix(Axis axis, double[] x, double[] y, double z);

    VDiscrete computeVDiscrete(Samples samples);

    VDiscrete computeVDiscrete(double[] x, double[] y);

    Vector computeVector(Axis axis, double[] x, double y, double z);

    Vector computeVector(Axis axis, double x, double[] y, double z);

    Vector computeVector(Axis axis, double x, double y, double[] z);

    Matrix computeMatrix(final Axis axis, Samples samples);

    Vector computeVector(final Axis axis, Samples samples);

}
