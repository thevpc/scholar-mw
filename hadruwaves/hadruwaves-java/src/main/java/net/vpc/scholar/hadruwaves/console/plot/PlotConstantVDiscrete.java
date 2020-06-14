package net.vpc.scholar.hadruwaves.console.plot;

import net.vpc.scholar.hadruplot.console.ConsoleActionParams;
import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruplot.console.yaxis.PlotAxisCubes;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruplot.PlotHyperCube;
import net.vpc.scholar.hadruplot.console.PlotConfigManager;

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