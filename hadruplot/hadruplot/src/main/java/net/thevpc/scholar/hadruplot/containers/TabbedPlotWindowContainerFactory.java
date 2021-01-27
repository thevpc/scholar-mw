package net.thevpc.scholar.hadruplot.containers;

import net.thevpc.scholar.hadruplot.PlotContainer;
import net.thevpc.scholar.hadruplot.extension.PlotWindowContainerFactory;

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
