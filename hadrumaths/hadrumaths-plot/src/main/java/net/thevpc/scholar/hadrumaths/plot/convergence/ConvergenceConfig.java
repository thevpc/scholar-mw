package net.thevpc.scholar.hadrumaths.plot.convergence;

import java.io.PrintStream;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 oct. 2007 00:49:55
 */

public class ConvergenceConfig implements Cloneable {

    private double threshold = 1E-2;
    private int stabilityIterations = 5;
    private int maxIterations = -1;
    private ConvergenceListener listener;

    public ConvergenceConfig() {
        this(1E-2, 5, System.out);
    }

    public ConvergenceConfig(double threshold, int stabilityIterations, PrintStream listener) {
        this(threshold, stabilityIterations, new ConvergenceLogListener(listener));
    }

    public ConvergenceConfig(double threshold, int stabilityIterations, ConvergenceListener listener) {
        this.threshold = threshold;
        this.stabilityIterations = stabilityIterations;
        this.listener = listener;
    }

    public double getThreshold() {
        return threshold;
    }

    public ConvergenceConfig setThreshold(double threshold) {
        this.threshold = threshold;
        return this;
    }

    public int getStabilityIterations() {
        return stabilityIterations;
    }

    public ConvergenceConfig setStabilityIterations(int stabilityIterations) {
        this.stabilityIterations = stabilityIterations;
        return this;
    }

    public ConvergenceConfig copy() {
        return clone();
    }

    public ConvergenceConfig clone() {
        try {
            return (ConvergenceConfig) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e.toString());
        }
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public ConvergenceConfig setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
        return this;
    }

    public ConvergenceListener getListener() {
        return listener;
    }

    public ConvergenceConfig setListener(ConvergenceListener listener) {
        this.listener = listener;
        return this;
    }

    public ConvergenceConfig setListener(PrintStream listener) {
        this.listener = listener == null ? null : new ConvergenceLogListener(listener);
        return this;
    }
}
