package net.thevpc.scholar.hadruwaves.mom.str;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.str.MWStructureEvaluator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 21:49:35
 */
public interface TestFieldEvaluator extends MWStructureEvaluator {
    public VDiscrete evaluate(MomStructure str, double[] x, double[] y, ProgressMonitor monitor);
}
