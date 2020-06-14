package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.plot.component.AxisVectorPlotPanel;
import net.vpc.scholar.hadrumaths.plot.component.ExpressionsPlotPanel;
import net.vpc.scholar.hadrumaths.plot.model.AxisVectorPlotModel;
import net.vpc.scholar.hadrumaths.plot.model.ExpressionsPlotModel;
import net.vpc.scholar.hadruplot.model.PlotModel;
import net.vpc.scholar.hadruplot.extension.PlotModelPanelFactory;
import net.vpc.scholar.hadruplot.PlotPanel;
import net.vpc.scholar.hadruplot.PlotWindowManager;

public class ExpressionsPlotModelPanelFactory implements PlotModelPanelFactory {
    @Override
    public boolean acceptModel(PlotModel model) {
        if(model instanceof ExpressionsPlotModel){
            return true;
        }
        if(model instanceof AxisVectorPlotModel){
            return true;
        }
        return false;
    }

    @Override
    public PlotPanel createPanel(PlotModel model, PlotWindowManager windowManager) {
        if (model instanceof ExpressionsPlotModel) {
            return new ExpressionsPlotPanel((ExpressionsPlotModel) model, windowManager);
        }
        if (model instanceof AxisVectorPlotModel) {
            return new AxisVectorPlotPanel((AxisVectorPlotModel) model, windowManager);
        }
        return null;
    }
}
