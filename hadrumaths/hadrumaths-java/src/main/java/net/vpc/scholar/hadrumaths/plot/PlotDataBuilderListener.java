package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.plot.console.PlotData;

public interface PlotDataBuilderListener {
    void onBuild(PlotData data, PlotDataBuilder builder);
}
