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
public interface DirectivitySphericalBuilder extends ValueBuilder {
    DirectivitySphericalBuilder monitor(ProgressMonitorFactory monitor);

    DirectivitySphericalBuilder monitor(ProgressMonitor monitor);

    DirectivitySphericalBuilder converge(ConvergenceEvaluator convergenceEvaluator);

    ComplexMatrix evalMatrix(double[] theta, double[] phi, double r);

    default ComplexVector evalHPlaneVector(double[] theta, double r) {
        return evalXZVector(theta, r);
    }

    default ComplexVector evalXZVector(double[] theta, double r) {
        return evalMatrix(theta, new double[]{0}, r).getColumn(0);
    }

    default ComplexVector evalEPlaneVector(double[] theta, double r) {
        return evalYZVector(theta, r);
    }

    default ComplexVector evalYZVector(double[] theta, double r) {
        return evalMatrix(theta, new double[]{Maths.HALF_PI}, r).getColumn(0);
    }
}
