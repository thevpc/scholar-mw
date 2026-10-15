package net.thevpc.ntexup.extension.antennas.phys;

public final class Complex2 {
    final double re, im;

    public Complex2(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public static Complex2 of(double re) {
        return new Complex2(re, 0);
    }

    public static Complex2 polar(double r, double a) {
        return new Complex2(r * Math.cos(a), r * Math.sin(a));
    }

    /**
     * exp(j x)
     */
    public static Complex2 ej(double x) {
        return new Complex2(Math.cos(x), Math.sin(x));
    }

    public Complex2 add(Complex2 o) {
        return new Complex2(re + o.re, im + o.im);
    }

    public Complex2 sub(Complex2 o) {
        return new Complex2(re - o.re, im - o.im);
    }

    public Complex2 mul(Complex2 o) {
        return new Complex2(re * o.re - im * o.im, re * o.im + im * o.re);
    }

    public Complex2 mul(double k) {
        return new Complex2(re * k, im * k);
    }

    public Complex2 div(Complex2 o) {
        double d = o.re * o.re + o.im * o.im;
        return new Complex2((re * o.re + im * o.im) / d, (im * o.re - re * o.im) / d);
    }

    public Complex2 inv() {
        return of(1).div(this);
    }

    public double abs() {
        return Math.hypot(re, im);
    }

    public double arg() {
        return Math.atan2(im, re);
    }

    @Override
    public String toString() {
        return AntennaKit.fmt(re, 1) + (im < 0 ? " - j" : " + j") + AntennaKit.fmt(Math.abs(im), 1);
    }
}
