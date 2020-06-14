package net.vpc.scholar.hadruplot.containers;

import net.vpc.scholar.hadruplot.PlotContainer;
import net.vpc.scholar.hadruplot.extension.PlotWindowContainerFactory;

/**
 * Created by vpc on 5/7/14.
 */
public class TabbedPlotWindowContainerFactory implements PlotWindowContainerFactory {
    public static final TabbedPlotWindowContainerFactory INSTANCE = new TabbedPlotWindowContainerFactory();

    @Override
    public PlotContainer create() {
        return new TabbedPlotContainer();
//        return new TabbedPlotContainer();// new DefaultPlotContainer();
    }
}
