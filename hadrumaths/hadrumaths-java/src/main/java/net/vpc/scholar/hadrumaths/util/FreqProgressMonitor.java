package net.vpc.scholar.hadrumaths.util;

import java.util.logging.Level;

public class FreqProgressMonitor extends AbstractEnhancedProgressMonitor {
    private long freq;
    private long lastDate;
    private double progress;
    private ProgressMessage message;
    private ProgressMonitor base;
    private Level level = Level.INFO;

    public FreqProgressMonitor(ProgressMonitor base, long freq) {
        this.base = base;
        if (freq < 0) {
            freq = 0;
        }
        this.freq = freq;
    }

    @Override
    public double getProgressValue() {
        return progress;
    }

    @Override
    public ProgressMessage getProgressMessage() {
        return message;
    }

    public void setProgressImpl(double progress, ProgressMessage message) {
        this.progress = progress;
        this.message = message;
        long newd = System.currentTimeMillis();
        if (message.getLevel().intValue() >= level.intValue() || newd > lastDate + freq) {
            base.setProgress(progress, message);
            lastDate = newd;
        }
    }

    @Override
    public String toString() {
        return "Freq(" +
                "value=" + getProgressValue() +
                ",time=" + freq +
                ", " + base +
                ')';
    }
}
