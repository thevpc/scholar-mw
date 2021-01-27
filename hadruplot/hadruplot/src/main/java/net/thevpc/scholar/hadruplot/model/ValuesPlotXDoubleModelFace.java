package net.thevpc.scholar.hadruplot.model;

import net.thevpc.common.util.ArrayUtils;
import net.thevpc.scholar.hadruplot.util.PlotModelUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.thevpc.scholar.hadruplot.PlotDoubleConverter;
import net.thevpc.scholar.hadruplot.PlotModelType;
import net.thevpc.scholar.hadruplot.PlotViewConfig;

public class ValuesPlotXDoubleModelFace {

    private String title;
    private double[][] x;
    private double[][] y;
    private int[] initialIndexes;
    private String[] ytitles;

    public ValuesPlotXDoubleModelFace(ValuesPlotModel model, PlotViewConfig plotViewConfig) {
        this.title = model.getTitle();

        if (model.accept(PlotModelType.NUMBER_1)) {
            PlotModel1D m1d = model.toPlotModel1D();
            double[][] xAxis = new double[][]{m1d.get1dX()};
            double[][] yAxis = new double[][]{PlotDoubleConverter.toDouble(m1d.getConverter(),m1d.get1dY())};
            List<Integer> initialIndexesList = new ArrayList<>();
            List<double[]> xAxisList = new ArrayList<>();
            List<double[]> yAxisList = new ArrayList<>();
            List<String> yTitleList = new ArrayList<>();
            if (plotViewConfig == null) {
                plotViewConfig = (PlotViewConfig) model.getProperty("config", null);
                plotViewConfig = PlotViewConfig.copy(plotViewConfig).validate(model.getZ().length);
            }

            double defaultXMultiplier = plotViewConfig.getDefaultXMultiplier(1);
            if (xAxis == null) {
                for (int i = 0; i < yAxis.length; i++) {
                    if (model.getYVisible(i)) {
                        initialIndexesList.add(i);
                        double xmultiplier = plotViewConfig.getXMultiplierAt(i, 1) * defaultXMultiplier;
                        double ymultiplier = plotViewConfig.getYMultiplierAt(i, 1);
                        xAxisList.add(PlotModelUtils.mul(ArrayUtils.dsteps(1, yAxis[i].length, 1), xmultiplier));
                        yAxisList.add(PlotModelUtils.mul(yAxis[i], ymultiplier));
                        yTitleList.add(PlotModelUtils.resolveYTitle(model, i));
                    }
                }
            } else {
                double[][] dd = null;
                dd = yAxis;
                for (int i = 0; i < dd.length; i++) {
                    double[] yAxi = dd[i];
                    if (model.getYVisible(i)) {
                        initialIndexesList.add(i);
                        double xmultiplier = plotViewConfig.getXMultiplierAt(i, 1) * defaultXMultiplier;
                        double ymultiplier = plotViewConfig.getYMultiplierAt(i, 1);
                        //jfreechart does not accept infinity
                        double[] mul = PlotModelUtils.replaceInfinityWithNaN(PlotModelUtils.mul(yAxi, ymultiplier));
                        yAxisList.add(mul);
                        yTitleList.add(PlotModelUtils.resolveYTitle(model, i));
                        if (i >= xAxis.length || xAxis[i] == null || xAxis[i].length == 0 || xAxis[i].length < yAxi.length) {
                            if (xAxisList.isEmpty() || yAxi.length != xAxisList.get(xAxisList.size() - 1).length) {
                                boolean ok = false;
                                for (int j = Math.min(i - 1, xAxis.length - 1); j >= 0; j--) {
                                    if (xAxis[j] != null && yAxi.length == xAxis[j].length) {
                                        ok = true;
                                        xAxisList.add(PlotModelUtils.mul(xAxis[j], xmultiplier));
                                        break;
                                    }
                                }
                                if (!ok) {
                                    xAxisList.add(PlotModelUtils.mul(ArrayUtils.dsteps(1, yAxi.length, 1), xmultiplier));
                                }
                            } else {
                                xAxisList.add(xAxisList.get(xAxisList.size() - 1));
                            }
                        } else {
                            xAxisList.add(PlotModelUtils.mul(xAxis[i], xmultiplier));
                        }
                    }
                }
            }

            x = xAxisList.toArray(new double[xAxisList.size()][]);
            y = yAxisList.toArray(new double[yAxisList.size()][]);
            initialIndexes = ArrayUtils.unboxIntegerList(initialIndexesList);

            //rename titles with the same name
            HashSet<String> visited = new HashSet<>();
            for (int i = 0; i < yTitleList.size(); i++) {
                String key = yTitleList.get(i);
                int keyIndex = 1;
                while (true) {
                    String kk = keyIndex == 1 ? key : (key + " " + keyIndex);
                    if (!visited.contains(kk)) {
                        visited.add(kk);
                        key = kk;
                        break;
                    }
                    visited.add(kk);
                    keyIndex++;
                }
                yTitleList.set(i, key);
            }
            ytitles = yTitleList.toArray(new String[0]);

        } else {

            double[][] xAxis = model.getX();
            double[][] x2Axis = model.getY();
            double[][] yAxis = null;
            double[][] zd0 = model.getZd();
            boolean swappedXY = false;
            if (zd0 != null && zd0.length > 0 && xAxis != null && x2Axis != null && xAxis.length > 1 && xAxis[0].length == 1 && x2Axis.length > 0 && x2Axis[0].length > 0) {
                double[][] sw = xAxis;
                xAxis = x2Axis;
                x2Axis = sw;
                yAxis = new double[1][zd0.length];
                for (int i = 0; i < zd0.length; i++) {
                    if (zd0[i].length > 0) {
                        yAxis[0][i] = zd0[i][0];
                    }
                }
                swappedXY = true;
            } else {
                yAxis = zd0;
            }

            List<Integer> initialIndexesList = new ArrayList<>();
            List<double[]> xAxisList = new ArrayList<>();
            List<double[]> yAxisList = new ArrayList<>();
            List<String> yTitleList = new ArrayList<>();
            if (plotViewConfig == null) {
                plotViewConfig = (PlotViewConfig) model.getProperty("config", null);
                plotViewConfig = PlotViewConfig.copy(plotViewConfig).validate(model.getZ().length);
            }
            if (yAxis == null) {
                //do nothing
            } else {
                double defaultXMultiplier = plotViewConfig.getDefaultXMultiplier(1);
                if (xAxis == null) {
                    for (int i = 0; i < yAxis.length; i++) {
                        if (model.getYVisible(i)) {
                            initialIndexesList.add(i);
                            double xmultiplier = plotViewConfig.getXMultiplierAt(i, 1) * defaultXMultiplier;
                            double ymultiplier = plotViewConfig.getYMultiplierAt(i, 1);
                            xAxisList.add(PlotModelUtils.mul(ArrayUtils.dsteps(1, yAxis[i].length, 1), xmultiplier));
                            yAxisList.add(PlotModelUtils.mul(yAxis[i], ymultiplier));
                            yTitleList.add(PlotModelUtils.resolveYTitle(model, i));
                        }
                    }
                } else {
                    double[][] dd = null;
                    if (yAxis.length == 1 && yAxis[0].length > 0) {
                        dd = new double[yAxis[0].length][1];
                        for (int i = 0; i < yAxis[0].length; i++) {
                            dd[i][0] = yAxis[0][i];
                        }
                    } else {
                        dd = yAxis;
                    }
                    for (int i = 0; i < dd.length; i++) {
                        double[] yAxi = dd[i];
                        if (model.getYVisible(i)) {
                            initialIndexesList.add(i);
                            double xmultiplier = plotViewConfig.getXMultiplierAt(i, 1) * defaultXMultiplier;
                            double ymultiplier = plotViewConfig.getYMultiplierAt(i, 1);
                            yAxisList.add(PlotModelUtils.replaceInfinityWithNaN(PlotModelUtils.mul(yAxi, ymultiplier)));
                            yTitleList.add(PlotModelUtils.resolveYTitle(model, i));
                            if (i >= xAxis.length || xAxis[i] == null || xAxis[i].length == 0 || xAxis[i].length < yAxi.length) {
                                if (xAxisList.isEmpty() || yAxi.length != xAxisList.get(xAxisList.size() - 1).length) {
                                    boolean ok = false;
                                    for (int j = Math.min(i - 1, xAxis.length - 1); j >= 0; j--) {
                                        if (xAxis[j] != null && yAxi.length == xAxis[j].length) {
                                            ok = true;
                                            xAxisList.add(PlotModelUtils.mul(xAxis[j], xmultiplier));
                                            break;
                                        }
                                    }
                                    if (!ok) {
                                        xAxisList.add(PlotModelUtils.mul(ArrayUtils.dsteps(1, yAxi.length, 1), xmultiplier));
                                    }
                                } else {
                                    xAxisList.add(xAxisList.get(xAxisList.size() - 1));
                                }
                            } else {
                                xAxisList.add(PlotModelUtils.mul(xAxis[i], xmultiplier));
                            }
                        }
                    }
                }
            }
            x = xAxisList.toArray(new double[xAxisList.size()][]);
            y = yAxisList.toArray(new double[yAxisList.size()][]);
            initialIndexes = ArrayUtils.unboxIntegerList(initialIndexesList);

            //rename titles with the same name
            HashSet<String> visited = new HashSet<>();
            for (int i = 0; i < yTitleList.size(); i++) {
                String key = yTitleList.get(i);
                int keyIndex = 1;
                while (true) {
                    String kk = keyIndex == 1 ? key : (key + " " + keyIndex);
                    if (!visited.contains(kk)) {
                        visited.add(kk);
                        key = kk;
                        break;
                    }
                    visited.add(kk);
                    keyIndex++;
                }
                yTitleList.set(i, key);
            }
            ytitles = yTitleList.toArray(new String[0]);
        }
    }

//    public ValuesPlotXDoubleModelFace(double[] x, double[] y, double[][] z, String title) {
//        this.x = x;
//        this.y = y;
//        this.z = z;
//        this.title = title;
//    }
    public int getInitialIndex(int series) {
        return initialIndexes[series];
    }

    public double[] getX(int series) {
        return x[series];
    }

    public String getYTitle(int series) {
        return ytitles[series];
    }

    public double[] getY(int series) {
        return y[series];
    }

    public int size() {
        return y.length;
    }

    public int getSeriesLength() {
        int len = 0;
        for (double[] doubles : y) {
            if (doubles.length > len) {
                len = doubles.length;
            }
        }
        return len;
    }

    public String getTitle() {
        return title;
    }
}
