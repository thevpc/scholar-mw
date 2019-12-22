package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruplot.console.ProgressTaskMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface MagneticFieldBuilder extends CartesianFieldBuilder {
    MagneticFieldBuilder monitor(ProgressTaskMonitor monitor);
    MagneticFieldBuilder monitor(ProgressMonitor monitor);

    MagneticFieldBuilder converge(ConvergenceEvaluator convergenceEvaluator);


}
