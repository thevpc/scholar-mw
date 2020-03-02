package net.vpc.scholar.hadruwaves.mom.builders;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.TaskMonitorManager;
import net.vpc.scholar.hadruwaves.builders.ValueBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface MomMatrixABuilder extends ValueBuilder {
    MomMatrixABuilder monitor(TaskMonitorManager monitor);

    MomMatrixABuilder monitor(ProgressMonitor monitor);

    MomMatrixABuilder converge(ConvergenceEvaluator convergenceEvaluator);

    ComplexMatrix evalMatrix();

}
