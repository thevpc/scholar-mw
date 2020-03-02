package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.TaskMonitorManager;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface SParametersBuilder extends ValueBuilder{
    SParametersBuilder monitor(TaskMonitorManager monitor);
    SParametersBuilder monitor(ProgressMonitor monitor);
    SParametersBuilder converge(ConvergenceEvaluator convergenceEvaluator) ;

    ComplexMatrix evalMatrix();

    Complex evalComplex();

}
