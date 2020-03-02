package net.vpc.scholar.hadruwaves.str;

import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.vpc.common.mon.ProgressMonitor;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 21:49:35
 */
public interface ElectricFieldEvaluator extends MWStructureEvaluator {
    VDiscrete evaluate(MWStructure str, double[] x, double[] y, double[] z, ProgressMonitor monitor);
}
