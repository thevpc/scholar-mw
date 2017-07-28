package net.vpc.scholar.hadruwaves.mom.project.common;

import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadrumaths.util.ProgressMessage;
import net.vpc.scholar.hadrumaths.util.StringProgressMessage;

import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: taha
 * Date: 3 juil. 2004
 * Time: 15:15:13
 * To change this template use File | Settings | File Templates.
 */
public abstract class RunAction implements ComputationMonitor{
    private long startRunningTime = -1;
    private long endRunningTime = -1;
    private double progress;

    protected abstract Object run();

    public Object go() {
        startChrono();
        try {
            return run();
        } finally {
            stopChrono();
        }
    }

    public double getProgressValue(){
        return progress;
    }

    public void setProgress(double progress, ProgressMessage message) {
        this.progress = progress;
    }

    @Override
    public void setProgress(double progress, String message) {
        this.setProgress(progress,message==null?null:new StringProgressMessage(Level.INFO, message));
    }

    public long getStartRunningTime() {
        return startRunningTime;
    }

    public long getEndRunningTime() {
        return endRunningTime;
    }

    public void startChrono() {
        startRunningTime = System.currentTimeMillis();
    }

    public void stopChrono() {
        endRunningTime = System.currentTimeMillis();
    }

    public long getRemainingTimeEstimation() {
        double _progress = getProgressValue();
        if (_progress == 0) {
            return -1;
        }
        return (long) (getEvolvedTime() * (1 - _progress) / _progress);
    }

    public long getEvolvedTime() {
        return endRunningTime < 0 ? System.currentTimeMillis() - startRunningTime
                : endRunningTime - startRunningTime;

    }


}
