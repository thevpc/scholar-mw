package net.vpc.scholar.hadruwaves.mom.project.common;

import net.vpc.common.mon.BaseProgressMonitor;
import net.vpc.common.mon.ProgressMessage;

/**
 * Created by IntelliJ IDEA. User: taha Date: 3 juil. 2004 Time: 15:15:13 To
 * change this template use File | Settings | File Templates.
 */
public abstract class RunAction extends BaseProgressMonitor {
    private double progress;

    public double getProgress(){
        return getProgressValue();
    }
    
    protected abstract Object run();

    public Object go() {
        setProgress(0);
        try {
            return run();
        } finally {
            setProgress(1);
        }
    }

    public double getProgressValue() {
        return progress;
    }

    public void setProgressImpl(double progress, ProgressMessage message) {
        this.progress = progress;
    }

    public long getRemainingTimeEstimation() {
        double _progress = getProgressValue();
        if (_progress == 0) {
            return -1;
        }
        return (long) (getDuration() * (1 - _progress) / _progress);
    }

    @Override
    public ProgressMessage getProgressMessage() {
        return null;
    }

}
