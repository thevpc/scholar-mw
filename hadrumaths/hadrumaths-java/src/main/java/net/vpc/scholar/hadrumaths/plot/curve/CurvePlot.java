package net.vpc.scholar.hadrumaths.plot.curve;

import net.vpc.scholar.hadrumaths.plot.*;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CurvePlot extends JPanel implements PlotComponentPanel {
    private PlotModelProvider modelProvider;
    private boolean USER_JFREECHART;
    private PlotComponentPanel subPanel;

    public CurvePlot(PlotModelProvider modelProvider, boolean USER_JFREECHART) {
        super(new BorderLayout());
        this.modelProvider = modelProvider;
        this.USER_JFREECHART = USER_JFREECHART;
        ValuesPlotModel model = (ValuesPlotModel) this.modelProvider.getModel();
        model.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateChart();
            }
        });
        updateChart();
    }

    private void updateChart() {
//        ValuesPlotModel model = (ValuesPlotModel) this.modelProvider.getModel();
//        String theTitle  = model.getTitle() == null ? "" : model.getTitle();
//        String theXTitle = model.getXtitle() == null ? "X" : model.getXtitle();
//        String theYTitle = model.getZtitle() == null ? "Y" : model.getZtitle();
//        String[] ytitles = model.getYtitles();
//        double[][] xAxis = model.getX();
//        double[][] yAxis = model.getZd();
        for (Component component : getComponents()) {
            remove(component);
        }
        if (USER_JFREECHART) {
            add((subPanel = new PlotCanvasCurveJFreeChart(this.modelProvider)).toComponent());
        } else {
            add((subPanel = new PlotCanvasCurveSimple(this.modelProvider)).toComponent());
//            ArrayList<Curve> cc = new ArrayList<Curve>();
//            MinMax x_minmax = new MinMax();
//            MinMax y_minmax = new MinMax();
//            //        double[][] zValues=getZ();
//            for (int i = 0; i < yAxis.length; i++) {
//                if (ytitles != null && ytitles.length > i) {
//                    if (!model.getYVisible(i)) {
//                        continue;
//                    }
//                }
//                double[] xxf = new double[yAxis[i].length];
//                double[] yyf = new double[yAxis[i].length];
//                for (int k = 0; k < yAxis[i].length; k++) {
//                    double yf = yAxis[i][k];
//                    double xf = (xAxis.length <= i ? xAxis[xAxis.length - 1] : xAxis[i])[k];
//                    x_minmax.registerValue(xf);
//                    y_minmax.registerValue(yf);
//                    xxf[k] = xf;
//                    yyf[k] = yf;
//                }
//                Curve series = new Curve((ytitles == null || (i == 0 && ytitles.length == 0)) ? (model.getZtitle() == null ? "Y" : model.getZtitle()) : !(i < ytitles.length) ? ytitles[ytitles.length - 1] : ytitles[i], xxf, yyf);
//                cc.add(series);
//            }
//            JComponent comp=new CurveStat(cc.toArray(new Curve[cc.size()]), theTitle, 400, 400);
//            add(comp);
//            PlotModelUtils.buildJPopupMenu(comp,this.modelProvider,true);
        }
        invalidate();
        revalidate();
    }

    @Override
    public JComponent toComponent() {
        return this;
    }

    @Override
    public JPopupMenu getPopupMenu() {
        return subPanel.getPopupMenu();
    }
    //    public ValuesPlotModel getModel() {
//        return this.modelProvider.getModel();
//    }
//
//    public Component getComponent() {
//        return this;
//    }
}

