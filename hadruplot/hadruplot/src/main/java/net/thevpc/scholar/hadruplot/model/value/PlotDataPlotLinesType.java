package net.thevpc.scholar.hadruplot.model.value;

import net.thevpc.scholar.hadruplot.PlotLines;

public class PlotDataPlotLinesType extends AbstractPlotValueType {
    public static final PlotValueType INSTANCE = new PlotDataPlotLinesType();

    public PlotDataPlotLinesType() {
        super("PlotLines");
    }

    public PlotLines toPlotLines(Object o){
        return (PlotLines) o;
    }

    @Override
    public Object getValue(Object o) {
        return o;
    }
}
