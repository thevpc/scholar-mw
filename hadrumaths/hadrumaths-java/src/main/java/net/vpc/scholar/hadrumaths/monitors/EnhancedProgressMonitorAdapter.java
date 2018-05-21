package net.vpc.scholar.hadrumaths.monitors;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public class EnhancedProgressMonitorAdapter extends AbstractEnhancedProgressMonitor {
    private ProgressMonitor base;

    public EnhancedProgressMonitorAdapter(ProgressMonitor base) {
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
        base.setProgress(progress, message);
    }

    public ProgressMonitor getBase() {
        return base;
    }

    @Override
    public String toString() {
        return "Adapter(" +
                base +
                ')';
    }
}
