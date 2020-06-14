package net.vpc.scholar.hadruplot.model.value;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruplot.console.PlotEvaluator;
import net.vpc.scholar.hadruplot.PlotMatrix;
import net.vpc.scholar.hadruplot.PlotType;
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
    protected PlotMatrix evalValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p) {
        return value.evalValue(structure,monitor,p);
    }

}
