package net.vpc.scholar.hadruplot;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruplot.console.ConsoleActionParams;
import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruplot.console.yaxis.PlotAxisSeries;
import net.vpc.scholar.hadruplot.console.yaxis.YType;

public class PlotValueMatrix extends PlotAxisSeries implements Cloneable {
    private PlotEvaluator<PlotMatrix> value;
    public PlotValueMatrix(String name, PlotEvaluator<PlotMatrix> value, PlotType plotType, YType... type) {
        super(name, type, plotType);
        this.value=value;
    }

   @Override
    protected PlotMatrix computeValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p) {
        return value.computeValue(structure,monitor,p);
    }

}
