package net.vpc.scholar.hadrumaths.plot.convergence;

import net.vpc.common.mon.ProgressMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface ObjectEvaluator {
    Object evaluate(Object object, ProgressMonitor monitor);
}
