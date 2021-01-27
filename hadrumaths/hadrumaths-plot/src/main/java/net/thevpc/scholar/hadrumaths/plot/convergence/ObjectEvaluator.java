package net.thevpc.scholar.hadrumaths.plot.convergence;

import net.thevpc.common.mon.ProgressMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface ObjectEvaluator {
    Object evaluate(Object object, ProgressMonitor monitor);
}
