package net.vpc.scholar.hadruplot.containers;

import net.vpc.scholar.hadruplot.PlotContainer;
import net.vpc.scholar.hadruplot.PlotWindowContainerFactory;

/**
 * Created by vpc on 5/7/14.
 */
public class PanelPlotWindowContainerFactory implements PlotWindowContainerFactory {
    public static final PanelPlotWindowContainerFactory INSTANCE = new PanelPlotWindowContainerFactory();

//    @Override
    public PlotContainer create() {
        return new PanelPlotContainer();// new DefaultPlotContainer();
//        return new TabbedPlotContainer();// new DefaultPlotContainer();
    }
}
