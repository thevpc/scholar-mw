package net.thevpc.scholar.hadruwaves.str;

import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 21:49:35
 */
public interface ElectricFieldFundamentalEvaluator extends MWStructureEvaluator {
    VDiscrete evaluate(MomStructure str, double[] x, double[] y, double[] z, ProgressMonitor monitor);
}
