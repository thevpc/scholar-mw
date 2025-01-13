package net.thevpc.scholar.hadruwaves.str;

import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.common.mon.ProgressMonitor;

/**
 * Created by vpc on 5/30/14.
 */
public interface MagneticFieldEvaluator extends MWStructureEvaluator {
    public VDiscrete evaluate(MWStructure str, double[] x, double[] y, double[] z, ProgressMonitor monitor);
}
