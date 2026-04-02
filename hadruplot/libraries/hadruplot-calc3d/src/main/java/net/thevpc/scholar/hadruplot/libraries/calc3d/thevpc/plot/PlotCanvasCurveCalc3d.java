package net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.plot;

import net.thevpc.nuts.math.NDoubleRange;
import net.thevpc.scholar.hadruplot.model.PlotModel;
import net.thevpc.scholar.hadruplot.extension.PlotModelProvider;
import net.thevpc.scholar.hadruplot.model.ValuesPlotModel;
import net.thevpc.scholar.hadruplot.model.ValuesPlotXDoubleModelFace;
import net.thevpc.scholar.hadruplot.libraries.calc3d.core.Preferences;
import net.thevpc.scholar.hadruplot.libraries.calc3d.geometry3d.Box3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.Point3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.element3d.Calc3dFactory;

public class PlotCanvasCurveCalc3d extends PlotCanvasAnyCalc3d {
    public PlotCanvasCurveCalc3d(PlotModelProvider plotModelProvider) {
        super(plotModelProvider);

        chartPanel.getSceneManager().setAxisVisible(true);
        chartPanel.getSceneManager().setGridXYVisible(true);
        chartPanel.getSceneManager().setBoxVisible(true);

        PlotModel m = plotModelProvider.getModel();
        ValuesPlotXDoubleModelFace data = new ValuesPlotXDoubleModelFace((ValuesPlotModel) m, config);
        NDoubleRange xminMax = NDoubleRange.of();
        NDoubleRange yminMax = NDoubleRange.of();
        NDoubleRange zminMax = NDoubleRange.of();
        for (int i = 0; i < data.size(); i++) {
//            String key = data.getYTitle(i);
            double[] x = data.getX(i);
            double[] y = data.getY(i);
            Point3D[] r = new Point3D[y.length];
            for (int k = 0; k < y.length; k++) {
                xminMax.add(x[k]);
                yminMax.add(y[k]);
                zminMax.add(x[k]);
                zminMax.add(y[k]);
                r[k] = new Point3D(x[k], y[k], 0);
            }
            chartPanel.addElement(Calc3dFactory.createCurve(r));
        }

        Preferences preferences = chartPanel.getSettings().getPreferences();
        preferences.setClipBox(
                new Box3D(xminMax.min(), xminMax.max(),
                        yminMax.min(), yminMax.max(),
                        zminMax.min(), zminMax.max())
        );
        chartPanel.applySettings(preferences,true);
    }
}
