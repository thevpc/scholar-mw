package net.vpc.scholar.hadruplot.model;


import net.vpc.scholar.hadruplot.model.ValuesPlotModel;
import net.vpc.common.util.ArrayUtils;
import net.vpc.common.util.DoubleArrayList;
import net.vpc.scholar.hadruplot.util.PlotModelUtils;
import net.vpc.scholar.hadruplot.util.PlotUtils;

import java.util.ArrayList;
import java.util.List;
import net.vpc.scholar.hadruplot.PlotViewConfig;

public class ValuesPlotXYComplexModelFace {
    private String title;
    private double[] x;
    private double[] y;
    private int[] initialIndexes;
    private String[] ytitles;
    private Object[][] z;

    public ValuesPlotXYComplexModelFace(ValuesPlotModel model, PlotViewConfig plotViewConfig) {
        if (plotViewConfig == null) {
            plotViewConfig = (PlotViewConfig) model.getProperty("config", null);
            plotViewConfig = PlotViewConfig.copy(plotViewConfig).validate(model.getZ().length);
        }
        List<Integer> initialIndexesList = new ArrayList<>();
        double defaultXMultiplier = plotViewConfig.getDefaultXMultiplier(1);

        x = PlotModelUtils.mul(PlotUtils.toValidOneDimArray(model.getX(), (z == null || z.length == 0) ? -1 : z[0].length), defaultXMultiplier);
        z = PlotUtils.toValidTwoDimArray(model.getZ());
        y = PlotUtils.toValidOneDimArray(model.getY(), z.length);

        if (x.length == 0) {
            x = z.length == 0 ? new double[0] : ArrayUtils.dsteps(0, z[0].length - 1, 1.0);
        }
        if (y == null) {
            y = z.length == 0 ? new double[0] : ArrayUtils.dsteps(0, z.length - 1, 1.0);
        }

        title = model.getTitle();
        DoubleArrayList yl = new DoubleArrayList(y.length);
        List<String> ys = new ArrayList<>(y.length);
        List<Object[]> zl = new ArrayList<>(y.length);
        for (int i = 0; i < y.length; i++) {
            double ymultiplier = plotViewConfig.getYMultiplierAt(i, 1);
            double v = y[i] * ymultiplier;
            if (model.getYVisible(i)) {
                initialIndexesList.add(i);
                yl.add(v);
                zl.add(z[i]);
                ys.add(PlotModelUtils.resolveYTitle(model, i));
            }
        }
        initialIndexes = ArrayUtils.unboxIntegerList(initialIndexesList);
        y = yl.toArray();
        z = zl.toArray(new Object[0][]);
        ytitles = ys.toArray(new String[0]);
    }

//    public ValuesPlotXYComplexModelFace(double[] x, double[] y, Complex[][] z, String title) {
//        this.x = x;
//        this.y = y;
//        this.z = z;
//        this.title = title;
//    }

    public int getInitialIndex(int series) {
        return initialIndexes[series];
    }

    public double[] getX() {
        return x;
    }

    public double[] getY() {
        return y;
    }

    public Object getZ(int xi, int yi) {
        return z[yi][xi];
    }

    public Object[][] getZ() {
        return z;
    }

    public String getTitle() {
        return title;
    }

    public String getYTitle(int index) {
        return ytitles[index];
    }

    public int size() {
        return y.length;
    }
}
