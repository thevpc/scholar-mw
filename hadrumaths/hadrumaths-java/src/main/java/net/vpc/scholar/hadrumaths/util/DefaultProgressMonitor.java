package net.vpc.scholar.hadrumaths.util;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 15 mai 2007 01:36:26
 */
public class DefaultProgressMonitor implements ProgressMonitor {
    double progress;
    ProgressMessage progressMessage;

    public double getProgressValue() {
        return progress;
    }

    public void setProgress(double progress, ProgressMessage message) {
        if (progress < 0 || progress > 1) {
            System.err.println("%= " + progress + "????????????");
        }
        this.progress = progress;
        this.progressMessage = message;
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

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public void stop() {

    }
}
