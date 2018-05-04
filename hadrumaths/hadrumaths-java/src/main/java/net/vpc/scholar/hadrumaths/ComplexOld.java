package net.vpc.scholar.hadrumaths;

import java.util.Objects;

public final class ComplexOld extends Complex {
    private static final long serialVersionUID=1;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ComplexOld that = (ComplexOld) o;
        return Double.compare(that.real, real) == 0 &&
                Double.compare(that.imag, imag) == 0;
    }

    @Override
    public int hashCode() {

        int hash = 7;
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.imag) ^ (Double.doubleToLongBits(this.imag) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.real) ^ (Double.doubleToLongBits(this.real) >>> 32));
        return hash;
    }
}
