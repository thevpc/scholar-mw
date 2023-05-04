package net.thevpc.scholar.hadruplot.libraries.calc3d;

import net.thevpc.scholar.hadruplot.PlotComponentContext;
import net.thevpc.scholar.hadruplot.PlotComponentPanel;
import net.thevpc.scholar.hadruplot.PlotLibrary;
import net.thevpc.scholar.hadruplot.PlotType;
import net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.plot.PlotCanvasCurveCalc3d;
import net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.plot.PlotCanvasMeshCalc3d;

public class Calc3dLibrary implements PlotLibrary {

    @Override
    public String getName() {
        return "3D";
    }

    @Override
    public int getSupportLevel(PlotType type) {
        switch (type) {
            case CURVE:
            case MESH:
                return 40;
        }
        return -1;
    }

    @Override
    public PlotComponentPanel createPlotComponentPanel(PlotComponentContext context) {
        switch (context.getPlotType().getType()) {
            case CURVE:
                {
                return new PlotCanvasCurveCalc3d(context.getModelProvider());
            }
            case MESH:
                {
                return new PlotCanvasMeshCalc3d(context.getModelProvider());
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

}
