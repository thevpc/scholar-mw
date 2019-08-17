package net.vpc.scholar.hadruplot;

import net.vpc.common.util.ArrayUtils;
import net.vpc.common.util.DoubleArrayList;
import net.vpc.common.util.DoubleFormat;
import net.vpc.scholar.hadruplot.util.PlotModelUtils;
import net.vpc.scholar.hadruplot.util.PlotUtils;

import java.util.ArrayList;
import java.util.List;

public class ValuesPlotXYDoubleModelFace {
    private String title;
    private double[] x;
    private double[] y;
    private int[] initialIndexes;
    private String[] ytitles;
    private double[][] z;
    private DoubleFormat xformat;
    private DoubleFormat yformat;
    private DoubleFormat zformat;

    public ValuesPlotXYDoubleModelFace(ValuesPlotModel model, PlotConfig plotConfig) {
        double[][] x0 = model.getX();
        double[][] y0 = model.getY();
        Object[][] z0 = model.getZ();
        xformat = model.getXformat();
        yformat = model.getYformat();
        zformat = model.getZformat();
        List<Integer> initialIndexesList = new ArrayList<>();
        if (plotConfig == null) {
            plotConfig = (PlotConfig) model.getProperty("config", null);
            plotConfig = PlotConfig.copy(plotConfig).validate(z0.length);
        }
        double defaultXMultiplier = plotConfig.getDefaultXMultiplier(1);

        this.x = PlotModelUtils.mul(PlotUtils.toValidOneDimArray(x0, (z == null || z.length == 0) ? -1 : z[0].length), defaultXMultiplier);
        PlotDoubleConverter converter = model.getConverter();
        if(converter==null){
            converter= PlotDoubleConverter.ABS;
        }
        this.z = PlotDoubleConverter.intern(converter).toDouble(PlotUtils.toValidTwoDimArray(z0));
        this.y = PlotUtils.toValidOneDimArray(y0, this.z.length);

        if (this.x.length == 0) {
            this.x = this.z.length == 0 ? new double[0] : ArrayUtils.dsteps(0, this.z[0].length - 1, 1.0);
        }
        if (this.y == null) {
            this.y = this.z.length == 0 ? new double[0] : ArrayUtils.dsteps(0, this.z.length - 1, 1.0);
        }

        title = model.getTitle();
        DoubleArrayList yl = new DoubleArrayList(this.y.length);
        List<String> ys = new ArrayList<>(this.y.length);
        List<DoubleArrayList> zl = new ArrayList<>(this.y.length);
        for (int i = 0; i < this.y.length; i++) {
            double ymultiplier = plotConfig.getYMultiplierAt(i, 1);
            double v = this.y[i] * ymultiplier;
            if (model.getYVisible(i)) {
                initialIndexesList.add(i);
                yl.add(v);
                zl.add(i < this.z.length ? new DoubleArrayList(this.z[i]) : new DoubleArrayList(new double[]{0}));
                ys.add(PlotModelUtils.resolveYTitle(model, i));
            }
        }
        initialIndexes = ArrayUtils.unboxIntegerList(initialIndexesList);
        this.y = yl.toArray();
        this.z = new double[zl.size()][];
        for (int i = 0; i < this.z.length; i++) {
            this.z[i]=zl.get(i).toArray();
        }
        ytitles = ys.toArray(new String[0]);
    }

    public ValuesPlotXYDoubleModelFace(double[] x0, double[] y0, double[][] z0, String title, String[] ytitles) {
        this.x = PlotUtils.toValidOneDimArray(x0);
        this.y = PlotUtils.toValidOneDimArray(y0);
        this.z = PlotUtils.toValidTwoDimArray(z0);

        if (this.x.length == 0) {
            this.x = this.z.length == 0 ? new double[0] : ArrayUtils.dsteps(0, this.z[0].length - 1, 1.0);
        }
        if (this.y == null) {
            this.y = this.z.length == 0 ? new double[0] : ArrayUtils.dsteps(0, this.z.length - 1, 1.0);
        }

        this.title = title;
        DoubleArrayList yl = (DoubleArrayList) new DoubleArrayList(this.y.length);
        List<String> ys = new ArrayList<>(this.y.length);
        List<DoubleArrayList> zl = new ArrayList<>(this.y.length);
        for (int i = 0; i < this.y.length; i++) {
            double v = this.y[i];
            yl.add(v);
            zl.add(new DoubleArrayList(this.z[i]));
            ys.add(PlotModelUtils.resolveYTitle(ytitles, i));
        }
        this.y = yl.toArray();
        this.z = new double[zl.size()][];
        for (int i = 0; i < this.z.length; i++) {
            this.z[i]=zl.get(i).toArray();
        }
        this.ytitles = ys.toArray(new String[0]);
    }

    public int getInitialIndex(int series) {
        return initialIndexes[series];
    }

    public double[] getX() {
        return x;
    }

    public double[] getY() {
        return y;
    }

    public double getZ(int xi, int yi) {
        return z[yi][xi];
    }

    public double[][] getZ() {
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

    public DoubleFormat getXformat() {
        return xformat;
    }

    public DoubleFormat getYformat() {
        return yformat;
    }

    public DoubleFormat getZformat() {
        return zformat;
    }
}
