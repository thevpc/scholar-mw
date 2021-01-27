package net.thevpc.scholar.hadruplot.extension;

import net.thevpc.scholar.hadruplot.PlotPanel;
import net.thevpc.scholar.hadruplot.PlotWindowManager;
import net.thevpc.scholar.hadruplot.model.PlotModel;

public interface PlotModelPanelFactory {
    boolean acceptModel(PlotModel model);
    PlotPanel createPanel(PlotModel model, PlotWindowManager windowManager);
}
