package net.vpc.scholar.hadruplot.libraries.jfreechart;

import net.vpc.scholar.hadruplot.PlotLibrary;
import net.vpc.scholar.hadruplot.PlotComponentContext;
import net.vpc.scholar.hadruplot.PlotComponentPanel;
import net.vpc.scholar.hadruplot.PlotType;

public class JFreechartLibrary implements PlotLibrary {
    private String name="JfreeChart";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSupportLevel(PlotType type) {
        switch (type) {
            case BAR:
            case CURVE:
            case AREA:
            case PIE:
            case RING:
            case BUBBLE:
            case POLAR:
            case FIELD:
                return 50;
        }
        return -1;
    }

    @Override
    public PlotComponentPanel createPlotComponentPanel(PlotComponentContext context) {
        switch (context.getPlotType().getType()) {
            case CURVE: {
                return new PlotCanvasCurveJFreeChart(context.getModelProvider());
            }
            case BAR: {
                return new PlotCanvasBarJFreeChart(context.getModelProvider());
            }
            case AREA: {
                return new PlotCanvasAreaJFreeChart(context.getModelProvider());
            }
            case PIE: {
                return new PlotCanvasPieJFreeChart(context.getModelProvider());
            }
            case RING: {
                return new PlotCanvasRingJFreeChart(context.getModelProvider());
            }
            case BUBBLE: {
                return new PlotCanvasBubbleJFreeChart(context.getModelProvider());
            }
            case POLAR: {
                return new PlotCanvasPolarJFreeChart(context.getModelProvider());
            }
            case FIELD: {
                return new PlotCanvasVectorJFreeChart(context.getModelProvider());
            }
        }
        throw new IllegalArgumentException("Unsupported plot type " + context.getPlotType());
    }
}
