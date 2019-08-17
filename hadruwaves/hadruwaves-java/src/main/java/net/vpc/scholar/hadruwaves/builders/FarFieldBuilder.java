package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadruplot.console.ProgressTaskMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface FarFieldBuilder extends ValueBuilder {
    FarFieldBuilder monitor(ProgressTaskMonitor monitor);
    FarFieldBuilder monitor(ProgressMonitor monitor);

    FarFieldBuilder converge(ConvergenceEvaluator convergenceEvaluator);

    TMatrix<Complex> computeMatrix(SAxis axis, double[] theta, double[] phi, double r);

    TVector<TMatrix<Complex>> computeMatrices(double[] theta, double[] phi, double r);
}
