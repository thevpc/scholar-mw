package net.vpc.scholar.hadruwaves.str;

import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.vpc.common.mon.ProgressMonitor;

/**
 * Created by vpc on 5/30/14.
 */
public interface PoyntingVectorEvaluator extends MWStructureEvaluator {
    public VDiscrete evaluate(MWStructure str, double[] x, double[] y, double[] z, ProgressMonitor monitor);
}
