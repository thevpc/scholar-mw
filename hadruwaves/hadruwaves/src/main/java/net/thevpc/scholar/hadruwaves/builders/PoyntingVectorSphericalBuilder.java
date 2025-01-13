package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitorFactory;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.ComplexVector;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface PoyntingVectorSphericalBuilder extends ValueBuilder {
    PoyntingVectorSphericalBuilder monitor(ProgressMonitorFactory monitor);

    PoyntingVectorSphericalBuilder monitor(ProgressMonitor monitor);

    PoyntingVectorSphericalBuilder converge(ConvergenceEvaluator convergenceEvaluator);


    ComplexMatrix evalModuleMatrix(double[] theta, double[] phi, double r);

    ComplexMatrix evalModuleMatrix(double[] theta, double phi, double[] r);

    ComplexMatrix evalModuleMatrix(double theta, double[] phi, double[] r);

    default ComplexVector evalHPlaneModuleVector(double[] theta, double r) {
        return evalXZModuleVector(theta, r);
    }

    default ComplexVector evalXZModuleVector(double[] theta, double r) {
        return evalModuleMatrix(theta, new double[]{0}, r).getColumn(0);
    }

    default ComplexVector evalEPlaneModuleVector(double[] theta, double r) {
        return evalYZModuleVector(theta, r);
    }

    default ComplexVector evalYZModuleVector(double[] theta, double r) {
        return evalModuleMatrix(theta, new double[]{Maths.HALF_PI}, r).getColumn(0);
    }
}
