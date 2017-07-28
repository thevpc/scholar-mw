package net.vpc.scholar.hadrumaths.util;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public class EnhancedComputationMonitorAdapter extends AbstractEnhancedComputationMonitor {
    private ComputationMonitor base;

    public EnhancedComputationMonitorAdapter(ComputationMonitor base) {
        this.base = base;
    }

    @Override
    public double getProgressValue() {
        return base.getProgressValue();
    }

    @Override
    public ProgressMessage getProgressMessage() {
        return base.getProgressMessage();
    }

    @Override
    public void setProgressImpl(double progress, ProgressMessage message) {
        base.setProgress(progress,message);
    }

    public ComputationMonitor getBase() {
        return base;
    }

    @Override
    public String toString() {
        return "Adapter(" +
                base +
                ')';
    }
}
