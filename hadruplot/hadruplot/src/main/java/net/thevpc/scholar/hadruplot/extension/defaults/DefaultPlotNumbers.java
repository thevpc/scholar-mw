/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruplot.extension.defaults;

import java.math.BigDecimal;
import java.math.BigInteger;
import net.thevpc.scholar.hadruplot.PlotDoubleComplex;
import net.thevpc.scholar.hadruplot.PlotDoubleConverter;
import net.thevpc.scholar.hadruplot.extension.PlotNumbers;

/**
 *
 * @author vpc
 */
public class DefaultPlotNumbers implements PlotNumbers {
    
    public DefaultPlotNumbers() {
    }

    @Override
    public double toDouble(Object o) {
        double d = Double.NaN;
        if (o == null) {
            d = 0;
        } else if (o instanceof Number) {
            d = ((Number) o).doubleValue();
        }
        if (Double.isNaN(d)) {
            d = 0;
        }
        return d;
    }

    @Override
    public Object plus(Object a, Object b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        if (a instanceof Number && b instanceof Number) {
            return ((Number) a).doubleValue() + ((Number) b).doubleValue();
        }
        return Double.NaN;
    }

    @Override
    public int compare(Object a, Object b) {
        if (a == b) {
            return 0;
        }
        if (a == null) {
            return -1;
        }
        if (b == null) {
            return 1;
        }
        if (a instanceof Number && b instanceof Number) {
            return Double.compare(((Number) a).doubleValue(), ((Number) b).doubleValue());
        }
        return Integer.MIN_VALUE;
    }

    @Override
    public PlotDoubleComplex toDoubleComplex(Object o) {
        if (o instanceof Number) {
            return new PlotDoubleComplex(((Number) o).doubleValue(), 0);
        }
        throw new IllegalArgumentException("Unsupported");
    }

    @Override
    public Object mul(Object o, double b) {
        if (o instanceof BigDecimal) {
            return ((BigDecimal) o).multiply(new BigDecimal(b));
        } else if (o instanceof BigInteger) {
            return new BigDecimal((BigInteger) o).multiply(new BigDecimal(b));
        } else if (o instanceof Number) {
            return ((Number) o).doubleValue() * b;
        }
        throw new IllegalArgumentException("Unsupported");
    }

    @Override
    public double relativeError(Object aa, Object bb) {
        double a = ((Number) aa).doubleValue();
        double b = ((Number) bb).doubleValue();
        double c;
        if (a == b || (Double.isNaN(a) && Double.isNaN(b)) || (Double.isInfinite(a) && Double.isInfinite(b))) {
            c = 0;
            //                    } else if (b.isNaN() || b.isInfinite() || b.equals(Math2.CZERO)) {
            //                        c[i][j] = (a.substract(b));
        } else {
            c = Math.abs((a - b) * 100 / (b));
        }
        return c;
    }

    @Override
    public PlotDoubleConverter resolveDoubleConverter(Object d) {
        return PlotDoubleConverter.REAL;
    }
    
}
