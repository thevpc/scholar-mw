package net.thevpc.scholar.hadruwaves.str;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.common.mon.ProgressMonitor;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 21:49:35
 */
public interface SourceEvaluator extends MWStructureEvaluator {
    public ComplexMatrix evalPlanarSources(MWStructure str, double[] x, double[] y, Axis axis, ProgressMonitor monitor) ;
}
