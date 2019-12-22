package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruplot.console.ProgressTaskMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface ElectricFieldBuilder extends CartesianFieldBuilder {
    ElectricFieldBuilder monitor(ProgressTaskMonitor monitor);
    ElectricFieldBuilder monitor(ProgressMonitor monitor);

    ElectricFieldBuilder converge(ConvergenceEvaluator convergenceEvaluator);


}
