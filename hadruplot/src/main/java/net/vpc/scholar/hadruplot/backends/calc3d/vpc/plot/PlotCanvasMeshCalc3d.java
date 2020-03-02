package net.vpc.scholar.hadruplot.backends.calc3d.vpc.plot;

import net.vpc.common.util.MinMax;
import net.vpc.scholar.hadruplot.PlotModel;
import net.vpc.scholar.hadruplot.PlotModelProvider;
import net.vpc.scholar.hadruplot.ValuesPlotModel;
import net.vpc.scholar.hadruplot.ValuesPlotXYDoubleModelFace;
import net.vpc.scholar.hadruplot.backends.calc3d.core.Preferences;
import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.Box3D;
import net.vpc.scholar.hadruplot.backends.calc3d.math.Vector3D;
import net.vpc.scholar.hadruplot.backends.calc3d.vpc.element3d.Element3DSurface2;

public class PlotCanvasMeshCalc3d extends PlotCanvasAnyCalc3d {
    public PlotCanvasMeshCalc3d(PlotModelProvider plotModelProvider) {
        super(plotModelProvider);

        chartPanel.getSceneManager().setAxisVisible(true);
        chartPanel.getSceneManager().setGridXYVisible(false);
        chartPanel.getSceneManager().setBoxVisible(true);

        PlotModel m = plotModelProvider.getModel();
        ValuesPlotXYDoubleModelFace data = new ValuesPlotXYDoubleModelFace((ValuesPlotModel) m, config);
        MinMax xminMax = new MinMax();
        MinMax yminMax = new MinMax();
        MinMax zminMax = new MinMax();
        double[] x = data.getX();
        double[] y = data.getY();
        double[][] z = data.getZ();
        Vector3D[][] r = new Vector3D[z.length][z[0].length];
        for (int i = 0; i < x.length; i++) {
            xminMax.registerValue(x[i]);
        }
        for (int i = 0; i < y.length; i++) {
            yminMax.registerValue(y[i]);
        }
        for (int i = 0; i < z.length; i++) {
            for (int j = 0; j < z[i].length; j++) {
                r[i][j] = new Vector3D(x[j], y[i], z[i][j]);
                zminMax.registerValue(z[i][j]);
            }
        }
        Preferences settings = chartPanel.getSettings().getPreferences();
        Box3D clipBox = new Box3D(xminMax.getMin(), xminMax.getMax(),
                yminMax.getMin(), yminMax.getMax(),
                zminMax.getMin(), zminMax.getMax());
        settings.setClipBox(clipBox);
        chartPanel.addElement(new Element3DSurface2(r, clipBox));
        chartPanel.applySettings( settings,true);
    }
}
