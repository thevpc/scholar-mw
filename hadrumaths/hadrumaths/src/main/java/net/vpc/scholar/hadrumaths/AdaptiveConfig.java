package net.vpc.scholar.hadrumaths;

public class AdaptiveConfig {
    private double error = 0.1;
    private int minimumXSamples = 10;
    private int maximumXSamples = 1000;
    private SamplifyListener listener;

    public double getError() {
        return error;
    }

    public AdaptiveConfig setError(double error) {
        this.error = error;
        return this;
    }

    public int getMinimumXSamples() {
        return minimumXSamples;
    }

    public AdaptiveConfig setMinimumXSamples(int minimumXSamples) {
        this.minimumXSamples = minimumXSamples;
        return this;
    }

    public int getMaximumXSamples() {
        return maximumXSamples;
    }

    public AdaptiveConfig setMaximumXSamples(int maximumXSamples) {
        this.maximumXSamples = maximumXSamples;
        return this;
    }

    public SamplifyListener getListener() {
        return listener;
    }

    public AdaptiveConfig setListener(SamplifyListener listener) {
        this.listener = listener;
        return this;
    }
}
