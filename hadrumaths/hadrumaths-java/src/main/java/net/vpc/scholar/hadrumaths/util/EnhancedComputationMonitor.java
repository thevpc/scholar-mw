package net.vpc.scholar.hadrumaths.util;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public interface EnhancedComputationMonitor extends ComputationMonitor {


    EnhancedComputationMonitor[] split(boolean... enabledElements);

    /**
     * creates Monitors for each enabled Element or null if false
     *
     * @return ComputationMonitor[] array that contains nulls or  translated baseMonitor
     */
    EnhancedComputationMonitor[] split(int nbrElements);

    EnhancedComputationMonitor[] split(double[] weight);

    EnhancedComputationMonitor[] split(double[] weight, boolean[] enabledElements);

    EnhancedComputationMonitor translate(int index, int max);

    EnhancedComputationMonitor translate(double factor, double start);

    void setProgress(int i, int max, String message);

    void setProgress(int i, int max, String message, Object... args);

    void setProgress(int i, int j, int maxi, int maxj, String message, Object... args);

    void setProgress(int i, int j, int maxi, int maxj, String message);


    EnhancedComputationMonitor createIncrementalMonitor(int iterations);

    EnhancedComputationMonitor createIncrementalMonitor(ComputationMonitor baseMonitor, double delta);

    EnhancedComputationMonitor temporize(long freq);

    EnhancedComputationMonitor inc();

    EnhancedComputationMonitor setMessage(ProgressMessage message);

    EnhancedComputationMonitor setMessage(String message);

    EnhancedComputationMonitor setMessage(String message, Object... args);

    EnhancedComputationMonitor setIndeterminate(String message, Object... args);

    EnhancedComputationMonitor setIndeterminate(String message);

    EnhancedComputationMonitor inc(String message);

    EnhancedComputationMonitor inc(String message, Object... args);

//    EnhancedComputationMonitor startm(String message);

    EnhancedComputationMonitor start(String message);

    EnhancedComputationMonitor start(String message, Object... args);

    EnhancedComputationMonitor terminate(String message);

    EnhancedComputationMonitor terminate(String message, Object... args);

//    EnhancedComputationMonitor terminatem(String message);

//    EnhancedComputationMonitor terminatem(String message, Object... args);

    EnhancedComputationMonitor setIncrementor(ComputationMonitorInc incrementor);

    EnhancedComputationMonitor cancel();

    EnhancedComputationMonitor resume();

    EnhancedComputationMonitor suspend();

    boolean isTerminated();

    boolean isCancelled();

    long getStartTime();

    void setProgress(double progress, String message);

    void setProgress(double progress, String message, Object... args);

}
