package net.thevpc.scholar.hadruwaves.str;

import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.common.mon.ProgressMonitor;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 21:49:35
 */
public interface CurrentEvaluator extends MWStructureEvaluator {
    VDiscrete evaluate(MWStructure str, double[] x, double[] y, ProgressMonitor monitor);
}
