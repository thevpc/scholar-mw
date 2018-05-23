package net.vpc.scholar.hadruwaves.str;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.common.util.mon.ProgressMonitor;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 21:49:35
 */
public interface SourceEvaluator extends MWStructureEvaluator {
    public Matrix computePlanarSources(MWStructure str, double[] x, double[] y, Axis axis, ProgressMonitor monitor) ;
}
