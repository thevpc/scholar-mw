package net.vpc.scholar.hadrumaths;

//import net.vpc.scholar.hadrumaths.util.DevUtils;

/**
 * Created by vpc on 1/1/17.
 */
public class MutableComplex {
    private double real;
    private double imag;

    public static MutableComplex[] createArray(Complex value, int size) {
        MutableComplex[] arr = new MutableComplex[size];
        for (int i = 0; i < size; i++) {
            arr[i] = new MutableComplex(value);
        }
        return arr;
    }

    public static MutableComplex[][] createArray(Complex value, int size1, int size2) {
        double real = value.getReal();
        double imag = value.getImag();
        MutableComplex[][] arr = new MutableComplex[size1][size2];
        for (int i = 0; i < size1; i++) {
            MutableComplex[] a_i = arr[i];
            for (int j = 0; j < size2; j++) {
                a_i[j] = new MutableComplex(real, imag);
            }
        }
        return arr;
    }

    public static MutableComplex[][][] createArray(Complex value, int size1, int size2, int size3) {
        double real = value.getReal();
        double imag = value.getImag();
        MutableComplex[][][] arr = new MutableComplex[size1][size2][size3];
        for (int i = 0; i < size1; i++) {
            MutableComplex[][] a_i = arr[i];
            for (int j = 0; j < size2; j++) {
                MutableComplex[] a_ij = a_i[j];
                for (int k = 0; k < size3; k++) {
                    a_ij[k] = new MutableComplex(real, imag);
                }
            }
        }
        return arr;
    }

    public static Complex toComplex(MutableComplex t) {
        return t.toComplex();
    }

    public static Complex toImmutable(MutableComplex t) {
        return t.toComplex();
    }

    public static Complex[] toImmutable(MutableComplex[] t) {
        return toComplex(t);
    }

    public static Complex[] toComplex(MutableComplex[] t) {
        Complex[] all = new Complex[t.length];
        int size1 = all.length;
        for (int i = 0; i < size1; i++) {
            MutableComplex c = t[i];
            all[i] = c.toComplex();
        }
        return all;
    }

    public static Complex[][] toImmutable(MutableComplex[][] t) {
        return toComplex(t);
    }

    public static Complex[][] toComplex(MutableComplex[][] t) {
        Complex[][] all = new Complex[t.length][];
        int size1 = all.length;
        for (int i = 0; i < size1; i++) {
            all[i] = toComplex(t[i]);
        }
        return all;
    }

    public static Complex[][][] toImmutable(MutableComplex[][][] t) {
        return toComplex(t);
    }

    public static Complex[][][] toComplex(MutableComplex[][][] t) {
        Complex[][][] all = new Complex[t.length][][];
        int size1 = all.length;
        for (int i = 0; i < size1; i++) {
            all[i] = toComplex(t[i]);
        }
        return all;
    }

    public static MutableComplex toMutableComplex(Complex t) {
        return new MutableComplex(t);
    }

    public static MutableComplex[] toMutableComplex(Complex[] t) {
        MutableComplex[] all = new MutableComplex[t.length];
        int size1 = all.length;
        for (int i = 0; i < size1; i++) {
            Complex c = t[i];
            all[i] = new MutableComplex(c);
        }
        return all;
    }

    public static MutableComplex[][] toMutableComplex(Complex[][] t) {
        MutableComplex[][] all = new MutableComplex[t.length][];
        int size1 = all.length;
        for (int i = 0; i < size1; i++) {
            all[i] = toMutableComplex(t[i]);
        }
        return all;
    }

    public static MutableComplex[][][] toMutableComplex(Complex[][][] t) {
        MutableComplex[][][] all = new MutableComplex[t.length][][];
        int size1 = all.length;
        for (int i = 0; i < size1; i++) {
            all[i] = toMutableComplex(t[i]);
        }
        return all;
    }

    public static MutableComplex forComplex(Complex c) {
        return new MutableComplex(c);
    }

    public static MutableComplex Zero() {
        return new MutableComplex(0, 0);
    }

