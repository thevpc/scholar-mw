package net.vpc.scholar.hadrumaths.monitors;

/**
 * @author taha.bensalah@gmail.com on 7/22/16.
 */
public class IntIterationComputationMonitorInc implements ComputationMonitorInc {
    private double max;
    private int index;

    public IntIterationComputationMonitorInc(int max) {
        this.max = max;
    }

    @Override
    public double inc(double last) {
        index++;
        return index / max;
    }
}
