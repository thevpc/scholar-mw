package net.vpc.scholar.hadruplot.extension;

import net.vpc.scholar.hadruplot.PlotPanel;
import net.vpc.scholar.hadruplot.PlotWindowManager;
import net.vpc.scholar.hadruplot.model.PlotModel;

public interface PlotModelPanelFactory {
    boolean acceptModel(PlotModel model);
    PlotPanel createPanel(PlotModel model, PlotWindowManager windowManager);
}
