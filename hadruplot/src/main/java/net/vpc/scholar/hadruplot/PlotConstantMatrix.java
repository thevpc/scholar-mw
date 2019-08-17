package net.vpc.scholar.hadruplot;

import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadruplot.console.ConsoleActionParams;
import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruplot.console.yaxis.YType;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruplot.console.yaxis.PlotAxisSeries;

public class PlotConstantMatrix extends PlotAxisSeries implements Cloneable {
    private PlotMatrix value;
    public PlotConstantMatrix(String name, PlotMatrix value, PlotType plotType) {
        super(name, new YType[]{YType.REFERENCE}, plotType);
        this.value=value;
    }


    @Override
    protected PlotMatrix computeValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p) {
        ProgressMonitor m= ProgressMonitorFactory.nonnull(monitor);
        String name = getName();
        m.start(name+", starting...");
        m.terminate(name+", terminated...");
        return value;
    }

}
