package net.vpc.scholar.hadruplot.backends.simple;

import net.vpc.scholar.hadruplot.*;
import net.vpc.scholar.hadruplot.backends.simple.heatmap.HeatMapPlot;
import net.vpc.scholar.hadruplot.backends.simple.heatmap.PlotNormalizer;
import net.vpc.scholar.hadruplot.backends.simple.table.TablePlotComponentPanel;

public class DefaultLibrary implements PlotLibrary {
    private String name="Default";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSupportLevel(PlotType type) {
        switch (type){
            case CURVE:
            case HEATMAP:
            case MATRIX:
            case TABLE:
                return 10;
        }
        return -1;
    }

    @Override
    public PlotComponentPanel createPlotComponentPanel(PlotComponentContext context) {
        switch (context.getPlotType().getType()) {
            case CURVE: {
                return new PlotCanvasCurveSimple(context.getModelProvider());
            }
            case HEATMAP:
            case MATRIX: {
                int dim=400;
                if(context.getPreferredWidth()!=null){
                    dim=context.getPreferredWidth();
                }else if(context.getPreferredHeight()!=null){
                    dim=context.getPreferredHeight();
                }
                PlotModelProvider modelProvider = context.getModelProvider();
                HeatMapPlot heatMapPlot = new HeatMapPlot(
                        modelProvider,
                        Plot.getColorPalette(modelProvider.getModel().getColorPalette()),
                        dim
                );
                PlotNormalizer n = context.getNormalizer();
                if(n!=null){
                    heatMapPlot.setNormalizer(n);
                }
                return heatMapPlot;
            }
            case TABLE: {
                return new TablePlotComponentPanel(context.getModelProvider());
            }
        }
        throw new IllegalArgumentException("Unsupported plot type " + context.getPlotType());
    }
}
