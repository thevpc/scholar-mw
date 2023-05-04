package net.thevpc.scholar.hadrumaths.plot;

import java.util.function.ToDoubleFunction;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadruplot.PlotDoubleConverter;

public class ComplexAsDoubleValues {
    public static final ToDoubleFunction<Object> ABS = new PlotDoubleConverter.AbstractPlotDoubleConverter("ABS", true) {
        @Override
        public Object toComplex(double d) {
            return Complex.of(d).abs();
        }

        @Override
        public double toDouble(Object c) {
            if (c == null) {
                return Double.NaN;
            }
            if (c instanceof Complex) {
                return ((Complex) c).absdbl();
            }
            if (c instanceof Number) {
                return Math.abs(((Number) c).doubleValue());
            }
            return Double.NaN;
        }
    };
    public static final ToDoubleFunction<Object> REAL = new PlotDoubleConverter.AbstractPlotDoubleConverter("REAL", true) {
        @Override
        public Object toComplex(double d) {
            return Complex.of(d).real();
        }

        @Override
        public double toDouble(Object c) {
            if (c == null) {
                return Double.NaN;
            }
            if (c instanceof Complex) {
                return ((Complex) c).realdbl();
            }
            if (c instanceof Number) {
                return (((Number) c).doubleValue());
            }
            return Double.NaN;
        }
    };
    public static final ToDoubleFunction<Object> IMG = new PlotDoubleConverter.AbstractPlotDoubleConverter("IMG", true) {
        @Override
        public Object toComplex(double d) {
            return Complex.ZERO;
        }

        @Override
        public double toDouble(Object c) {
            if (c == null) {
                return Double.NaN;
            }
            if (c instanceof Complex) {
                return ((Complex) c).imagdbl();
            }
            if (c instanceof Number) {
                return 0;
            }
            return Double.NaN;
        }
    };
    public static final ToDoubleFunction<Object> DB = new PlotDoubleConverter.AbstractPlotDoubleConverter("DB", true) {
        @Override
        public Object toComplex(double d) {
            return Complex.of(d).db();
        }

        @Override
        public double toDouble(Object c) {
            if (c == null) {
                return Double.NaN;
            }
            if (c instanceof Complex) {
                return ((Complex) c).dbdbl();
            }
            if (c instanceof Number) {
                return Complex.of(((Number) c).doubleValue()).dbdbl();
            }
            return Double.NaN;
        }
    };
    public static final ToDoubleFunction<Object> DB2 = new PlotDoubleConverter.AbstractPlotDoubleConverter("DB2", true) {
        @Override
        public Object toComplex(double d) {
            return Complex.of(d).db2();
        }

        @Override
        public double toDouble(Object c) {
            if (c == null) {
                return Double.NaN;
            }
            if (c instanceof Complex) {
                return ((Complex) c).db2dbl();
            }
            if (c instanceof Number) {
                return Complex.of(((Number) c).doubleValue()).db2dbl();
            }
            return Double.NaN;
        }
    };
    public static final ToDoubleFunction<Object> LOG = new PlotDoubleConverter.AbstractPlotDoubleConverter("LOG", true) {
        @Override
        public Object toComplex(double d) {
            return Complex.of(d).log();
        }

        @Override
        public double toDouble(Object c) {
            if (c == null) {
                return Double.NaN;
            }
            if (c instanceof Complex) {
                return ((Complex) c).db2dbl();
            }
            if (c instanceof Number) {
                return Math.log(((Number) c).doubleValue());
            }
            return Double.NaN;
        }
    };
    public static final ToDoubleFunction<Object> LOG10 = new PlotDoubleConverter.AbstractPlotDoubleConverter("LOG10", true) {
        @Override
        public Object toComplex(double d) {
            return Complex.of(d).log10();
        }

        @Override
        public double toDouble(Object c) {
            if (c == null) {
                return Double.NaN;
            }
            if (c instanceof Complex) {
                return ((Complex) c).log10().dbl();
            }
            if (c instanceof Number) {
                return Math.log(((Number) c).doubleValue());
            }
            return Double.NaN;
        }
    };

    public static final ToDoubleFunction<Object> ARG = new PlotDoubleConverter.AbstractPlotDoubleConverter("ARG", true) {
        @Override
        public Object toComplex(double d) {
            return Complex.of(d).arg();
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
            return Complex.of(d / Math.PI * 180).arg();
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
