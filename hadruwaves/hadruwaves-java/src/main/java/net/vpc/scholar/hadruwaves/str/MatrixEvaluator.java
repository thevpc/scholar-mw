package net.vpc.scholar.hadruwaves.str;

import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 21:47:59
 */
public interface MatrixEvaluator extends MWStructureEvaluator {
    Matrix evaluate(MomStructure str, ProgressMonitor monitor);
}
