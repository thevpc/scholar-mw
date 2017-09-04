package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.DoubleArray2;
import net.vpc.scholar.hadrumaths.DoubleList;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class ValuesPlotXYDoubleModelFace {
    private String title;
    private double[] x;
    private double[] y;
    private String[] ytitles;
    private double[][] z;

    public ValuesPlotXYDoubleModelFace(ValuesPlotModel model,PlotConfig plotConfig) {
        double[][] x0 = model.getX();
        double[][] y0 = model.getY();
        Complex[][] z0 = model.getZ();

        if(plotConfig==null){
            plotConfig = (PlotConfig) model.getProperty("config", null);
            plotConfig=PlotConfig.copy(plotConfig).validate(z0.length);
        }
        double defaultXMultiplier=plotConfig.getDefaultXMultiplier(1);

        this.x = PlotModelUtils.mul(ArrayUtils.toValidOneDimArray(x0),defaultXMultiplier);
        this.y = ArrayUtils.toValidOneDimArray(y0, this.x.length);
        this.z = Maths.toDouble(ArrayUtils.toValidTwoDimArray(z0), model.getZDoubleFunction());

        if (this.x.length == 0) {
            this.x = this.z.length == 0 ? new double[0] : Maths.dsteps(0, this.z[0].length - 1, 1.0);
        }
        if (this.y == null) {
            this.y = this.z.length == 0 ? new double[0] : Maths.dsteps(0, this.z.length - 1, 1.0);
        }

        title = model.getTitle();
        DoubleList yl = (DoubleList) Maths.dlist(this.y.length);
        List<String> ys=new ArrayList<>(this.y.length);
        DoubleArray2 zl = new DoubleArray2(this.y.length);
        for (int i = 0; i < this.y.length; i++) {
            double ymultiplier=plotConfig.getYMultiplierAt(i,1);
            double v = this.y[i]*ymultiplier;
            if (model.getYVisible(i)) {
                yl.append(v);
                zl.appendRow(this.z[i]);
                ys.add(PlotModelUtils.resolveYTitle(model,i));
            }
        }
        this.y = yl.toDoubleArray();
        this.z = zl.toDoubleArray();
        ytitles = ys.toArray(new String[ys.size()]);
    }

    public ValuesPlotXYDoubleModelFace(double[] x0, double[] y0, double[][] z0, String title,String[] ytitles) {
        this.x = ArrayUtils.toValidOneDimArray(x0);
        this.y = ArrayUtils.toValidOneDimArray(y0);
        this.z = ArrayUtils.toValidTwoDimArray(z0);

        if (this.x.length == 0) {
            this.x = this.z.length == 0 ? new double[0] : Maths.dsteps(0, this.z[0].length - 1, 1.0);
        }
        if (this.y == null) {
            this.y = this.z.length == 0 ? new double[0] : Maths.dsteps(0, this.z.length - 1, 1.0);
        }

        this.title = title;
        DoubleList yl = (DoubleList) Maths.dlist(this.y.length);
        List<String> ys=new ArrayList<>(this.y.length);
        DoubleArray2 zl = new DoubleArray2(this.y.length);
        for (int i = 0; i < this.y.length; i++) {
            double v = this.y[i];
            yl.append(v);
            zl.appendRow(this.z[i]);
            ys.add(PlotModelUtils.resolveYTitle(ytitles,i));
        }
        this.y = yl.toDoubleArray();
        this.z = zl.toDoubleArray();
        this.ytitles = ys.toArray(new String[ys.size()]);
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
}
