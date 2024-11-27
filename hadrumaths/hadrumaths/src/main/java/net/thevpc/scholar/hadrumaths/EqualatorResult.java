/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.thevpc.scholar.hadrumaths;

/**
 *
 * @author vpc
 */
public class EqualatorResult {

    public static final EqualatorResult EQUALS = new EqualatorResult(true, 0);
    public static final EqualatorResult DIFFERENT = new EqualatorResult(false, Double.POSITIVE_INFINITY);
    private boolean equal;
    private double precision;

    public EqualatorResult(boolean equals, double precision) {
        this.equal = equals;
        this.precision = precision;
    }

    public EqualatorResult max(EqualatorResult other) {
        if (equal && other.equal) {
            if (precision < other.precision) {
                return other;
            }
            return this;
        }
        if (!equal && !other.equal) {
            if (precision < other.precision) {
                return other;
            }
            return this;
        }
        if (equal) {
            return other;
        }
        return this;
    }

    public boolean isEquals() {
        return equal;
    }

    public double getPrecision() {
        return precision;
    }

    @Override
    public String toString() {
        return "EqualatorResult{" + "equal=" + equal + ", precision=" + precision + '}';
    }
    

}
