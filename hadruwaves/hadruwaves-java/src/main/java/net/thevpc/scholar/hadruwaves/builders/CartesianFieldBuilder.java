package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitorFactory;
import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.ComplexVector;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.scholar.hadrumaths.Samples;

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
