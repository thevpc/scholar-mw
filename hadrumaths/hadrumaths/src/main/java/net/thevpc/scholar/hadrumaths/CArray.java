package net.thevpc.scholar.hadrumaths;

/**
 * User: vpc
 * Date: 7 juin 2005
 * Time: 17:58:05
 */
public class CArray {
    private final Complex[] array;

    public CArray(CArray other) {
        this.array = other.getArrayCopy();
    }

    public Complex[] getArrayCopy() {
        Complex[] n = new Complex[array.length];
        System.arraycopy(array, 0, n, 0, n.length);
        return n;
    }

    public CArray(double[] c) {
        this.array = new Complex[c.length];
        for (int i = 0; i < this.array.length; i++) {
            this.array[i] = Complex.of(c[i]);
        }
    }

    public CArray(int dim, Complex value) {
        this.array = new Complex[dim];
        for (int i = 0; i < dim; i++) {
            this.array[i] = value;
        }
    }

    public CArray(int dim, double value) {
        this.array = new Complex[dim];
        Complex v = Complex.of(value);
        for (int i = 0; i < dim; i++) {
            this.array[i] = v;
        }
    }

    public CArray(Complex[] array) {
        this.array = array;
    }

    public Complex[] getArray() {
        return array;
    }

    public CArray getCopy() {
        return new CArray(this);
    }

    public double[] absdbl() {
        double[] ret = new double[array.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = array[i].absdbl();
        }
        return ret;
    }

    public double[] getReal() {
        double[] ret = new double[array.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = array[i].getReal();
        }
        return ret;
    }

    public double[] getImag() {
        double[] ret = new double[array.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = array[i].getImag();
        }
        return ret;
    }

