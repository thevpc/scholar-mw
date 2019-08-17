package net.vpc.scholar.hadruwaves.console.plot;

import net.vpc.scholar.hadruplot.console.ConsoleActionParams;
import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruplot.console.yaxis.PlotAxisCubes;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruplot.PlotEvaluator;

public class PlotValueVDiscrete extends PlotAxisCubes implements Cloneable {
    private PlotEvaluator<VDiscrete> value;
    public PlotValueVDiscrete(String name, PlotEvaluator<VDiscrete> value, YType... types) {
        super(name,types);
        this.value=value;
    }

    protected VDiscrete computeValue(ConsoleAwareObject o, ProgressMonitor monitor, ConsoleActionParams p) {
        return value.computeValue(o,monitor,p);
    }
}