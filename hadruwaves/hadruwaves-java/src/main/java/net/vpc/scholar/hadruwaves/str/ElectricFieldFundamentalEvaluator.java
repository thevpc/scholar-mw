package net.vpc.scholar.hadruwaves.str;

import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 21:49:35
 */
public interface ElectricFieldFundamentalEvaluator extends MWStructureEvaluator {
    VDiscrete evaluate(MomStructure str, double[] x, double[] y, double[] z, ProgressMonitor monitor);
}
