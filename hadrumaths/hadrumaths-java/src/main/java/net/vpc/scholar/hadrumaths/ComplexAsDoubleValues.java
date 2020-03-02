package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadruplot.PlotDoubleConverter;

public class ComplexAsDoubleValues {
    public static final PlotDoubleConverter ABS = new PlotDoubleConverter.AbstractPlotDoubleConverter("ABS", true) {
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
    public static final PlotDoubleConverter REAL = new PlotDoubleConverter.AbstractPlotDoubleConverter("REAL", true) {
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
    public static final PlotDoubleConverter IMG = new PlotDoubleConverter.AbstractPlotDoubleConverter("IMG", true) {
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
    public static final PlotDoubleConverter DB = new PlotDoubleConverter.AbstractPlotDoubleConverter("DB", true) {
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
    public static final PlotDoubleConverter DB2 = new PlotDoubleConverter.AbstractPlotDoubleConverter("DB2", true) {
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

    public static final PlotDoubleConverter ARG = new PlotDoubleConverter.AbstractPlotDoubleConverter("ARG", true) {
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
