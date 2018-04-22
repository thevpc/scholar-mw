package net.vpc.scholar.hadrumaths.util;

import java.awt.*;
import java.util.logging.Level;

public class DialogProgressMonitor implements ProgressMonitor {
    private javax.swing.ProgressMonitor monitor;
    private double nv;
    private ProgressMessage msg;

    public DialogProgressMonitor(Component parentComponent, Object message) {
        monitor = new javax.swing.ProgressMonitor(parentComponent, message, null, 0, 100);
        nv = 0;
        msg = new StringProgressMessage(Level.INFO, "");
    }

    @Override
    public double getProgressValue() {
        return nv;
    }

    @Override
    public ProgressMessage getProgressMessage() {
        return msg;
    }

    @Override
    public void setProgress(double progress, ProgressMessage message) {
        this.nv=progress;
        msg=message;
        monitor.setProgress((int)(100*nv));
    }

    @Override
    public boolean isCanceled() {
        return monitor.isCanceled();
    }

    @Override
    public void stop() {
        monitor.close();
    }
}
