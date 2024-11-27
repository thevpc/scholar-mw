/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.thevpc.scholar.hadrumaths.convergence;

import net.thevpc.scholar.hadrumaths.Equalator;

/**
 *
 * @author vpc
 */
public class ConvergenceConfig<T> {

    private int stability;
    private long startEpoch;
    private long maxEpochs;
    private long maxTimeMs;
    private Equalator<T> equalator;
    private ConvergenceEvaluator<T> evaluator;
    private ConvergenceListener<T> listener;

    public ConvergenceConfig() {
    }

    public long getMaxTimeMs() {
        return maxTimeMs;
    }

    public ConvergenceConfig<T> setMaxTimeMs(long maxTimeMs) {
        this.maxTimeMs = maxTimeMs;
        return this;
    }

    public long getMaxEpochs() {
        return maxEpochs;
    }

    public void setMaxEpochs(long maxIterations) {
        this.maxEpochs = maxIterations;
    }

    public ConvergenceEvaluator<T> getEval() {
        return evaluator;
    }

    public ConvergenceConfig<T> setEval(ConvergenceEvaluator<T> iteration) {
        this.evaluator = iteration;
        return this;
    }

    public long getStartEpoch() {
        return startEpoch;
    }

    public ConvergenceConfig<T> setStartEpoch(long startIteration) {
        this.startEpoch = startIteration;
        return this;
    }

    public ConvergenceConfig<T> setStability(int stability) {
        this.stability = stability;
        return this;
    }

    public ConvergenceConfig<T> setEqualator(Equalator<T> equalator) {
        this.equalator = equalator;
        return this;
    }

    public int getStability() {
        return stability;
    }

    public Equalator<T> getEqualator() {
        return equalator;
    }

    public ConvergenceListener<T> getListener() {
        return listener;
    }

    public ConvergenceConfig<T> setListener(ConvergenceListener<T> listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public String toString() {
        return "ConvergenceConfig{" + "stability=" + stability + ", startEpoch=" + startEpoch + ", maxEpochs=" + maxEpochs + ", maxTimeMs=" + maxTimeMs + ", equalator=" + equalator + ", iteration=" + evaluator + ", listener=" + listener + '}';
    }

}
