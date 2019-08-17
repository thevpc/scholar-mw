package net.vpc.scholar.hadruplot.backends.jzy3d;

import net.vpc.scholar.hadruplot.PlotLibrary;
import net.vpc.scholar.hadruplot.PlotComponentContext;
import net.vpc.scholar.hadruplot.PlotComponentPanel;
import net.vpc.scholar.hadruplot.PlotType;

public class Jzy3DLibrary implements PlotLibrary {
    private String name="Jzy3d";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSupportLevel(PlotType type) {
        switch (type) {
            case MESH:
                return 100;
        }
        return -1;
    }

    @Override
    public PlotComponentPanel createPlotComponentPanel(PlotComponentContext context) {
        switch (context.getPlotType()) {
            case MESH: {
                return new Jzy3dMeshPlot(context.getModelProvider());
            }
        }
        throw new IllegalArgumentException("Unsupported plot type " + context.getPlotType());
    }
}
