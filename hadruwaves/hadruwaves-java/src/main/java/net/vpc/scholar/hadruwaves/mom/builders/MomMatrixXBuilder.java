package net.vpc.scholar.hadruwaves.mom.builders;

import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.Vector;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruplot.console.ProgressTaskMonitor;
import net.vpc.scholar.hadruwaves.builders.ValueBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface MomMatrixXBuilder extends ValueBuilder {
    MomMatrixXBuilder monitor(ProgressTaskMonitor monitor);
    MomMatrixXBuilder monitor(ProgressMonitor monitor);

    MomMatrixXBuilder converge(ConvergenceEvaluator convergenceEvaluator);

    Matrix computeMatrix();

    Vector computeVector();

}
