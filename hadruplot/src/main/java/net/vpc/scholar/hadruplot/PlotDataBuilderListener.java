package net.vpc.scholar.hadruplot;

import net.vpc.scholar.hadruplot.console.PlotData;

public interface PlotDataBuilderListener {
    void onBuild(PlotData data, PlotDataBuilder builder);
}
