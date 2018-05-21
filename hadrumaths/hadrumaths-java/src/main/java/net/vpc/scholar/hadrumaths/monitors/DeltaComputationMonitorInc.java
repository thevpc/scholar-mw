package net.vpc.scholar.hadrumaths.monitors;

import net.vpc.scholar.hadrumaths.monitors.ComputationMonitorInc;

/**
 * @author taha.bensalah@gmail.com on 7/22/16.
 */
public class DeltaComputationMonitorInc implements ComputationMonitorInc {
    private double delta;

    public DeltaComputationMonitorInc(double delta) {
        this.delta = delta;
    }

    @Override
    public double inc(double last) {
        return last + delta;
    }
}
