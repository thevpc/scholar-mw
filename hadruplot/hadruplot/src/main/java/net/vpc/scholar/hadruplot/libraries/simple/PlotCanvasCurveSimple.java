/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruplot.libraries.simple;

import net.vpc.scholar.hadruplot.model.ValuesPlotModel;
import net.vpc.common.util.ArrayUtils;
import net.vpc.common.util.MinMax;
import net.vpc.scholar.hadruplot.libraries.simple.curve.Curve;
import net.vpc.scholar.hadruplot.libraries.simple.curve.CurveStat;
import net.vpc.scholar.hadruplot.util.PlotUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import net.vpc.scholar.hadruplot.PlotComponentPanel;
import net.vpc.scholar.hadruplot.extension.PlotModelProvider;

/**
 * @author vpc
 */
public class PlotCanvasCurveSimple extends JPanel implements PlotComponentPanel {

    private PlotModelProvider modelProvider;
    private CurveStat chartPanel;

    public PlotCanvasCurveSimple(PlotModelProvider modelProvider) {
        super(new BorderLayout());
        this.modelProvider = modelProvider;
        ValuesPlotModel model = (ValuesPlotModel) modelProvider.getModel();
        String theTitle = model.getPreferredTitle();
        String theXTitle = model.getXtitle() == null ? "X" : model.getXtitle();
        String theYTitle = model.getZtitle() == null ? "Y" : model.getZtitle();
        String[] ytitles = model.getYtitles();
        double[][] xAxis = model.getX();
        double[][] yAxis = model.getZd();
        if (xAxis == null || xAxis.length == 0) {
            if (yAxis == null || yAxis.length == 0) {
                xAxis = new double[0][];
                yAxis = new double[0][];
            } else {
                double[] xx = ArrayUtils.dtimes(1.0, yAxis[0].length, yAxis[0].length);
                xAxis = new double[yAxis.length][];
                for (int i = 0; i < yAxis.length; i++) {
                    xAxis[i] = xx;
                }
            }
        }
        ArrayList<Curve> cc = new ArrayList<Curve>();
        MinMax x_minmax = new MinMax();
        MinMax y_minmax = new MinMax();
        //        double[][] zValues=getZ();
        for (int i = 0; i < yAxis.length; i++) {
            if (ytitles != null && ytitles.length > i) {
                if (!model.getYVisible(i)) {
                    continue;
                }
            }
            double[] xxf = new double[yAxis[i].length];
            double[] yyf = new double[yAxis[i].length];
            for (int k = 0; k < yAxis[i].length; k++) {
                double yf = yAxis[i][k];
                double xf = (xAxis.length <= i ? xAxis[xAxis.length - 1] : xAxis[i])[k];
                x_minmax.registerValue(xf);
                y_minmax.registerValue(yf);
                xxf[k] = xf;
                yyf[k] = yf;
            }
            Curve series = new Curve(
                    (ytitles == null || (/*i == 0 && */ytitles.length == 0)) ?
                            (model.getZtitle() == null ? "Y" : model.getZtitle())
                            : !(i < ytitles.length) ? ytitles[ytitles.length - 1] : ytitles[i], xxf, yyf);
            cc.add(series);
        }

        chartPanel = new CurveStat(cc.toArray(new Curve[0]), theTitle, 400, 400);
        add(chartPanel);
//        Plot.buildJPopupMenu(chartPanel, modelProvider);
    }

    @Override
    public JComponent toComponent() {
        return this;
    }

    @Override
    public JPopupMenu getPopupMenu() {
        return PlotUtils.getOrCreateComponentPopupMenu(chartPanel);
    }
}
