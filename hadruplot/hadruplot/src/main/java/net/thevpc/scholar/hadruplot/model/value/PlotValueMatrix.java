package net.thevpc.scholar.hadruplot.model.value;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruplot.console.PlotEvaluator;
import net.thevpc.scholar.hadruplot.PlotMatrix;
import net.thevpc.scholar.hadruplot.PlotType;
import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.console.yaxis.PlotAxisSeries;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;

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
