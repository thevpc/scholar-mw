package net.thevpc.scholar.hadrumaths.plot;

import net.thevpc.scholar.hadrumaths.geom.Point;
import net.thevpc.scholar.hadruplot.model.value.AbstractPlotValueType;

public class PlotValuePointType extends AbstractPlotValueType {
    public PlotValuePointType() {
        super("point");
    }

    public Point toPoint(Object o) {
        return (Point) o;
    }

    @Override
    public Object getValue(Object o) {
        return o;
    }
}
