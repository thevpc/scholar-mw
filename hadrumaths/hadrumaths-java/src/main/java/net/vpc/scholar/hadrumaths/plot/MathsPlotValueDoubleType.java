package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadruplot.PlotValueDoubleType;
import net.vpc.scholar.hadruplot.PlotValueType;

public class MathsPlotValueDoubleType extends PlotValueDoubleType {
    public static final PlotValueType INSTANCE=new MathsPlotValueDoubleType();
    public MathsPlotValueDoubleType() {
    }

    @Override
    public double toDouble(Object value) {
        return PlotTypesHelper.toDouble(value);
    }

    @Override
    public Object toValue(Object value,Class cls) {
        if(cls.equals(Complex.class)){
            return Complex.valueOf(toDouble(value));
        }
        return super.toValue(value,cls);
    }

}
