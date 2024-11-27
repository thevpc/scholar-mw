/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.thevpc.scholar.hadrumaths.convergence;

import net.thevpc.nuts.time.NDuration;
import net.thevpc.scholar.hadrumaths.EqualatorResult;

/**
 *
 * @author vpc
 */
public class ConvergenceResult<T> {

    private T value;
    private ConvergenceConfig config;
    private long epoch;
    private NDuration duration;
    private EqualatorResult result;

    public ConvergenceResult(T value, ConvergenceConfig config, long epoch, EqualatorResult result, NDuration duration) {
        this.value = value;
        this.result = result;
        this.epoch = epoch;
        this.config = config;
        this.duration = duration;
    }

    public double isPrecision() {
        return result.getPrecision();
    }

    public boolean isConverged() {
        return result.isEquals();
    }

    public ConvergenceConfig getConfig() {
        return config;
    }

    public T getValue() {
        return value;
    }

    public long getEpoch() {
        return epoch;
    }

    public NDuration getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "ConvergenceResult{"
                + "status=" + (result.isEquals() ? "converged" : "non-converged")
                + ", value=" + value
                + ", epoch=" + epoch
                + ", precision=" + result.getPrecision()
                + ", duration=" + duration
                + ", config=" + config
                + ", iteration=" + epoch
                + '}';
    }

}
