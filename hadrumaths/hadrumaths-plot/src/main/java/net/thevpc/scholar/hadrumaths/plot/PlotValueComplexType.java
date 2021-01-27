package net.thevpc.scholar.hadrumaths.plot;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadruplot.model.value.AbstractPlotValueType;

public class PlotValueComplexType extends AbstractPlotValueType {
    public PlotValueComplexType() {
        super("complex");
    }

    @Override
    public Object getValue(Object o) {
        return toComplex(o);
    }

    public Complex toComplex(Object o) {
        return PlotTypesHelper.toComplex(o);
    }
}
