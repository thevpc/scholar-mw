package net.vpc.scholar.hadruplot.libraries.calc3d.vpc;

import net.vpc.scholar.hadruplot.PlotComponentContext;
import net.vpc.scholar.hadruplot.PlotComponentPanel;
import net.vpc.scholar.hadruplot.PlotLibrary;
import net.vpc.scholar.hadruplot.PlotType;
import net.vpc.scholar.hadruplot.libraries.calc3d.vpc.plot.PlotCanvasCurveCalc3d;
import net.vpc.scholar.hadruplot.libraries.calc3d.vpc.plot.PlotCanvasMeshCalc3d;

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
