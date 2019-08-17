package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.Vector;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadruplot.Samples;
import net.vpc.scholar.hadruplot.console.ProgressTaskMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface CartesianFieldBuilder extends ValueBuilder {
    CartesianFieldBuilder monitor(ProgressTaskMonitor monitor);

    CartesianFieldBuilder monitor(ProgressMonitor monitor);

    CartesianFieldBuilder converge(ConvergenceEvaluator convergenceEvaluator);

    Matrix computeMatrix(Axis axis, double[] x, double[] y, double z);

    Matrix computeMatrix(Axis axis, double[] x, double y, double[] z);

    Matrix computeMatrix(Axis axis, double x, double[] y, double[] z);

    VDiscrete computeVDiscrete(Samples samples);

    VDiscrete computeVDiscrete(double[] x, double[] y, double[] z);

    Vector computeVector(Axis axis, double[] x, double y, double z);

    Vector computeVector(Axis axis, double x, double[] y, double z);

    Vector computeVector(Axis axis, double x, double y, double[] z);

    Matrix computeMatrix(Axis axis, Samples samples);

    Vector computeVector(Axis axis, Samples samples);

    VDiscrete computeVDiscrete(double[] x, double[] y);

    Expr expr();
}
