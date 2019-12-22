package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadruplot.PlotDoubleConverter;

public class ComplexAsDoubleValues {
    public static final PlotDoubleConverter ABS = new PlotDoubleConverter.AbstractPlotDoubleConverter("ABS",true) {
        @Override
        public double toDouble(Object c) {
            if(c==null){
                return Double.NaN;
            }
            return ((Complex)c).absdbl();
        }
        @Override
        public Object toComplex(double d) {
            return Complex.valueOf(d);
        }
    };
    public static final PlotDoubleConverter REAL = new PlotDoubleConverter.AbstractPlotDoubleConverter("REAL",true) {
        @Override
        public double toDouble(Object c) {
            if(c==null){
                return Double.NaN;
            }
            return ((Complex)c).realdbl();
        }
        @Override
        public Object toComplex(double d) {
            return Complex.valueOf(d);
        }
    };
    public static final PlotDoubleConverter IMG = new PlotDoubleConverter.AbstractPlotDoubleConverter("IMG",true) {
        @Override
        public double toDouble(Object c) {
            if(c==null){
                return Double.NaN;
            }
            return ((Complex)c).imagdbl();
        }
        @Override
        public Object toComplex(double d) {
            return Complex.valueOf(d);
        }
    };
    public static final PlotDoubleConverter DB = new PlotDoubleConverter.AbstractPlotDoubleConverter("DB",true) {
        @Override
        public double toDouble(Object c) {
            if(c==null){
                return Double.NaN;
            }
            return ((Complex)c).dbdbl();
        }
        @Override
        public Object toComplex(double d) {
            return Complex.valueOf(d);
        }
    };
    public static final PlotDoubleConverter DB2 = new PlotDoubleConverter.AbstractPlotDoubleConverter("DB2",true) {
        @Override
        public double toDouble(Object c) {
            if(c==null){
                return Double.NaN;
            }
            return ((Complex)c).db2dbl();
        }
        @Override
        public Object toComplex(double d) {
            return Complex.valueOf(d);
        }
    };

    public static final PlotDoubleConverter ARG = new PlotDoubleConverter.AbstractPlotDoubleConverter("ARG",true) {
        @Override
        public double toDouble(Object c) {
            if(c==null){
                return Double.NaN;
            }
            return ((Complex)c).argdbl();
        }
        @Override
        public Object toComplex(double d) {
            return Complex.valueOf(d);
        }
    };

    public static final PlotDoubleConverter ARG_DEG = new PlotDoubleConverter.AbstractPlotDoubleConverter("ARG_DEG",true) {
        @Override
        public double toDouble(Object c) {
            if(c==null){
                return Double.NaN;
            }
            return ((Complex)c).argdbl()/Math.PI*180;
        }
        @Override
        public Object toComplex(double d) {
            return Complex.valueOf(d);
        }
    };

    public static void init(){

    }
}
