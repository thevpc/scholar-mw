package net.vpc.scholar.hadruwaves.console.plot;

import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleAwareObject;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.PlotAxisCubes;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadruwaves.console.PlotEvaluator;

public class PlotValueVDiscrete extends PlotAxisCubes implements Cloneable {
    private PlotEvaluator<VDiscrete> value;
    public PlotValueVDiscrete(String name, PlotEvaluator<VDiscrete> value, YType... types) {
        super(name,types);
        this.value=value;
    }

    protected VDiscrete computeValue(ConsoleAwareObject o, ComputationMonitor monitor, ConsoleActionParams p) {
        return value.computeValue(o,monitor,p);
    }
}