package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.ComplexVector;
import net.vpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.vpc.scholar.hadrumaths.Samples;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface CartesianFieldBuilder extends ValueBuilder {
    CartesianFieldBuilder monitor(ProgressMonitorFactory monitor);

    CartesianFieldBuilder monitor(ProgressMonitor monitor);

    CartesianFieldBuilder converge(ConvergenceEvaluator convergenceEvaluator);

    ComplexMatrix evalMatrix(Axis axis, double[] x, double[] y, double z);

    ComplexMatrix evalMatrix(Axis axis, double[] x, double y, double[] z);

    ComplexMatrix evalMatrix(Axis axis, double x, double[] y, double[] z);

    VDiscrete evalVDiscrete(Samples samples);

    VDiscrete evalVDiscrete(double[] x, double[] y, double[] z);

    ComplexVector evalVector(Axis axis, double[] x, double y, double z);

    ComplexVector evalVector(Axis axis, double x, double[] y, double z);

    ComplexVector evalVector(Axis axis, double x, double y, double[] z);

    ComplexMatrix evalMatrix(Axis axis, Samples samples);

    ComplexVector evalVector(Axis axis, Samples samples);

    VDiscrete evalVDiscrete(double[] x, double[] y);

    Expr expr();
}
