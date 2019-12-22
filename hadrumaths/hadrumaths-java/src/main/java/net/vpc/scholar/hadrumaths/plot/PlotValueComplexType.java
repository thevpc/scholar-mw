package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadruplot.AbstractPlotValueType;

public class PlotValueComplexType extends AbstractPlotValueType {
    public PlotValueComplexType() {
        super("complex");
    }

    public Complex toComplex(Object o) {
        return PlotTypesHelper.toComplex(o);
    }

    @Override
    public Object getValue(Object o) {
        return toComplex(o);
    }
}
