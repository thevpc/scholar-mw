package net.vpc.scholar.hadrumaths.util;

/**
 * Created by vpc on 5/14/17.
 */
public abstract class VoidMonitoredAction implements MonitoredAction<Boolean> {
    public final Boolean process(EnhancedComputationMonitor monitor, String messagePrefix) throws Exception {
        invoke(monitor, messagePrefix);
        return true;
    }

    public abstract void invoke(EnhancedComputationMonitor monitor, String messagePrefix) throws Exception;
}
