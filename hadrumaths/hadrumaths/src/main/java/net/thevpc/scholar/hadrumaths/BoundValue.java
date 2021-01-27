/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths;

/**
 *
 * @author vpc
 */
public class BoundValue {

    private double value = Double.NaN;
    private boolean nan;
    private boolean negInfinity;
    private boolean posInfinity;

    public BoundValue() {
    }

    public void min(double d) {
        if (Double.isNaN(d)) {
            nan = true;
        } else if (d == Double.NEGATIVE_INFINITY) {
            negInfinity = true;
        } else if (d == Double.POSITIVE_INFINITY) {
            posInfinity = true;
        } else {
            if (Double.isNaN(value) || d < value) {
                value = d;
            }
        }
    }

    public void max(double d) {
        if (Double.isNaN(d)) {
            nan = true;
        } else if (d == Double.NEGATIVE_INFINITY) {
            negInfinity = true;
        } else if (d == Double.POSITIVE_INFINITY) {
            posInfinity = true;
        } else {
            if (Double.isNaN(value) || d > value) {
                value = d;
            }
        }
    }

    public boolean isNan() {
        return nan;
    }

    public boolean isNegativeInfinity() {
        return negInfinity;
    }

    public boolean isPositiveInfinity() {
        return posInfinity;
    }

    public boolean isInfinity() {
        return posInfinity || negInfinity;
    }

    public boolean isRegular() {
        return !Double.isNaN(value) && !posInfinity && !negInfinity && !nan;
    }

    public boolean isEmpty() {
        return Double.isNaN(value);
    }

    public double getValue() {
        return value;
    }

    public double getRealValue() {
        if (isNan()) {
            return Double.NaN;
        }
        if (isNegativeInfinity()) {
            return Double.NEGATIVE_INFINITY;
        }
        if (isPositiveInfinity()) {
            return Double.POSITIVE_INFINITY;
        }
        return value;
    }

    public double getRealMinValue() {
        if (isNegativeInfinity()) {
            return Double.NEGATIVE_INFINITY;
        }
        if(!Double.isNaN(value)){
            return value;
        }
        if (isPositiveInfinity()) {
            return Double.POSITIVE_INFINITY;
        }
        return value;
    }

    public double getRealMaxValue() {
        if (isPositiveInfinity()) {
            return Double.POSITIVE_INFINITY;
        }
        if(!Double.isNaN(value)){
            return value;
        }
        if (isNegativeInfinity()) {
            return Double.NEGATIVE_INFINITY;
        }
        return value;
    }

}