    public static MutableComplex One() {
        return new MutableComplex(1, 0);
    }

    public static MutableComplex I() {
        return new MutableComplex(0, 1);
    }

    public MutableComplex() {

    }

    public MutableComplex(Complex c) {
        this.real = c.getReal();
        this.imag = c.getImag();
        //DevUtils.run(this::debug_check);
    }

    public MutableComplex(double real, double imag) {
        this.real = real;
        this.imag = imag;
        //DevUtils.run(this::debug_check);
    }

    public void addProduct(Complex complex1, MutableComplex complex2) {
        double r1 = complex1.getReal();
        double r2 = complex2.real;
        double i1 = complex1.getImag();
        double i2 = complex2.imag;
        this.real = r1 * r2 - i1 * i2;
        this.imag = r1 * i2 + i1 * r2;
//        return this;
        //DevUtils.run(this::debug_check);
    }

    public void addProduct(Complex complex1, Complex complex2) {
        double r1 = complex1.getReal();
        double r2 = complex2.getReal();
        double i1 = complex1.getImag();
        double i2 = complex2.getImag();
        this.real += r1 * r2 - i1 * i2;
        this.imag += r1 * i2 + i1 * r2;
//        return this;
        //DevUtils.run(this::debug_check);
    }

    public void addProduct(Complex complex1, Complex complex2, Complex complex3) {
        double a = complex1.getReal();
        double b = complex1.getImag();
        double c = complex2.getReal();
        double d = complex2.getImag();
        double A = a * c - b * d;
        double B = a * d + b * c;
        double C = complex3.getReal();
        double D = complex3.getImag();
        this.real += A * C - B * D;
        this.imag += A * D + B * C;
//        return this;
        //DevUtils.run(this::debug_check);
    }

    public void addProduct(MutableComplex complex1, Complex complex2) {
        double r1 = complex1.real;
        double r2 = complex2.getReal();
        double i1 = complex1.imag;
        double i2 = complex2.getImag();
        this.real += r1 * r2 - i1 * i2;
        this.imag += r1 * i2 + i1 * r2;
//        return this;
        //DevUtils.run(this::debug_check);
    }

    public void addProduct(MutableComplex complex1, MutableComplex complex2) {
        double a = complex1.real;
        double b = complex1.imag;
        double c = complex2.real;
        double d = complex2.imag;
        this.real += a * c - b * d;
        this.imag += a * d + b * c;
//        return this;
        //DevUtils.run(this::debug_check);
    }

    public void add(Complex complex) {
        this.real += complex.getReal();
        this.imag += complex.getImag();
//        return this;
        //DevUtils.run(this::debug_check);
    }

    public void add(MutableComplex complex) {
        this.real += complex.real;
        this.imag += complex.real;
//        return this;
        //DevUtils.run(this::debug_check);
    }

    public void add(double real, double imag) {
        this.real += real;
        this.imag += imag;
//        return this;
        //DevUtils.run(this::debug_check);
    }

    public void mul(double real) {
        this.real = this.real * real;
        this.imag = this.imag * real;
//        return this;
        //DevUtils.run(this::debug_check);
    }

//    public void mul(double real,double imag){
//        double a = this.real;
//        double b = this.imag;
//        this.real=a * real - b * imag;
//        this.imag=a * imag + b * real;
////        return this;
//        //DevUtils.run(this::debug_check);
//    }

    public void mul(MutableComplex complex) {
        mul(complex.real, complex.imag);
//        return this;
        //DevUtils.run(this::debug_check);
    }

    public void mul(Complex complex) {
        mul(complex.getReal(), complex.getImag());
    }