    // *******************************  ADD
    public CArray addSelf(Complex b) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].plus(b);
        }
        return this;
    }

    public CArray addSelf(double b) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].plus(b);
        }
        return this;
    }

    public CArray addSelf(double[] b) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].plus(b[i]);
        }
        return this;
    }

    public CArray addSelf(Complex[] b) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].plus(b[i]);
        }
        return this;
    }

    public CArray addSelf(CArray b) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].plus(b.array[i]);
        }
        return this;
    }

    public CArray add(Complex b) {
        Complex[] array2 = new Complex[array.length];
        for (int i = 0; i < array2.length; i++) {
            array2[i] = array[i].plus(b);
        }
        return new CArray(array2);
    }

    public CArray add(double b) {
        Complex[] array2 = new Complex[array.length];
        for (int i = 0; i < array2.length; i++) {
            array2[i] = array[i].plus(b);
        }
        return new CArray(array2);
    }

    public CArray add(double[] b) {
        Complex[] array2 = new Complex[array.length];
        for (int i = 0; i < array2.length; i++) {
            array2[i] = array[i].plus(b[i]);
        }
        return new CArray(array2);
    }

    public CArray add(Complex[] b) {
        Complex[] array2 = new Complex[array.length];
        for (int i = 0; i < array2.length; i++) {
            array2[i] = array[i].plus(b[i]);
        }
        return new CArray(array2);
    }

    public CArray add(CArray b) {
        Complex[] array2 = new Complex[array.length];
        for (int i = 0; i < array.length; i++) {
            array2[i] = array[i].plus(b.array[i]);
        }
        return new CArray(array2);
    }


    // *******************************  SUBSTRUCT
    public CArray subSelf(Complex b) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].minus(b);
        }
        return this;
    }

    public CArray subSelf(double b) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].sub(b);
        }
        return this;
    }

    public CArray subSelf(double[] b) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].sub(b[i]);
        }
        return this;
    }

    public CArray subSelf(Complex[] b) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].minus(b[i]);
        }
        return this;
    }

    public CArray subSelf(CArray b) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].minus(b.array[i]);
        }
        return this;
    }

    public CArray sub(Complex b) {
        Complex[] array2 = new Complex[array.length];
        for (int i = 0; i < array2.length; i++) {
            array2[i] = array[i].minus(b);
        }
        return new CArray(array2);
    }

    public CArray sub(double b) {
        Complex[] array2 = new Complex[array.length];
        for (int i = 0; i < array2.length; i++) {
            array2[i] = array[i].sub(b);
        }
        return new CArray(array2);
    }

    public CArray sub(double[] b) {
        Complex[] array2 = new Complex[array.length];
        for (int i = 0; i < array2.length; i++) {
            array2[i] = array[i].sub(b[i]);
        }
        return new CArray(array2);
    }

    public CArray sub(Complex[] b) {
        Complex[] array2 = new Complex[array.length];
        for (int i = 0; i < array2.length; i++) {
            array2[i] = array[i].minus(b[i]);
        }
        return new CArray(array2);
    }

    public CArray sub(CArray b) {
        Complex[] array2 = new Complex[array.length];
        for (int i = 0; i < array.length; i++) {
            array2[i] = array[i].minus(b.array[i]);
        }
        return new CArray(array2);
    }

    // *******************************  MULTIPLY
    public CArray mulSelf(Complex b) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].mul(b);
        }
        return this;
    }

    public CArray mulSelf(double b) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].mul(b);
        }
        return this;
    }

    public CArray mulSelf(double[] b) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].mul(b[i]);
        }
        return this;
    }

    public CArray mulSelf(Complex[] b) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].mul(b[i]);
        }
        return this;
    }

    public CArray mulSelf(CArray b) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].mul(b.array[i]);
        }
        return this;
    }

    public CArray mul(Complex b) {
        Complex[] array2 = new Complex[array.length];
        for (int i = 0; i < array2.length; i++) {
            array2[i] = array[i].mul(b);
        }
        return new CArray(array2);
    }

    public CArray mul(double b) {
        Complex[] array2 = new Complex[array.length];
        for (int i = 0; i < array2.length; i++) {
            array2[i] = array[i].mul(b);
        }
        return new CArray(array2);
    }

    public CArray mul(double[] b) {
        Complex[] array2 = new Complex[array.length];
        for (int i = 0; i < array2.length; i++) {
            array2[i] = array[i].mul(b[i]);
        }
        return new CArray(array2);
    }

    public CArray mul(Complex[] b) {
        Complex[] array2 = new Complex[array.length];
        for (int i = 0; i < array2.length; i++) {
            array2[i] = array[i].mul(b[i]);
        }
        return new CArray(array2);
    }

    public CArray mul(CArray b) {
        Complex[] array2 = new Complex[array.length];
        for (int i = 0; i < array.length; i++) {
            array2[i] = array[i].mul(b.array[i]);
        }
        return new CArray(array2);
    }


    // *******************************  MULTIPLY
    public CArray divSelf(Complex b) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].div(b);
        }
        return this;
    }

    public CArray divSelf(double b) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].div(b);
        }
        return this;
    }

    public CArray divSelf(double[] b) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].div(b[i]);
        }
        return this;
    }

    public CArray divSelf(Complex[] b) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].div(b[i]);
        }
        return this;
    }

    public CArray divSelf(CArray b) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].div(b.array[i]);
        }
        return this;
    }

    public CArray div(Complex b) {
        Complex[] array2 = new Complex[array.length];
        for (int i = 0; i < array2.length; i++) {
            array2[i] = array[i].div(b);
        }
        return new CArray(array2);
    }

    public CArray div(double b) {
        Complex[] array2 = new Complex[array.length];
        for (int i = 0; i < array2.length; i++) {
            array2[i] = array[i].div(b);
        }
        return new CArray(array2);
    }

    public CArray div(double[] b) {
        Complex[] array2 = new Complex[array.length];
        for (int i = 0; i < array2.length; i++) {
            array2[i] = array[i].div(b[i]);
        }
        return new CArray(array2);
    }

    public CArray div(Complex[] b) {
        Complex[] array2 = new Complex[array.length];
        for (int i = 0; i < array2.length; i++) {
            array2[i] = array[i].div(b[i]);
        }
        return new CArray(array2);
    }

    public CArray div(CArray b) {
        Complex[] array2 = new Complex[array.length];
        for (int i = 0; i < array.length; i++) {
            array2[i] = array[i].div(b.array[i]);
        }
        return new CArray(array2);
    }

}
