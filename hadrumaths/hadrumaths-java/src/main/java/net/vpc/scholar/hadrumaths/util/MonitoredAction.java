package net.vpc.scholar.hadrumaths.util;

/**
 * Created by vpc on 5/14/17.
 */
public interface MonitoredAction<T> {
    T process(EnhancedComputationMonitor monitor, String messagePrefix) throws Exception;
}
