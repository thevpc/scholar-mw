package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.util.AbstractEnhancedProgressMonitor;
import net.vpc.scholar.hadrumaths.util.ProgressMessage;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class SilentEnhancedProgressMonitor extends AbstractEnhancedProgressMonitor {
    private double progress;
    private ProgressMessage message;

    @Override
    public double getProgressValue() {
        return progress;
    }

    @Override
    public void setProgressImpl(double progress, ProgressMessage message) {
        this.progress = progress;
        this.message = message;
        //do nothing
    }

    @Override
    public ProgressMessage getProgressMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Silent(" +
                "value=" + getProgressValue() +
                ')';
    }
}
