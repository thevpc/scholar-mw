package net.thevpc.scholar.hadrumaths.plot;

import net.thevpc.scholar.hadrumaths.AxisVector;
import net.thevpc.scholar.hadruplot.model.value.AbstractPlotValueType;

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
