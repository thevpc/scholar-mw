package net.vpc.scholar.hadrumaths.util;

/**
 * @author taha.bensalah@gmail.com on 7/22/16.
 */
public class LongIterationComputationMonitorInc implements ComputationMonitorInc {
    private double max;
    private long index;

    public LongIterationComputationMonitorInc(long max) {
        this.max = max;
    }

    @Override
    public double inc(double last) {
        index++;
        return index/max;
    }
}
