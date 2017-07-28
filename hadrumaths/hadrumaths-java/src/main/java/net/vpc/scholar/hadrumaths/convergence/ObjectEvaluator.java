package net.vpc.scholar.hadrumaths.convergence;

import net.vpc.scholar.hadrumaths.util.ComputationMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface ObjectEvaluator {
    public abstract Object evaluate(Object object,ComputationMonitor monitor);
}
