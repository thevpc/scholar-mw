package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.DMatrix;
import net.vpc.scholar.hadrumaths.MathsBase;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadruplot.PlotDoubleComplex;
import net.vpc.scholar.hadruplot.PlotDoubleConverter;
import net.vpc.scholar.hadruplot.PlotNumbers;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MathsPlotNumbers implements PlotNumbers {
    @Override
    public double toDouble(Object o) {
        double d = 0;
        if (o == null) {
            d = 0;
        } else if (o instanceof Complex) {
            d = ((Complex) o).absdbl();
        } else if (o instanceof ComplexMatrix) {
            d = ((ComplexMatrix) o).norm1();
        } else if (o instanceof DMatrix) {
            d = ((DMatrix) o).norm1();
        } else {
            d = ((Number) o).doubleValue();
        }
        if (Double.isNaN(d)) {
            d = 0;
        }
        return d;
    }

    @Override
    public Object mul(Object o, double b) {
        if (o instanceof BigDecimal) {
            return ((BigDecimal) o).multiply(new BigDecimal(b));
        } else if (o instanceof BigInteger) {
            return new BigDecimal((BigInteger) o).multiply(new BigDecimal(b));
        } else if (o instanceof Complex) {
            return ((Complex) o).mul(b);
        } else if (o instanceof ComplexMatrix) {
            return ((ComplexMatrix) o).mul(b);
        } else if (o instanceof DMatrix) {
            return ((DMatrix) o).mul(b);
        } else if (o instanceof Number) {
            return ((Number) o).doubleValue() * b;
        }
        throw new IllegalArgumentException("Unsupported");
    }

    @Override
    public PlotDoubleComplex toDoubleComplex(Object o) {
        if (o instanceof PlotDoubleComplex) {
            return (PlotDoubleComplex) o;
        } else if (o instanceof Complex) {
            return new PlotDoubleComplex(
                    ((Complex) o).getReal(),
                    ((Complex) o).getImag()
            );
        } else if (o instanceof ComplexMatrix) {
            ComplexMatrix m = (ComplexMatrix) o;
            if (m.getColumnCount() == 1 && m.getRowCount() == 1) {
                return toDoubleComplex(m.get(0, 0));
            }
        } else if (o instanceof DMatrix) {
            DMatrix m = (DMatrix) o;
            if (m.getColumnCount() == 1 && m.getRowCount() == 1) {
                return toDoubleComplex(m.get(0, 0));
            }
        } else if (o instanceof Number) {
            return new PlotDoubleComplex(
                    ((Number) o).doubleValue(),
                    0
            );
        }
        throw new IllegalArgumentException("Unsupported");
    }

    @Override
    public Object plus(Object aa, Object bb) {
        Complex a = (Complex) aa;
        Complex b = (Complex) bb;
        return ((Complex) aa).add(b);
    }

    @Override
    public int compare(Object aa, Object bb) {
        Complex a = (Complex) aa;
        Complex b = (Complex) bb;
        return ((Complex) aa).compareTo(bb);
    }


    @Override
    public double relativeError(Object aa, Object bb) {
        Complex a = (Complex) aa;
        Complex b = (Complex) bb;
        if (a.equals(b) || (a.isNaN() && b.isNaN()) || (a.isInfinite() && b.isInfinite())) {
            return 0;
        } else if (a.isNaN() || b.isInfinite() || b.equals(MathsBase.CZERO)) {
            return (a.sub(b)).absdbl();
        } else {
            return (((a.sub(b)).absdbl() * 100 / (a.absdbl())));
        }
    }

    @Override
    public PlotDoubleConverter resolveDoubleConverter(Object d) {
        if (d instanceof Complex) {
            Complex c = (Complex) d;
            if (!c.isReal()) {
                return PlotDoubleConverter.ABS;
            }
        }
        return null;
    }
}
