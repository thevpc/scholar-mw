package net.vpc.scholar.hadruwaves.mom.project.common;

import net.vpc.common.util.Chronometer;
import net.vpc.common.util.mon.AbstractProgressMonitor;
import net.vpc.common.util.mon.ProgressMessage;
import net.vpc.common.util.mon.ProgressMonitor;

/**
 * Created by IntelliJ IDEA. User: taha Date: 3 juil. 2004 Time: 15:15:13 To
 * change this template use File | Settings | File Templates.
 */
public abstract class RunAction extends AbstractProgressMonitor {
    private double progress;

    public double getProgress(){
        return getProgressValue();
    }
    
    protected abstract Object run();

    public Object go() {
        startChrono();
        try {
            return run();
        } finally {
            stopChrono();
        }
    }

    public double getProgressValue() {
        return progress;
    }

    public void setProgressImpl(double progress, ProgressMessage message) {
        this.progress = progress;
    }

    public void startChrono() {
        getChronometer().start();
    }

    public void stopChrono() {
        getChronometer().stop();
    }

    public long getRemainingTimeEstimation() {
        double _progress = getProgressValue();
        if (_progress == 0) {
            return -1;
        }
        return (long) (getEvolvedTime() * (1 - _progress) / _progress);
    }

    public long getEvolvedTime() {
        return getChronometer().getTime();

    }

    @Override
    public ProgressMessage getProgressMessage() {
        return null;
    }

}
