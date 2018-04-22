package net.vpc.scholar.hadrumaths.util;

import net.vpc.scholar.hadrumaths.util.EnhancedProgressMonitor;
import net.vpc.scholar.hadrumaths.util.ProgressMessage;
import net.vpc.scholar.hadrumaths.util.ProgressMonitor;

public class ReusableProgressMonitor implements ProgressMonitor {
    private final EnhancedProgressMonitor base;

    public ReusableProgressMonitor(EnhancedProgressMonitor base) {
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
    public void setProgress(double progress, ProgressMessage message) {
        if(progress==1.0){
            progress=Double.NaN;
        }
        base.setProgress(progress,message);
    }

    @Override
    public boolean isCanceled() {
        return base.isCanceled();
    }

    @Override
    public void stop() {
        base.stop();
    }

    public void terminateAll(){
        base.terminate("");
    }
}
