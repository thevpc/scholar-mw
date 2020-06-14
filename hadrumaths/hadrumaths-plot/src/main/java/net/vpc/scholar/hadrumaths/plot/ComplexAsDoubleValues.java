package net.vpc.scholar.hadrumaths.plot;

import java.util.function.ToDoubleFunction;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadruplot.PlotDoubleConverter;

public class ComplexAsDoubleValues {
    public static final ToDoubleFunction<Object> ABS = new PlotDoubleConverter.AbstractPlotDoubleConverter("ABS", true) {
        @Override
        public Object toComplex(double d) {
            return Complex.of(d);
        }

        @Override
        public double toDouble(Object c) {
            if (c == null) {
                return Double.NaN;
            }
            return ((Complex) c).absdbl();
        }
    };
    public static final ToDoubleFunction<Object> REAL = new PlotDoubleConverter.AbstractPlotDoubleConverter("REAL", true) {
        @Override
        public Object toComplex(double d) {
            return Complex.of(d);
        }

        @Override
        public double toDouble(Object c) {
            if (c == null) {
                return Double.NaN;
            }
            return ((Complex) c).realdbl();
        }
    };
    public static final ToDoubleFunction<Object> IMG = new PlotDoubleConverter.AbstractPlotDoubleConverter("IMG", true) {
        @Override
        public Object toComplex(double d) {
            return Complex.of(d);
        }

        @Override
        public double toDouble(Object c) {
            if (c == null) {
                return Double.NaN;
            }
            return ((Complex) c).imagdbl();
        }
    };
    public static final ToDoubleFunction<Object> DB = new PlotDoubleConverter.AbstractPlotDoubleConverter("DB", true) {
        @Override
        public Object toComplex(double d) {
            return Complex.of(d);
        }

        @Override
        public double toDouble(Object c) {
            if (c == null) {
                return Double.NaN;
            }
            return ((Complex) c).dbdbl();
        }
    };
    public static final ToDoubleFunction<Object> DB2 = new PlotDoubleConverter.AbstractPlotDoubleConverter("DB2", true) {
        @Override
        public Object toComplex(double d) {
            return Complex.of(d);
        }

        @Override
        public double toDouble(Object c) {
            if (c == null) {
                return Double.NaN;
            }
            return ((Complex) c).db2dbl();
        }
    };

    public static final ToDoubleFunction<Object> ARG = new PlotDoubleConverter.AbstractPlotDoubleConverter("ARG", true) {
        @Override
        public Object toComplex(double d) {
            return Complex.of(d);
        }

        @Override
        public double toDouble(Object c) {
            if (c == null) {
                return Double.NaN;
            }
            return ((Complex) c).argdbl();
        }
    };

    public static final PlotDoubleConverter ARG_DEG = new PlotDoubleConverter.AbstractPlotDoubleConverter("ARG_DEG", true) {
        @Override
        public Object toComplex(double d) {
            return Complex.of(d);
        }

        @Override
        public double toDouble(Object c) {
            if (c == null) {
                return Double.NaN;
            }
            return ((Complex) c).argdbl() / Math.PI * 180;
        }
    };

    public static void init() {

    }
}
