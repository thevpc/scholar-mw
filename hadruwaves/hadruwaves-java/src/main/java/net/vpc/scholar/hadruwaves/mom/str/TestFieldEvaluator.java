package net.vpc.scholar.hadruwaves.mom.str;

import net.vpc.common.util.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.str.MWStructureEvaluator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 21:49:35
 */
public interface TestFieldEvaluator extends MWStructureEvaluator {
    public VDiscrete evaluate(MomStructure str, double[] x, double[] y, ProgressMonitor monitor);
}