    public void mul(double r, double i) {
        double r0 = this.real;
        double i0 = this.imag;
        if (i0 == 0) {
            if (i == 0) {
                //real * real
                this.real = r0 * r;
                this.imag = 0;
            } else if (r == 0) {
                //real * imag
                this.real = 0;
                this.imag = r0 * i;
            } else {
                //real * complex
                this.real = r0 * r;
                this.imag = r0 * i;
            }
        } else if (r0 == 0) {
            if (i == 0) {
                //imag * real
                this.real = 0;
                this.imag = i0 * r;
            } else if (r == 0) {
                //imag * imag
                this.real = -i0 * i;
                this.imag = 0;
            } else {
                //imag * complex
                this.real = -i0 * i;
                this.imag = i0 * r;
            }
        } else {
            this.real = r0 * r - i0 * i;
            this.imag = r0 * i + i0 * r;
        }
//        return this;
        //DevUtils.run(this::debug_check);
    }

    //////////////////////////////////////

    public void div(double real) {
        this.real = this.real / real;
        this.imag = this.imag / real;
//        return this;
        //DevUtils.run(this::debug_check);
    }

    public void div(double real, double imag) {
        double a = this.real;
        double b = this.imag;
        double c2d2 = real * real + imag * imag;
        this.real = (a * real + b * imag) / c2d2;
        this.imag = (b * real - a * imag) / c2d2;
//        return this;
        //DevUtils.run(this::debug_check);
    }

    public void div(MutableComplex other) {
        double a = this.real;
        double b = this.imag;
        double c = other.real;
        double d = other.imag;
        double c2d2 = c * c + d * d;
        this.real = (a * c + b * d) / c2d2;
        this.imag = (b * c - a * d) / c2d2;
//        return this;
        //DevUtils.run(this::debug_check);
    }

    public void div(Complex other) {
        double a = this.real;
        double b = this.imag;
        double c = other.getReal();
        double d = other.getImag();
        double c2d2 = c * c + d * d;
        this.real = (a * c + b * d) / c2d2;
        this.imag = (b * c - a * d) / c2d2;
//        return this;
        //DevUtils.run(this::debug_check);
    }

    ////////////////////////////////////////////


    public void add(double real) {
        this.real += real;
//        return this;
        //DevUtils.run(this::debug_check);
    }

    public void sub(Complex complex) {
        this.real -= complex.getReal();
        this.imag -= complex.getImag();
//        return this;
        //DevUtils.run(this::debug_check);
    }

    public void sub(MutableComplex complex) {
        this.real -= complex.real;
        this.imag -= complex.imag;
//        return this;
        //DevUtils.run(this::debug_check);
    }

    public void sub(double real, double imag) {
        this.real -= real;
        this.imag -= imag;
//        return this;
        //DevUtils.run(this::debug_check);
    }

    public void sub(double real) {
        this.real -= real;
//        return this;
        //DevUtils.run(this::debug_check);
    }

    public void zero() {
        real = 0;
        imag = 0;
//        return this;
    }

    public void one() {
        real = 1;
        imag = 0;
//        return this;
    }

    public void i() {
        real = 0;
        imag = 1;
//        return this;
    }

    public boolean isZero() {
        return real == 0 && imag == 0;
    }

    public Complex toComplex() {
        return Complex.valueOf(real, imag);
    }

    public Complex toImmutable() {
        return Complex.valueOf(real, imag);
    }

    public void exp() {
        double e = MathsBase.exp(real);
        double r = e * MathsBase.cos2(imag);
        double i = e * MathsBase.sin2(this.imag);
        this.real = r;
        this.imag = i;
        debug_check();
//        return this;
    }

    public void neg() {
        real = -real;
        imag = -imag;
//        return this;
        //DevUtils.run(this::debug_check);
    }

    @Override
    public String toString() {
        return toComplex().toString();
//        if (Double.isNaN(real) && Double.isNaN(imag)) {
//            return String.valueOf(imag);
//        }
//        if (Double.isNaN(real)) {
//            return String.valueOf(imag) + "i";
//        }
//        if (Double.isNaN(imag)) {
//            return String.valueOf(real);
//        }
//        String imag_string = String.valueOf(imag);
//        String real_string = String.valueOf(real);
//        if (imag == 0) {
//            return real_string;
//        } else if (real == 0) {
//            return (imag == 1) ? "i" : (imag == -1) ? "-i" : (imag_string + "i");
//        } else {
//            return real_string
//                    + ((imag == 1) ? "+i" : (imag == -1) ? "-i" : (imag > 0) ? ("+" + (imag_string + "i")) : (imag_string + "i"));
//        }
    }

