/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.thevpc.scholar.hadrumaths;

/**
 *
 * @author vpc
 */
public class Equalators {

    public static Equalator<Complex> ofComplexPrecision(int digits) {
        return ofComplex(Math.pow(10, -Math.abs(digits)));
    }

    public static Equalator<Complex> ofComplex(double relativePrecition) {
        return new ComplexEqualator(relativePrecition);
    }

    public static DoubleEqualator ofDouble(double relativePrecition) {
        return new DoubleEqualatorImpl(relativePrecition);
    }

    private static class ComplexEqualator implements Equalator<Complex> {

        private final double relativePrecition;

        public ComplexEqualator(double relativePrecition) {
            this.relativePrecition = relativePrecition;
        }

        @Override
        public EqualatorResult equals(Complex a, Complex b) {
            if (a == b) {
                return EqualatorResult.EQUALS;
            }
            if (a == null || b == null) {
                return EqualatorResult.DIFFERENT;
            }
            if (a.isNaN() && b.isNaN()) {
                DoubleEqualator u = Equalators.ofDouble(relativePrecition);
                return u.equals(a.getReal(), b.getReal())
                        .max(u.equals(a.getReal(), b.getReal()));
            }
            if (a.isNaN() || b.isNaN()) {
                return EqualatorResult.DIFFERENT;
            }
            if (a.isInfinite() || b.isInfinite()) {
                return EqualatorResult.DIFFERENT;
            }
            if (a.isZero()) {
                return EqualatorResult.DIFFERENT;
            }
            double p = b.sub(a).div(a).toComplex().absdbl();
            return new EqualatorResult(p <= relativePrecition, p);
        }

        @Override
        public String toString() {
            return "ComplexEqualator{" + relativePrecition + '}';
        }

    }

    private static class DoubleEqualatorImpl implements DoubleEqualator {

        private final double relativePrecition;

        public DoubleEqualatorImpl(double relativePrecition) {
            this.relativePrecition = relativePrecition;
        }

        @Override
        public EqualatorResult equals(double a, double b) {
            if (a == b) {
                return EqualatorResult.EQUALS;
            }
            if (Double.isNaN(a) && Double.isNaN(b)) {
                return EqualatorResult.EQUALS;
            }
            if (Double.isNaN(a) || Double.isNaN(b)) {
                return EqualatorResult.DIFFERENT;
            }
            if (Double.isInfinite(a) || Double.isInfinite(b)) {
                return EqualatorResult.DIFFERENT;
            }
            if (a == 0) {
                return EqualatorResult.DIFFERENT;
            }
            double p = Math.abs(b - a / a);
            return new EqualatorResult(p <= relativePrecition, p);
        }

        @Override
        public String toString() {
            return "DoubleEqualatorImpl{" + relativePrecition + '}';
        }

    }
}
