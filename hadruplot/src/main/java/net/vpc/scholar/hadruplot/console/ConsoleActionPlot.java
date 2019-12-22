package net.vpc.scholar.hadruplot.console;

import net.vpc.common.swings.JListCardPanel;
import net.vpc.scholar.hadruplot.PlotMatrix;
import net.vpc.scholar.hadruplot.*;

import javax.swing.*;
import java.util.Set;

public class ConsoleActionPlot implements ConsoleAction {
    private static final long serialVersionUID = 1L;
    private PlotMatrix yvalues;
    private String plotTitle;
    private String plotGroup;
    private double infiniteValue;
    private PlotType plotType;
    private WindowPath preferredPath;
    private PlotDoubleConverter toDoubleConverter;
    private String libraries;


    public ConsoleActionPlot(PlotMatrix yvalues, String plotTitle, String plotGroup, double infiniteValue, PlotType plotType, PlotDoubleConverter toDoubleConverter, WindowPath preferredPath, String libraries) {
        this.yvalues = yvalues;
        this.plotTitle = plotTitle;
        this.infiniteValue = infiniteValue;
        this.plotType = plotType;
        this.plotGroup = plotGroup;
        this.preferredPath = preferredPath;
        this.toDoubleConverter = toDoubleConverter;
        this.libraries = libraries;
    }

    public void execute(PlotConsole plotter) {
        if (yvalues == null) {
            return;
        }
        Object[][] m = yvalues.getMatrix();
        if (!Double.isNaN(infiniteValue)) {
            for (Object[] yvalue : m) {
                for (int j = 0; j < yvalue.length; j++) {
                    double d = PlotConfigManager.Numbers.toDouble(yvalue[j]);

                    if (!Double.isNaN(d) && !Double.isInfinite(d) && Math.abs(d) > infiniteValue) {
                        yvalue[j] = Double.NaN;
                    }
                }
            }
        }
        switch (plotType) {
            case CURVE: {
                ConsoleWindow window = plotter.getPlotConsoleFrame().getWindow(preferredPath);
                JComponent component = window.getComponent();
                if (component == null) {
                    component = new JListCardPanel();
                    window.setComponent(component);
                }
                JListCardPanel list = (JListCardPanel) component;
                JComponent pageComponent = list.getPageComponent(plotGroup);
                if (pageComponent == null) {
                    pageComponent = (JComponent) Plot.create(new ValuesPlotModel().setLibraries(libraries), Plot.getDefaultWindowManager());
                    ((ValuesPlotPanel) pageComponent).getModel().setPlotType(PlotType.CURVE);
                    window.addChild(plotGroup, pageComponent);
                }
                ValuesPlotModel model = (ValuesPlotModel) ((ValuesPlotPanel) pageComponent).getModel();
                model.setConverter(toDoubleConverter);
                Object[][] matrix = yvalues.getMatrix();
                for (int i = 0; i < matrix.length; i++) {
                    Object[] yvalue = matrix[i];
                    model.addX(yvalues.getColumns());
                    model.addYTitle(yvalues.getRowTitles() == null ? plotTitle : yvalues.getRowTitles()[i]);
                    model.addZ(yvalue);
                }
                break;
            }
            case HEATMAP: {
                plotter.getPlotConsoleFrame().getWindow(preferredPath).addChild(
                        plotTitle,
                        (JComponent) Plot.create(new ValuesPlotModel(
                                plotTitle,
                                null, null, null, yvalues.getRows(), yvalues.getColumns(), yvalues.getMatrix(),
                                toDoubleConverter, PlotType.HEATMAP, null).setLibraries(libraries), Plot.getDefaultWindowManager())
                );
                break;
            }
            case MESH: {
                plotter.getPlotConsoleFrame().getWindow(preferredPath)
                        .addChild(
                                plotTitle,
                                (JComponent) Plot.create(new ValuesPlotModel(
                                        plotTitle,
                                        null, null, null, yvalues.getRows(), yvalues.getColumns(), yvalues.getMatrix(),
                                        toDoubleConverter, PlotType.MESH, null).setLibraries(libraries), Plot.getDefaultWindowManager())
                        );
                break;
            }
        }
    }
}