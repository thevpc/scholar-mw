package net.thevpc.ntexup.extension.mwsimulator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * What a solve cost on this machine: wall-clock time of the sweep and the peak
 * memory of the solver containers it started. Both depend on the machine's load
 * at the time, so they are reported as an indication, not a benchmark.
 * <p>
 * A solver calls {@link #begin(String)} before its sweep, on the thread that
 * evaluates it; the Docker launcher records each container's peak into the cost
 * open on that thread; {@link NTxSolverMetrics#publish} closes it.
 */
public class NTxSolverCost {

    private static final ThreadLocal<NTxSolverCost> CURRENT = new ThreadLocal<>();
    private static final Map<String, NTxSolverCost> OPEN = new ConcurrentHashMap<>();

    private final long startNanos = System.nanoTime();
    private volatile long endNanos;
    private volatile long peakBytes = -1;

    public static NTxSolverCost begin(String planName) {
        NTxSolverCost c = new NTxSolverCost();
        CURRENT.set(c);
        if (planName != null && !planName.trim().isEmpty()) {
            OPEN.put(planName.trim(), c);
        }
        return c;
    }

    /** The cost open on this thread, or null when no solver sweep is running here. */
    public static NTxSolverCost current() {
        return CURRENT.get();
    }

    /** Closes and returns the cost opened for this plan, or null if none was opened. */
    public static NTxSolverCost finish(String planName) {
        if (planName == null) {
            return null;
        }
        NTxSolverCost c = OPEN.remove(planName.trim());
        if (c != null) {
            c.endNanos = System.nanoTime();
            if (CURRENT.get() == c) {
                CURRENT.remove();
            }
        }
        return c;
    }

    public synchronized void recordPeak(long bytes) {
        if (bytes > peakBytes) {
            peakBytes = bytes;
        }
    }

    public double seconds() {
        long end = endNanos != 0 ? endNanos : System.nanoTime();
        return (end - startNanos) / 1e9;
    }

    /** Peak memory of the solver containers, or -1 when the solve ran in-process. */
    public long peakBytes() {
        return peakBytes;
    }

    public String timeText() {
        double s = seconds();
        if (s < 60) {
            return Math.round(s) + " s";
        }
        long min = Math.round(s / 60);
        if (min < 60) {
            return min + " min";
        }
        return (min / 60) + " h " + (min % 60) + " min";
    }

    /** In GiB, the unit of Docker's --memory=20g, so it compares directly with the cap. */
    public String peakGBText() {
        if (peakBytes < 0) {
            return "n/a";
        }
        double gb = peakBytes / (double) (1L << 30);
        return gb < 10 ? String.format(java.util.Locale.ROOT, "%.2f", gb) : String.format(java.util.Locale.ROOT, "%.1f", gb);
    }
}
