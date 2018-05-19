package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.DoubleList;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class ValuesPlotXYComplexModelFace {
    private String title;
    private double[] x;
    private double[] y;
    private int[] initialIndexes;
    private String[] ytitles;
    private Complex[][] z;

    public ValuesPlotXYComplexModelFace(ValuesPlotModel model, PlotConfig plotConfig) {
        if (plotConfig == null) {
            plotConfig = (PlotConfig) model.getProperty("config", null);
            plotConfig = PlotConfig.copy(plotConfig).validate(model.getZ().length);
        }
        List<Integer> initialIndexesList = new ArrayList<>();
        double defaultXMultiplier = plotConfig.getDefaultXMultiplier(1);

        x = PlotModelUtils.mul(ArrayUtils.toValidOneDimArray(model.getX(), (z == null || z.length == 0) ? -1 : z[0].length), defaultXMultiplier);
        z = ArrayUtils.toValidTwoDimArray(model.getZ());
        y = ArrayUtils.toValidOneDimArray(model.getY(), z.length);

        if (x.length == 0) {
            x = z.length == 0 ? new double[0] : Maths.dsteps(0, z[0].length - 1, 1.0);
        }
        if (y == null) {
            y = z.length == 0 ? new double[0] : Maths.dsteps(0, z.length - 1, 1.0);
        }

        title = model.getTitle();
        DoubleList yl = (DoubleList) Maths.dlist(y.length);
        List<String> ys = new ArrayList<>(y.length);
        List<Complex[]> zl = new ArrayList<>(y.length);
        for (int i = 0; i < y.length; i++) {
            double ymultiplier = plotConfig.getYMultiplierAt(i, 1);
            double v = y[i] * ymultiplier;
            if (model.getYVisible(i)) {
                initialIndexesList.add(i);
                yl.append(v);
                zl.add(z[i]);
                ys.add(PlotModelUtils.resolveYTitle(model, i));
            }
        }
        initialIndexes = ArrayUtils.unboxIntegerList(initialIndexesList);
        y = yl.toDoubleArray();
        z = zl.toArray(new Complex[zl.size()][]);
        ytitles = ys.toArray(new String[ys.size()]);
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

    public Complex getZ(int xi, int yi) {
        return z[yi][xi];
    }

    public Complex[][] getZ() {
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
