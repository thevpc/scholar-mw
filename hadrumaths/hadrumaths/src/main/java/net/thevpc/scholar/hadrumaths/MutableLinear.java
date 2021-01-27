package net.thevpc.scholar.hadrumaths;

//import net.thevpc.scholar.hadrumaths.util.DevUtils;

import net.thevpc.scholar.hadrumaths.symbolic.double2double.Linear;

/**
 * Created by vpc on 1/1/17.
 */
public class MutableLinear {
    private double a;
    private double b;
    private double c;
    private Domain domain = Domain.FULLX;

    public MutableLinear() {

    }

    public MutableLinear(Linear c) {
        this.a = c.getA();
        this.b = c.getB();
        this.c = c.getC();
        this.domain = c.getDomain();
        //DevUtils.run(this::debug_check);
    }

    public MutableLinear(double a, double b, double c, Domain domain) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.domain = domain;
    }

    public static MutableLinear[] createArray(Linear value, int size) {
        MutableLinear[] arr = new MutableLinear[size];
        for (int i = 0; i < size; i++) {
            arr[i] = new MutableLinear(value);
        }
        return arr;
    }

    public static Linear toLinear(MutableLinear t) {
        return t.toLinear();
    }

    public Linear toLinear() {
        return new Linear(a, b, c, domain);
    }

    public static MutableLinear toMutable(Linear t) {
        return new MutableLinear(t);
    }

    public static MutableLinear[] toMutable(Linear[] t) {
        MutableLinear[] all = new MutableLinear[t.length];
        int size1 = all.length;
        for (int i = 0; i < size1; i++) {
            Linear c = t[i];
            all[i] = new MutableLinear(c);
        }
        return all;
    }

    public static MutableLinear of(Linear c) {
        return new MutableLinear(c);
    }

    public static MutableLinear Zero() {
        return new MutableLinear();
    }

    public void addA(double a) {
        this.a += a;
    }

    public void addB(double b) {
        this.b += b;
    }

    public void addC(double c) {
        this.c += c;
    }

    public double getA() {
        return a;
    }

    public MutableLinear setA(double a) {
        this.a = a;
        return this;
    }

    public double getB() {
        return b;
    }

    public MutableLinear setB(double b) {
        this.b = b;
        return this;
    }

    public double getC() {
        return c;
    }

    public MutableLinear setC(double c) {
        this.c = c;
        return this;
    }

    public void mul(double real) {
        this.a = this.a * real;
        this.b = this.b * real;
        this.c = this.c * real;
    }

    public void div(double real) {
        this.a = this.a / real;
        this.b = this.b / real;
        this.c = this.c / real;
    }

    public void subA(double real) {
        this.a -= real;
    }

    public void subB(double real) {
        this.b -= real;
    }

    public void subC(double real) {
        this.c -= real;
    }

    public void sub(double real) {
        this.c -= real;
    }

    public void zero() {
        a = 0;
        b = 0;
        c = 0;
    }

    public boolean isZero() {
        return a == 0 && b == 0 && c == 0;
    }

    public Linear toImmutable() {
        return toLinear();
    }

    @Override
    public String toString() {
        return toLinear().toString();
    }

    public void set(Linear linear) {
        this.a = linear.getA();
        this.b = linear.getB();
        this.c = linear.getC();
        this.domain = linear.getDomain();
    }

    public void add(Linear linear) {
        this.a += linear.getA();
        this.b += linear.getB();
        this.c += linear.getC();
    }

    public void sum(Linear linear) {
        this.a -= linear.getA();
        this.b -= linear.getB();
        this.c -= linear.getC();
    }

    public void setZero() {
        this.a = 0;
        this.b = 0;
        this.c = 0;
    }


}
