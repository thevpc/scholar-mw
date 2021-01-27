package net.thevpc.scholar.hadruplot;

public class RelativePlotSamples extends PlotSamples {

    private int dimension;
    private double[] x;
    private double[] y;
    private double[] z;

    RelativePlotSamples(boolean absolute, int dim, double[] x, double[] y, double[] z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dim;
    }

    RelativePlotSamples(boolean absolute, double[] x, double[] y, double[] z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = 3;
    }

    RelativePlotSamples(boolean absolute, double[] x, double[] y) {
        this.x = x;
        this.y = y;
        this.dimension = 2;
    }

    RelativePlotSamples(boolean absolute, double[] x) {
        this.x = x;
        this.dimension = 1;
    }

    public int getDimension() {
        return dimension;
    }

    public double[] xvalues() {
        return x;
    }

    public double[] yvalues() {
        return y;
    }

    public double[] zvalues() {
        return z;
    }

    public double[] getX() {
        return x;
    }

    public double[] getY() {
        return y;
    }

    public double[] getZ() {
        return z;
    }

    public boolean isAbsolute() {
        return false;
    }

    public boolean isRelative() {
        return true;
    }

    public PlotSamples toRelative() {
        return this;
    }

    public PlotSamples toAbsolute() {
        return new AbsolutePlotSamples(x, y, z);
    }

    public AbsolutePlotSamples toAbsolute(PlotDomain domain) {
        return domain.toAbsolute(this);
    }
}
