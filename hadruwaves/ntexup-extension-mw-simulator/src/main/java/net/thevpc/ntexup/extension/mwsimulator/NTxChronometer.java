package net.thevpc.ntexup.extension.mwsimulator;

import java.io.Serializable;

public class NTxChronometer implements Serializable {
    private long startTime;
    private long stopTime;
    private boolean started;
    private boolean stopped;

    public static NTxChronometer of() {
        NTxChronometer c = new NTxChronometer();
        c.start();
        return c;
    }

    public static NTxChronometer ofUnstarted() {
        return new NTxChronometer();
    }

    public NTxChronometer start() {
        startTime = System.currentTimeMillis();
        started = true;
        stopped = false;
        return this;
    }

    public NTxChronometer stop() {
        stopTime = System.currentTimeMillis();
        stopped = true;
        return this;
    }

    public NTxChronometer reset() {
        started = false;
        stopped = false;
        startTime = 0;
        stopTime = 0;
        return this;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isStopped() {
        return stopped;
    }

    public long durationMs() {
        if (!started) return 0;
        long end = stopped ? stopTime : System.currentTimeMillis();
        return Math.max(0, end - startTime);
    }

    public String duration() {
        long ms = durationMs();
        if (ms < 1000) return ms + "ms";
        return String.format("%.2fs", ms / 1000.0);
    }

    public NTxChronometer asReadOnly() {
        return this;
    }

    @Override
    public String toString() {
        return duration();
    }
}
