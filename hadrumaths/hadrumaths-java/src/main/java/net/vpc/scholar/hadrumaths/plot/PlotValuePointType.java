package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.geom.Point;
import net.vpc.scholar.hadruplot.AbstractPlotValueType;

public class PlotValuePointType extends AbstractPlotValueType {
    public PlotValuePointType() {
        super("point");
    }

    public Point toPoint(Object o){
        return (Point) o;
    }

    @Override
    public Object getValue(Object o) {
        return o;
    }
}
