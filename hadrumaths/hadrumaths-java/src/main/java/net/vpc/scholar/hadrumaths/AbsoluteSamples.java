package net.vpc.scholar.hadrumaths;

public class AbsoluteSamples extends Samples {
    private int dimension;
    private double[] x;
    private double[] y;
    private double[] z;


    AbsoluteSamples(boolean absolute, int dim, double[] x, double[] y, double[] z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dim;
    }

    AbsoluteSamples(boolean absolute, double[] x, double[] y, double[] z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = 3;
    }

    AbsoluteSamples(boolean absolute, double[] x, double[] y) {
        this.x = x;
        this.y = y;
        this.dimension = 2;
    }

    AbsoluteSamples(boolean absolute, double[] x) {
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
        return true;
    }

    public boolean isRelative() {
        return false;
    }

    public Samples toRelative() {
        return new RelativeSamples(false, dimension, x, y, z);
    }
    public Samples toAbsolute() {
        return this;
    }
    public Samples toAbsolute(Domain domain) {
        return this;
    }
}
