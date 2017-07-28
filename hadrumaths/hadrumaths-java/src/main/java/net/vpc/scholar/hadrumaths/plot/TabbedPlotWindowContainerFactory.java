package net.vpc.scholar.hadrumaths.plot;

/**
 * Created by vpc on 5/7/14.
 */
public class TabbedPlotWindowContainerFactory implements PlotWindowContainerFactory{
    @Override
    public PlotContainer create(String name, PlotWindowManager plotWindowManager) {
        return new TabbedPlotContainer(plotWindowManager,name);
    }
}
