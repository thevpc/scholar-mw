package net.vpc.scholar.hadruwaves.str;

import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadrumaths.util.ProgressMonitor;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 21:49:35
 */
public interface CurrentEvaluator extends MWStructureEvaluator {
    VDiscrete evaluate(MWStructure str, double[] x, double[] y, ProgressMonitor monitor);
}
