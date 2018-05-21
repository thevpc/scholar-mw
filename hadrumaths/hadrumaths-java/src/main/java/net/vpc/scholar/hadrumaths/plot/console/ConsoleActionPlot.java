package net.vpc.scholar.hadrumaths.plot.console;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.ExternalLibrary;
import net.vpc.scholar.hadrumaths.Plot;
import net.vpc.scholar.hadrumaths.plot.ComplexAsDouble;
import net.vpc.scholar.hadrumaths.plot.PlotType;
import net.vpc.scholar.hadrumaths.plot.ValuesPlotModel;
import net.vpc.scholar.hadrumaths.plot.ValuesPlotPanel;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.NamedMatrix;
import net.vpc.scholar.hadrumaths.plot.swings.JListCardPanel;

import javax.swing.*;
import java.util.Set;

public class ConsoleActionPlot implements ConsoleAction {
    private static final long serialVersionUID = 1L;
    private NamedMatrix yvalues;
    private String plotTitle;
    private String plotGroup;
    private double infiniteValue;
    private PlotType plotType;
    private WindowPath preferredPath;
    private ComplexAsDouble complexAsDouble;
    private Set<ExternalLibrary> preferredLibraries;


    public ConsoleActionPlot(NamedMatrix yvalues, String plotTitle, String plotGroup, double infiniteValue, PlotType plotType, ComplexAsDouble complexAsDouble, WindowPath preferredPath, Set<ExternalLibrary> preferredLibraries) {
        this.yvalues = yvalues;
        this.plotTitle = plotTitle;
        this.infiniteValue = infiniteValue;
        this.plotType = plotType;
        this.plotGroup = plotGroup;
        this.preferredPath = preferredPath;
        this.complexAsDouble = complexAsDouble;
        this.preferredLibraries = preferredLibraries;
    }

    public void execute(PlotConsole plotter) {
        if (yvalues == null) {
            return;
        }
        Complex[][] m = yvalues.getMatrix();
        if (!Double.isNaN(infiniteValue)) {
            for (Complex[] yvalue : m) {
                for (int j = 0; j < yvalue.length; j++) {
                    if (!yvalue[j].isNaN() && !yvalue[j].isInfinite() && yvalue[j].absdbl() > infiniteValue) {
                        yvalue[j] = Complex.NaN;
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
                    pageComponent = (JComponent) Plot.create(new ValuesPlotModel().setPreferredLibraries(preferredLibraries), Plot.getDefaultWindowManager());
                    ((ValuesPlotPanel) pageComponent).getModel().setPlotType(PlotType.CURVE);
                    window.addChild(plotGroup, pageComponent);
                }
                ValuesPlotModel model = (ValuesPlotModel) ((ValuesPlotPanel) pageComponent).getModel();
                model.setConverter(complexAsDouble);
                Complex[][] matrix = yvalues.getMatrix();
                for (int i = 0; i < matrix.length; i++) {
                    Complex[] yvalue = matrix[i];
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
                                complexAsDouble, PlotType.HEATMAP, null).setPreferredLibraries(preferredLibraries), Plot.getDefaultWindowManager())
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
                                        complexAsDouble, PlotType.MESH, null).setPreferredLibraries(preferredLibraries), Plot.getDefaultWindowManager())
                        );
                break;
            }
        }
    }
}
