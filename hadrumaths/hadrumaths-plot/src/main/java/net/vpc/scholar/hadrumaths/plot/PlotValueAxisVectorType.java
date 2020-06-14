package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.AxisVector;
import net.vpc.scholar.hadruplot.model.value.AbstractPlotValueType;

public class PlotValueAxisVectorType extends AbstractPlotValueType {
    public PlotValueAxisVectorType() {
        super("AxisVector");
    }

    @Override
    public Object getValue(Object o) {
        return cast(o);
    }

    public AxisVector cast(Object o) {
        return (AxisVector) o;
    }
}
