package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.Vector;
import net.vpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface SphericalFieldBuilder extends ValueBuilder {
    SphericalFieldBuilder monitor(ProgressMonitorFactory monitor);

    SphericalFieldBuilder monitor(ProgressMonitor monitor);

    SphericalFieldBuilder converge(ConvergenceEvaluator convergenceEvaluator);

    /**
     * returns {Xphi(phi,theta,r),Xtetha(phi,theta,r),Xr(phi,theta,r)}
     * @param phi phi
     * @param theta theta
     * @param r r
     * @return {Xphi(phi,theta,r),Xtetha(phi,theta,r),Xr(phi,theta,r)}
     */
    Vector<ComplexMatrix> evalMatrix(double[] theta, double[] phi, double r);

    Vector<ComplexMatrix> evalMatrix(double[] theta, double phi, double[] r);

    Vector<ComplexMatrix> evalMatrix(double theta, double[] phi, double[] r);

//    VDiscrete evalVDiscrete(Samples samples);

    Vector<VDiscrete> evalVDiscrete(double[] theta, double[] phi, double[] r);

//    VDiscrete evalVDiscrete(double[] phi, double[] theta);

//    Expr expr();
}
