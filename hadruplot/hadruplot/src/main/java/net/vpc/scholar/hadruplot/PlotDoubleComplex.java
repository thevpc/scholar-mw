package net.vpc.scholar.hadruplot;

public class PlotDoubleComplex implements PlotComplex{
    public final double real;
    public final double imag;

    public PlotDoubleComplex(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    @Override
    public double getReal() {
        return real;
    }

    @Override
    public double getImag() {
        return imag;
    }
    
}
