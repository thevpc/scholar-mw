package net.thevpc.scholar.hadrumaths.plot;

import net.thevpc.scholar.hadrumaths.geom.HPoint;
import net.thevpc.scholar.hadruplot.model.value.AbstractPlotValueType;

public class PlotValuePointType extends AbstractPlotValueType {
    public PlotValuePointType() {
        super("point");
    }

    public HPoint toPoint(Object o) {
        return (HPoint) o;
    }

    @Override
    public Object getValue(Object o) {
        return o;
    }
}
