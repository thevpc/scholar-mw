package net.vpc.scholar.hadrumaths.util;

import net.vpc.scholar.hadrumaths.Chronometer;
import net.vpc.scholar.hadrumaths.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.Maths;

import java.util.logging.Level;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public abstract class AbstractEnhancedProgressMonitor implements EnhancedProgressMonitor {
    private ComputationMonitorInc incrementor = new DeltaComputationMonitorInc(1E-2);
    private Chronometer chronometer;
    private boolean paused = false;
    private boolean cancelled = false;
    private boolean started = false;

    public AbstractEnhancedProgressMonitor() {
    }

    public EnhancedProgressMonitor[] split(boolean... enabledElements) {
        return ProgressMonitorFactory.split(this, enabledElements);
    }

    /**
     * creates Monitors for each enabled Element or null if false
     *
     * @return ProgressMonitor[] array that contains nulls or  translated baseMonitor
     */
    public EnhancedProgressMonitor[] split(int nbrElements) {
        return ProgressMonitorFactory.split(this, nbrElements);
    }

    public EnhancedProgressMonitor[] split(double[] weight) {
        return ProgressMonitorFactory.split(this, weight);
    }


    public EnhancedProgressMonitor[] split(double[] weight, boolean[] enabledElements) {
        return ProgressMonitorFactory.split(this, weight, enabledElements);
    }

    public EnhancedProgressMonitor translate(double factor, double start) {
        return ProgressMonitorFactory.translate(this, factor, start);
    }

    public EnhancedProgressMonitor translate(int index, int max) {
        return new ProgressMonitorTranslator(this, 1.0 / max, index * (1.0 / max));
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


    public EnhancedProgressMonitor createIncrementalMonitor(int iterations) {
        return ProgressMonitorFactory.createIncrementalMonitor(this, iterations);
    }

    public EnhancedProgressMonitor createIncrementalMonitor(ProgressMonitor baseMonitor, double delta) {
        return ProgressMonitorFactory.createIncrementalMonitor(this, delta);
    }

    public EnhancedProgressMonitor temporize(long freq) {
        return ProgressMonitorFactory.temporize(this, freq);
    }


    public EnhancedProgressMonitor inc() {
        inc("");
        return this;
    }

    public EnhancedProgressMonitor inc(String message) {
        setProgress(incrementor.inc(getProgressValue()), new StringProgressMessage(Level.FINE, message));
        return this;
    }

    @Override
    public EnhancedProgressMonitor setMessage(ProgressMessage message) {
        setProgress(getProgressValue(), message);
        return this;
    }

    @Override
    public EnhancedProgressMonitor setMessage(String message) {
        setProgress(getProgressValue(), new StringProgressMessage(Level.FINE, message));
        return this;
    }

    @Override
    public EnhancedProgressMonitor setIndeterminate(String message) {
        setProgress(ProgressMonitor.INDETERMINATE_PROGRESS, new StringProgressMessage(Level.FINE, message));
        return this;
    }

    @Override
    public EnhancedProgressMonitor setMessage(String message, Object... args) {
        setProgress(getProgressValue(), new FormattedProgressMessage(Level.FINE, message, args));
        return this;
    }

    @Override
    public EnhancedProgressMonitor setIndeterminate(String message, Object... args) {
        setProgress(ProgressMonitor.INDETERMINATE_PROGRESS, new FormattedProgressMessage(Level.FINE, message, args));
        return this;
    }

    @Override
    public EnhancedProgressMonitor inc(String message, Object... args) {
        setProgress(incrementor.inc(getProgressValue()), new FormattedProgressMessage(Level.FINE, message, args));
        return this;
    }

    @Override
    public EnhancedProgressMonitor start(String message) {
        setProgress(0, new StringProgressMessage(Level.FINE, message));
        return this;
    }

//    @Override
//    public EnhancedProgressMonitor startm(String message) {
//        return start(message + ", starting...");
//    }
//
//    @Override
//    public EnhancedProgressMonitor terminatem(String message, Object... args) {
//        return terminate(message + ", terminated...",args);
//    }
//
//    @Override
//    public EnhancedProgressMonitor terminatem(String message) {
//        return terminate(message + ", terminated...");
//    }

    @Override
    public EnhancedProgressMonitor start(String message, Object... args) {
        setProgress(0, new FormattedProgressMessage(Level.INFO, message, args));
        return this;
    }


    @Override
    public EnhancedProgressMonitor terminate(String message) {
        if (!isCanceled()) {
            setProgress(1, new StringProgressMessage(Level.INFO, message));
        }
        return this;
    }

    @Override
    public EnhancedProgressMonitor terminate(String message, Object... args) {
        if (!isCanceled()) {
            setProgress(1, new FormattedProgressMessage(Level.INFO, message,args));
        }
        return this;
    }

    public EnhancedProgressMonitor setIncrementor(ComputationMonitorInc incrementor) {
        this.incrementor = incrementor;
        return this;
    }

    @Override
    public EnhancedProgressMonitor cancel() {
        cancelled = true;
        return this;
    }

    @Override
    public EnhancedProgressMonitor resume() {
        paused = false;
        return this;
    }

    @Override
    public EnhancedProgressMonitor suspend() {
        paused = true;
        return this;
    }

    public final void setProgress(double progress, ProgressMessage message) {
        Chronometer c = getChronometer();//
        if(!started){
            started=true;
            c.start();
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
        return isCanceled() || getProgressValue() >= 1;
    }

    @Override
    public boolean isCanceled() {
        return cancelled;
    }

    public long getStartTime() {
        return getChronometer().getStartTime();
    }

    @Override
    public Chronometer getChronometer() {
        if(chronometer==null){
            chronometer=new Chronometer(false);
        }
        return chronometer;
    }

    @Override
    public void setProgress(double progress, String message) {
        setProgress(progress, new StringProgressMessage(Level.FINE, message));
    }

    @Override
    public void setProgress(double progress, String message, Object... args) {
        setProgress(progress, new FormattedProgressMessage(Level.FINE, message, args));
    }


    @Override
    public void stop() {

    }
}
