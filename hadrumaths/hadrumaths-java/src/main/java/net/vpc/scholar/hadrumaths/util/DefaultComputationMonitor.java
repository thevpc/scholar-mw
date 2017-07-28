package net.vpc.scholar.hadrumaths.util;

import java.util.logging.Level;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 15 mai 2007 01:36:26
 */
public class DefaultComputationMonitor implements ComputationMonitor{
    double progress;
    ProgressMessage progressMessage;
    public double getProgressValue() {
        return progress;
    }

    public void setProgress(double progress, ProgressMessage message) {
        if(progress<0 || progress>1){
            System.err.println("%= "+progress+"????????????");
        }
        this.progress=progress;
        this.progressMessage=message;
    }

    @Override
    public void setProgress(double progress, String message) {
        this.setProgress(progress,message==null?null:new StringProgressMessage(Level.INFO, message));
    }

    @Override
    public ProgressMessage getProgressMessage() {
        return progressMessage;
    }

    @Override
    public String toString() {
        return "Default(" +
                "value=" + getProgressValue() +
                ')';
    }
}
