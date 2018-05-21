package net.vpc.scholar.hadrumaths.convergence;

import net.vpc.scholar.hadrumaths.monitors.ProgressMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface ObjectEvaluator {
    public abstract Object evaluate(Object object, ProgressMonitor monitor);
}
