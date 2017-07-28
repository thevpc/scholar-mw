package net.vpc.scholar.hadrumaths;

public class AdaptiveSamples extends Samples {
    private double error=0.1;
    private int minimumXSamples=10;
    private int maximumXSamples=1000;
    int dimension=1;

    @Override
    public int getDimension() {
        return dimension;
    }

    public double getError() {
        return error;
    }

    public AdaptiveSamples setError(double error) {
        this.error = error;
        return this;
    }

    public AdaptiveSamples error(double error) {
        this.error = error;
        return this;
    }

    public int getMinimumXSamples() {
        return minimumXSamples;
    }

    public AdaptiveSamples setMinimumXSamples(int minimumXSamples) {
        this.minimumXSamples = minimumXSamples;
        return this;
    }

    public AdaptiveSamples minimumXSamples(int minimumXSamples) {
        this.minimumXSamples = minimumXSamples;
        return this;
    }

    public int getMaximumXSamples() {
        return maximumXSamples;
    }

    public AdaptiveSamples setMaximumXSamples(int maximumXSamples) {
        this.maximumXSamples = maximumXSamples;
        return this;
    }

    public AdaptiveSamples maximumXSamples(int maximumXSamples) {
        this.maximumXSamples = maximumXSamples;
        return this;
    }
}
