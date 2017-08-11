package net.vpc.scholar.hadrumaths;

public final class ComplexOld extends Complex {
    private double real;
    private double imag;

    public ComplexOld(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }


    @Override
    public double getImag() {
        return imag;
    }

    @Override
    public double getReal() {
        return real;
    }
}
