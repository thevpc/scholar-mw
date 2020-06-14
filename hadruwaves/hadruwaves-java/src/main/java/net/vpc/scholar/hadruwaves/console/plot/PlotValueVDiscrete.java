package net.vpc.scholar.hadruwaves.console.plot;

import net.vpc.scholar.hadruplot.console.ConsoleActionParams;
import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruplot.console.yaxis.PlotAxisCubes;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruplot.console.PlotEvaluator;
import net.vpc.scholar.hadruplot.PlotHyperCube;
import net.vpc.scholar.hadruplot.console.PlotConfigManager;

public class PlotValueVDiscrete extends PlotAxisCubes implements Cloneable {
    private PlotEvaluator<VDiscrete> value;
    public PlotValueVDiscrete(String name, PlotEvaluator<VDiscrete> value, YType... types) {
        super(name,types);
        this.value=value;
    }

    protected PlotHyperCube evalValue(ConsoleAwareObject o, ProgressMonitor monitor, ConsoleActionParams p) {
        VDiscrete d = value.evalValue(o,monitor,p);
        return PlotConfigManager.getPlotHyperCubeResolvers().resolve(d);
    }
}