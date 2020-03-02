package net.vpc.scholar.hadruwaves.str;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.Vector;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 21:49:35
 */
public interface FarFieldEvaluator extends MWStructureEvaluator {
    /**
     *
     * @param theta theta values
     * @param phi phi values
     * @param r distance
     * @param monitor monitor
     * @return FarField_theta(theta as row,phi as column) = Matrix[0] FarField_phi = Matrix[1]
     */
    Vector<ComplexMatrix> evaluate(MWStructure str, double[] theta, double[] phi, double r, ProgressMonitor monitor) ;
}
