package net.vpc.scholar.hadruplot.console.yaxis;

import net.vpc.scholar.hadruplot.PlotType;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class PlotAxisCustom extends PlotAxis {
    public PlotAxisCustom(String name, YType... type) {
        super(name, type);
    }

    public PlotAxisCustom(String name, YType[] type, PlotType plotType) {
        super(name, type, plotType);
    }
}
