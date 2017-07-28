package net.vpc.scholar.hadrumaths.util;

import net.vpc.scholar.hadrumaths.ComputationMonitorFactory;
import net.vpc.scholar.hadrumaths.Maths;

import java.util.logging.Level;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public abstract class AbstractEnhancedComputationMonitor implements EnhancedComputationMonitor {
    private ComputationMonitorInc incrementor = new DeltaComputationMonitorInc(1E-2);
    private long startTime;
    private boolean paused = false;
    private boolean cancelled = false;

    public AbstractEnhancedComputationMonitor() {
    }

    public EnhancedComputationMonitor[] split(boolean... enabledElements) {
        return ComputationMonitorFactory.split(this, enabledElements);
    }

    /**
     * creates Monitors for each enabled Element or null if false
     *
     * @return ComputationMonitor[] array that contains nulls or  translated baseMonitor
     */
    public EnhancedComputationMonitor[] split(int nbrElements) {
        return ComputationMonitorFactory.split(this, nbrElements);
    }

    public EnhancedComputationMonitor[] split(double[] weight) {
        return ComputationMonitorFactory.split(this, weight);
    }


    public EnhancedComputationMonitor[] split(double[] weight, boolean[] enabledElements) {
        return ComputationMonitorFactory.split(this, weight, enabledElements);
    }

    public EnhancedComputationMonitor translate(double factor, double start) {
        return ComputationMonitorFactory.translate(this, factor, start);
    }

    public EnhancedComputationMonitor translate(int index, int max) {
        return new ComputationMonitorTranslator(this, 1.0 / max, index * (1.0 / max));
    }

    public void setProgress(int i, int max, String message) {
        this.setProgress((1.0 * i / max), new StringProgressMessage(Level.FINE, message));
    }

    public void setProgress(int i, int max, String message, Object... args) {
        this.setProgress((1.0 * i / max), new FormattedProgressMessage(Level.FINE, message, args));
    }

    public void setProgress(int i, int j, int maxi, int maxj, String message) {
        this.setProgress(((1.0 * i * maxi) + j) / (maxi * maxj), new StringProgressMessage(Level.FINE, message));
    }

    public void setProgress(int i, int j, int maxi, int maxj, String message, Object... args) {
        this.setProgress(((1.0 * i * maxi) + j) / (maxi * maxj), new FormattedProgressMessage(Level.FINE, message, args));
    }


    public EnhancedComputationMonitor createIncrementalMonitor(int iterations) {
        return ComputationMonitorFactory.createIncrementalMonitor(this, iterations);
    }

    public EnhancedComputationMonitor createIncrementalMonitor(ComputationMonitor baseMonitor, double delta) {
        return ComputationMonitorFactory.createIncrementalMonitor(this, delta);
    }

    public EnhancedComputationMonitor temporize(long freq) {
        return ComputationMonitorFactory.temporize(this, freq);
    }


    public EnhancedComputationMonitor inc() {
        inc("");
        return this;
    }

    public EnhancedComputationMonitor inc(String message) {
        setProgress(incrementor.inc(getProgressValue()), new StringProgressMessage(Level.FINE, message));
        return this;
    }

    @Override
    public EnhancedComputationMonitor setMessage(ProgressMessage message) {
        setProgress(getProgressValue(), message);
        return this;
    }

    @Override
    public EnhancedComputationMonitor setMessage(String message) {
        setProgress(getProgressValue(), new StringProgressMessage(Level.FINE, message));
        return this;
    }

    @Override
    public EnhancedComputationMonitor setIndeterminate(String message) {
        setProgress(ComputationMonitor.INDETERMINATE_PROGRESS, new StringProgressMessage(Level.FINE, message));
        return this;
    }

    @Override
    public EnhancedComputationMonitor setMessage(String message, Object... args) {
        setProgress(getProgressValue(), new FormattedProgressMessage(Level.FINE, message, args));
        return this;
    }

    @Override
    public EnhancedComputationMonitor setIndeterminate(String message, Object... args) {
        setProgress(ComputationMonitor.INDETERMINATE_PROGRESS, new FormattedProgressMessage(Level.FINE, message, args));
        return this;
    }

    @Override
    public EnhancedComputationMonitor inc(String message, Object... args) {
        setProgress(incrementor.inc(getProgressValue()), new FormattedProgressMessage(Level.FINE, message, args));
        return this;
    }

    @Override
    public EnhancedComputationMonitor start(String message) {
        setProgress(0, new StringProgressMessage(Level.FINE, message));
        return this;
    }

//    @Override
//    public EnhancedComputationMonitor startm(String message) {
//        return start(message + ", starting...");
//    }
//
//    @Override
//    public EnhancedComputationMonitor terminatem(String message, Object... args) {
//        return terminate(message + ", terminated...",args);
//    }
//
//    @Override
//    public EnhancedComputationMonitor terminatem(String message) {
//        return terminate(message + ", terminated...");
//    }

    @Override
    public EnhancedComputationMonitor start(String message, Object... args) {
        setProgress(0, new FormattedProgressMessage(Level.INFO, message, args));
        return this;
    }


    @Override
    public EnhancedComputationMonitor terminate(String message) {
        if (!isCancelled()) {
            setProgress(1, new StringProgressMessage(Level.INFO, message));
        }
        return this;
    }

    @Override
    public EnhancedComputationMonitor terminate(String message, Object... args) {
        if (!isCancelled()) {
            setProgress(1, new FormattedProgressMessage(Level.INFO, message,args));
        }
        return this;
    }

    public EnhancedComputationMonitor setIncrementor(ComputationMonitorInc incrementor) {
        this.incrementor = incrementor;
        return this;
    }

    @Override
    public EnhancedComputationMonitor cancel() {
        cancelled = true;
        return this;
    }

    @Override
    public EnhancedComputationMonitor resume() {
        paused = false;
        return this;
    }

    @Override
    public EnhancedComputationMonitor suspend() {
        paused = true;
        return this;
    }

    public final void setProgress(double progress, ProgressMessage message) {
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }
        if (cancelled) {
            throw new ComputationCancelledException();
        }
        if (paused) {
            while (paused) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    //ignore
                }
            }
        }
        if ((progress < 0 || progress > 1) && !Double.isNaN(progress)) {
            if (Maths.Config.isStrictComputationMonitor()) {
                throw new RuntimeException("Invalid Progress value [0..1] : " + progress);
            } else {
                if (progress < 0) {
                    progress = 0;
                } else if (progress > 1) {
                    progress = 1;
                }
            }
        }
        if (Maths.Config.isStrictComputationMonitor()) {
            if (!Double.isNaN(progress)  && progress < getProgressValue() && getProgressValue() >= 1) {
                throw new RuntimeException("Invalid Progress value [0..1] : " + progress + "<" + getProgressValue());
            }
        }
        setProgressImpl(progress, message);
    }

    public abstract void setProgressImpl(double progress, ProgressMessage message);

    @Override
    public boolean isTerminated() {
        return isCancelled() || getProgressValue() >= 1;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public long getStartTime() {
        return startTime;
    }

    @Override
    public void setProgress(double progress, String message) {
        setProgress(progress, new StringProgressMessage(Level.FINE, message));
    }

    @Override
    public void setProgress(double progress, String message, Object... args) {
        setProgress(progress, new FormattedProgressMessage(Level.FINE, message, args));
    }
}
