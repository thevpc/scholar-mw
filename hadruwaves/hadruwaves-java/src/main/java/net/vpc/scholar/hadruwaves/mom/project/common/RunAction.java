package net.vpc.scholar.hadruwaves.mom.project.common;

import net.vpc.scholar.hadrumaths.Chronometer;
import net.vpc.scholar.hadrumaths.util.ProgressMonitor;
import net.vpc.scholar.hadrumaths.util.ProgressMessage;

/**
 * Created by IntelliJ IDEA. User: taha Date: 3 juil. 2004 Time: 15:15:13 To
 * change this template use File | Settings | File Templates.
 */
public abstract class RunAction implements ProgressMonitor {

    private Chronometer chronometer = new Chronometer(false);
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

    public void setProgress(double progress, ProgressMessage message) {
        this.progress = progress;
    }

    public void startChrono() {
        chronometer.start();
    }

    public void stopChrono() {
        chronometer.stop();
    }

    public long getRemainingTimeEstimation() {
        double _progress = getProgressValue();
        if (_progress == 0) {
            return -1;
        }
        return (long) (getEvolvedTime() * (1 - _progress) / _progress);
    }

    public long getEvolvedTime() {
        return chronometer.getTime();

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public ProgressMessage getProgressMessage() {
        return null;
    }

}
