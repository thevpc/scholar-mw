package net.thevpc.scholar.hadruwaves.console.plot;

import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.console.yaxis.PlotAxisCubes;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruplot.PlotHyperCube;
import net.thevpc.scholar.hadruplot.console.PlotConfigManager;

public class PlotConstantVDiscrete extends PlotAxisCubes implements Cloneable {
    private VDiscrete value;
    public PlotConstantVDiscrete(String name, VDiscrete value) {
        super(name,YType.REFERENCE);
        this.value=value;
    }

    protected PlotHyperCube evalValue(ConsoleAwareObject o, ProgressMonitor monitor, ConsoleActionParams p) {
        return PlotConfigManager.getPlotHyperCubeResolvers().resolve(value);
    }
}