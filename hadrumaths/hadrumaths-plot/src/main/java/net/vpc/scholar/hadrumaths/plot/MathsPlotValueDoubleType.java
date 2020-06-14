package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadruplot.model.value.PlotValueDoubleType;
import net.vpc.scholar.hadruplot.model.value.PlotValueType;

public class MathsPlotValueDoubleType extends PlotValueDoubleType {
    public static final PlotValueType INSTANCE = new MathsPlotValueDoubleType();

    public MathsPlotValueDoubleType() {
    }

    @Override
    public Object toValue(Object value, Class cls) {
        if (cls.equals(Complex.class)) {
            return Complex.of(toDouble(value));
        }
        return super.toValue(value, cls);
    }

    @Override
    public double toDouble(Object value) {
        return PlotTypesHelper.toDouble(value);
    }

}
