package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.TaskMonitorManager;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface FarFieldBuilder extends ValueBuilder {
    FarFieldBuilder monitor(TaskMonitorManager monitor);
    FarFieldBuilder monitor(ProgressMonitor monitor);

    FarFieldBuilder converge(ConvergenceEvaluator convergenceEvaluator);

    ComplexMatrix evalMatrix(SAxis axis, double[] theta, double[] phi, double r);

    Vector<ComplexMatrix> evalMatrices(double[] theta, double[] phi, double r);
}
