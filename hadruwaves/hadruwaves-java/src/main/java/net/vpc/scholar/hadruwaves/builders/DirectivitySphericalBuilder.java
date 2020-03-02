package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.ComplexVector;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.TaskMonitorManager;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface DirectivitySphericalBuilder extends ValueBuilder {
    DirectivitySphericalBuilder monitor(TaskMonitorManager monitor);

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
