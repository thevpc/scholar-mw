package net.thevpc.scholar.hadruplot;

public class AbsolutePlotSamples extends PlotSamples {
    private int dimension;
    private double[] x;
    private double[] y;
    private double[] z;


//    AbsoluteSamples(int dim, double[] x, double[] y, double[] z) {
//        this.x = x;
//        this.y = y;
//        this.z = z;
//        this.dimension = dim;
//    }

    public AbsolutePlotSamples(double[] x, double[] y, double[] z) {
        if(z!=null) {
            if(x==null){
                throw new IllegalArgumentException("x should not be null");
            }
            if(y==null){
                throw new IllegalArgumentException("y should not be null");
            }
            this.x = x;
            this.y = y;
            this.z = z;
            this.dimension = 3;
        }else if (y!=null){
            if(x==null){
                throw new IllegalArgumentException("x should not be null");
            }
            this.x = x;
            this.y = y;
            this.dimension = 2;
        }else if (x!=null){
            this.x = x;
            this.dimension = 1;
        }else{
            throw new IllegalArgumentException("x should not be null");
        }
    }

    AbsolutePlotSamples(double[] x, double[] y) {
        this(x,y,null);
    }

    AbsolutePlotSamples(double[] x) {
        this(x,null,null);
    }

    public int getDimension() {
        return dimension;
    }

    public double[] xvalues() {
        return x;
    }

    public double[] yvalues() {
        if(dimension<2){
            throw new IllegalArgumentException("Unable to get y for dimension "+dimension);
        }
        return y;
    }

    public double[] zvalues() {
        if(dimension<3){
            throw new IllegalArgumentException("Unable to get z for dimension "+dimension);
        }
        return z;
    }

    public double[] getX() {
        return x;
    }

    public double[] getY() {
        if(dimension<2){
            throw new IllegalArgumentException("Unable to get y for dimension "+dimension);
        }
        return y;
    }

    public double[] getZ() {
        if(dimension<3){
            throw new IllegalArgumentException("Unable to get z for dimension "+dimension);
        }
        return z;
    }

    public boolean isAbsolute() {
        return true;
    }

    public boolean isRelative() {
        return false;
    }

    public PlotSamples toRelative() {
        return new RelativePlotSamples(false, dimension, x, y, z);
    }

    public PlotSamples toAbsolute() {
        return this;
    }

    public PlotSamples toAbsolute(PlotDomain domain) {
        return this;
    }
}
