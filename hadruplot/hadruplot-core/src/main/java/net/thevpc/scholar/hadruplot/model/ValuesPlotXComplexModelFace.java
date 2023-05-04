package net.thevpc.scholar.hadruplot.model;

import net.thevpc.scholar.hadruplot.model.ValuesPlotModel;
import net.thevpc.common.util.ArrayUtils;
import net.thevpc.scholar.hadruplot.util.PlotModelUtils;
import net.thevpc.scholar.hadruplot.util.PlotUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.thevpc.scholar.hadruplot.PlotViewConfig;

public class ValuesPlotXComplexModelFace {
    private String title;
    private double[][] x;
    private Object[][] y;
    private int[] initialIndexes;
    private String[] ytitles;

    public ValuesPlotXComplexModelFace(ValuesPlotModel model, PlotViewConfig plotViewConfig) {
        this.title = model.getTitle();

        double[][] xAxis = model.getX();
        Object[][] yAxis = model.getZ();

        List<Integer> initialIndexesList = new ArrayList<>();
        List<double[]> xAxisList = new ArrayList<>();
        List<Object[]> yAxisList = new ArrayList<>();
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
                        xAxisList.add(PlotModelUtils.mul(ArrayUtils.dsteps(1, yAxis[i].length,1), xmultiplier));
                        yAxisList.add(PlotUtils.mul(yAxis[i], ymultiplier));
                        yTitleList.add(PlotModelUtils.resolveYTitle(model, i));
                    }
                }
            } else {
                for (int i = 0; i < yAxis.length; i++) {
                    if (model.getYVisible(i)) {
                        initialIndexesList.add(i);
                        double xmultiplier = plotViewConfig.getXMultiplierAt(i, 1) * defaultXMultiplier;
                        double ymultiplier = plotViewConfig.getYMultiplierAt(i, 1);
                        yAxisList.add(PlotUtils.mul(yAxis[i], ymultiplier));
                        yTitleList.add(PlotModelUtils.resolveYTitle(model, i));
                        if (i >= xAxis.length || xAxis[i] == null || xAxis[i].length == 0 || xAxis[i].length < yAxis[i].length) {
                            if (xAxisList.isEmpty() || yAxis[i].length != xAxisList.get(xAxisList.size() - 1).length) {
                                boolean ok = false;
                                for (int j = Math.min(i - 1, xAxis.length - 1); j >= 0; j--) {
                                    if (xAxis[j] != null && yAxis[i].length == xAxis[j].length) {
                                        ok = true;
                                        xAxisList.add(PlotModelUtils.mul(xAxis[j], xmultiplier));
                                        break;
                                    }
                                }
                                if (!ok) {
                                    xAxisList.add(PlotModelUtils.mul(ArrayUtils.dsteps(1, yAxis[i].length,1), xmultiplier));
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
        x = xAxisList.toArray(new double[0][]);
        y = yAxisList.toArray(new Object[0][]);
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

    public Object[] getY(int series) {
        return y[series];
    }

    public int size() {
        return y.length;
    }

    public int getSeriesLength() {
        int len = 0;
        for (Object[] doubles : y) {
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
