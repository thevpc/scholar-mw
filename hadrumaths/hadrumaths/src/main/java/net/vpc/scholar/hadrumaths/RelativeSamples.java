package net.vpc.scholar.hadrumaths;

public class RelativeSamples extends Samples {
    private int dimension;
    private double[] x;
    private double[] y;
    private double[] z;


    RelativeSamples(boolean absolute, int dim, double[] x, double[] y, double[] z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dim;
    }

    RelativeSamples(boolean absolute, double[] x, double[] y, double[] z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = 3;
    }

    RelativeSamples(boolean absolute, double[] x, double[] y) {
        this.x = x;
        this.y = y;
        this.dimension = 2;
    }

    RelativeSamples(boolean absolute, double[] x) {
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

    public Samples toRelative() {
        return this;
    }

    public Samples toAbsolute() {
        return new AbsoluteSamples(x, y, z);
    }

    public AbsoluteSamples toAbsolute(Domain domain) {
        return domain.toAbsolute(this);
    }
}
