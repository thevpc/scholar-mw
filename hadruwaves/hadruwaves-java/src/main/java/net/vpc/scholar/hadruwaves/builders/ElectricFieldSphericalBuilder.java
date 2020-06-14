package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.ComplexVector;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface ElectricFieldSphericalBuilder extends SphericalFieldBuilder {
    ElectricFieldSphericalBuilder monitor(ProgressMonitorFactory monitor);

    ElectricFieldSphericalBuilder monitor(ProgressMonitor monitor);

    ElectricFieldSphericalBuilder converge(ConvergenceEvaluator convergenceEvaluator);


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