    public void set(Complex complex) {
        this.real = complex.realdbl();
        this.imag = complex.imagdbl();
//        return this;
    }

    public void setZero() {
        this.real = 0;
        this.imag = 0;
    }

    public void setOne() {
        this.real = 1;
        this.imag = 0;
    }

    public void set(double real, double imag) {
        this.real = real;
        this.imag = imag;
//        return this;
    }

    public double sqrtDouble() {
        if (imag == 0) {
            if (real >= 0) {
                return MathsBase.sqrt(real);
            } else {
                return Double.NaN;
            }
        } else {
            return Double.NaN;
        }
    }

    public void sqrt() {
        if (imag == 0) {
            if (real >= 0) {
                this.real = MathsBase.sqrt(real);
            } else {
                set(0, MathsBase.sqrt(-real));
            }
        } else {
            double r = MathsBase.sqrt(abs());
            double theta = angle() / 2;
            set(r * MathsBase.cos2(theta), r * MathsBase.sin2(theta));
        }
//        return this;
    }

    public void sqrt(int n) {
        if (n == 0) {
            set(1, 0);
        }
        pow(1.0 / n);
    }


    public void pow(double power) {
        if (power == 0) {
            set(1, 0);
        } else if (power == 1) {
            //do nthing
        } else if (power == -1) {
            inv();
//        } else if (imag == 0) {
//            return real >= 0 ? new Complex(MathsBase.pow(real, power), 0) : new Complex(0, MathsBase.pow(-real, power));
        } else if (power >= 0) {
            double r = MathsBase.pow(abs(), power);
            double angle = angle();
            double theta = angle * power;
            this.real = r * MathsBase.cos2(theta);
            this.imag = r * MathsBase.sin2(theta);
        } else { //n<0
            power = -power;
            double r = MathsBase.pow(abs(), power);
            double theta = angle() * power;
            this.real = r * MathsBase.cos2(theta);
            this.imag = r * MathsBase.sin2(theta);
            inv();
        }
//        return this;
    }

    public void log() {
        set(Math.log(abs()), Math.atan2(imag, real));
//        return this;
    }

    public void log10() {
        set(Math.log(abs()), Math.atan2(imag, real));
        div(Math.log(10));
    }

    public void db() {
        log10();
        mul(10);
    }


    public void pow(Complex y) {
        double imag = y.getImag();
        if (imag == 0) {
            pow(y.getReal());
        } else {
            double real = y.getReal();
            //x^y=exp(y*ln(x))
            log();
            mul(real, imag);
            exp();
        }
    }


    public double abs() {
        return MathsBase.sqrt(real * real + imag * imag);
    }

    public void inv() {
        if (real == 0) {
            this.real = 0;
            this.imag = -1 / imag;
        } else if (imag == 0) {
            this.real = 1 / real;
            this.imag = 0;
        } else {
            double d = real * real + imag * imag;
            this.real = real / d;
            this.imag = -imag / d;
        }
        //DevUtils.run(this::debug_check);
    }

    public double angle() {
        //workaround
//        if(real==0){
//            return imag>=0?MathsBase.PI/2:-MathsBase.PI/2;
//        }else if(imag==0){
//            return real>=0?0:MathsBase.PI;
//        }
        return MathsBase.atan2(imag, real);
    }

    public double getReal() {
        return real;
    }

    public double getImag() {
        return imag;
    }

    private void debug_check() {
        if (Double.isInfinite(real) && Double.isNaN(imag)) {
            System.err.println("infinite real, NaN imag");
        } else if (Double.isInfinite(imag) && Double.isNaN(real)) {
            System.err.println("infinite imag, NaN real");
        }
    }
}
