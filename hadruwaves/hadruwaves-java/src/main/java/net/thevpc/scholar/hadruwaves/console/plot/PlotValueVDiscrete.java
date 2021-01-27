package net.thevpc.scholar.hadruwaves.console.plot;

import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.console.yaxis.PlotAxisCubes;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruplot.console.PlotEvaluator;
import net.thevpc.scholar.hadruplot.PlotHyperCube;
import net.thevpc.scholar.hadruplot.console.PlotConfigManager;

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