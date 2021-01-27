package net.thevpc.scholar.hadruplot.libraries.jzy3d;

import net.thevpc.scholar.hadruplot.PlotLibrary;
import net.thevpc.scholar.hadruplot.PlotComponentContext;
import net.thevpc.scholar.hadruplot.PlotComponentPanel;
import net.thevpc.scholar.hadruplot.PlotType;

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
        switch (context.getPlotType().getType()) {
            case MESH: {
                return new Jzy3dMeshPlot(context.getModelProvider());
            }
        }
        throw new IllegalArgumentException("Unsupported plot type " + context.getPlotType());
    }
}
