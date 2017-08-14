package net.vpc.scholar.hadruwaves.str;

import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadrumaths.util.ProgressMonitor;

/**
 * Created by vpc on 5/30/14.
 */
public interface MagneticFieldEvaluator extends MWStructureEvaluator {
    public VDiscrete evaluate(MWStructure str, double[] x, double[] y, double[] z, ProgressMonitor monitor);
}
