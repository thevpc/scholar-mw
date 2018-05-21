package net.vpc.scholar.hadrumaths.monitors;

import net.vpc.scholar.hadrumaths.Chronometer;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public interface EnhancedProgressMonitor extends ProgressMonitor {


    EnhancedProgressMonitor[] split(boolean... enabledElements);

    /**
     * creates Monitors for each enabled Element or null if false
     *
     * @return ProgressMonitor[] array that contains nulls or  translated baseMonitor
     */
    EnhancedProgressMonitor[] split(int nbrElements);

    EnhancedProgressMonitor[] split(double[] weight);

    EnhancedProgressMonitor[] split(double[] weight, boolean[] enabledElements);

    EnhancedProgressMonitor translate(int index, int max);

    EnhancedProgressMonitor translate(double factor, double start);

    void setProgress(int i, int max, String message);

    void setProgress(int i, int max, String message, Object... args);

    void setProgress(int i, int j, int maxi, int maxj, String message, Object... args);

    void setProgress(int i, int j, int maxi, int maxj, String message);


    EnhancedProgressMonitor createIncrementalMonitor(int iterations);

    EnhancedProgressMonitor createIncrementalMonitor(ProgressMonitor baseMonitor, double delta);

    EnhancedProgressMonitor temporize(long freq);

    EnhancedProgressMonitor inc();

    EnhancedProgressMonitor setMessage(ProgressMessage message);

    EnhancedProgressMonitor setMessage(String message);

    EnhancedProgressMonitor setMessage(String message, Object... args);

    EnhancedProgressMonitor setIndeterminate(String message, Object... args);

    EnhancedProgressMonitor setIndeterminate(String message);

    EnhancedProgressMonitor inc(String message);

    EnhancedProgressMonitor inc(String message, Object... args);

//    EnhancedProgressMonitor startm(String message);

    EnhancedProgressMonitor start(String message);

    EnhancedProgressMonitor start(String message, Object... args);

    EnhancedProgressMonitor terminate(String message);

    EnhancedProgressMonitor terminate(String message, Object... args);

//    EnhancedProgressMonitor terminatem(String message);

//    EnhancedProgressMonitor terminatem(String message, Object... args);

    EnhancedProgressMonitor setIncrementor(ComputationMonitorInc incrementor);

    EnhancedProgressMonitor cancel();

    EnhancedProgressMonitor resume();

    EnhancedProgressMonitor suspend();

    boolean isTerminated();

    Chronometer getChronometer();

    void setProgress(double progress, String message);

    void setProgress(double progress, String message, Object... args);

}
