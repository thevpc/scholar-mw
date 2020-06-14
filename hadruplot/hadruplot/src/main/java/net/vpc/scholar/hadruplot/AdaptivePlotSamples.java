package net.vpc.scholar.hadruplot;

public class AdaptivePlotSamples extends PlotSamples {
    private double error = 0.1;
    private int minimumXSamples = 10;
    private int maximumXSamples = 1000;
    int dimension = 1;

    @Override
    public int getDimension() {
        return dimension;
    }

    public double getError() {
        return error;
    }

    public AdaptivePlotSamples setError(double error) {
        this.error = error;
        return this;
    }

    public AdaptivePlotSamples error(double error) {
        this.error = error;
        return this;
    }

    public int getMinimumXSamples() {
        return minimumXSamples;
    }

    public AdaptivePlotSamples setMinimumXSamples(int minimumXSamples) {
        this.minimumXSamples = minimumXSamples;
        return this;
    }

    public AdaptivePlotSamples minimumXSamples(int minimumXSamples) {
        this.minimumXSamples = minimumXSamples;
        return this;
    }

    public int getMaximumXSamples() {
        return maximumXSamples;
    }

    public AdaptivePlotSamples setMaximumXSamples(int maximumXSamples) {
        this.maximumXSamples = maximumXSamples;
        return this;
    }

    public AdaptivePlotSamples maximumXSamples(int maximumXSamples) {
        this.maximumXSamples = maximumXSamples;
        return this;
    }
}
