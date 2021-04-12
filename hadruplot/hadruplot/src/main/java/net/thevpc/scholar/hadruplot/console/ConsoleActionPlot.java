package net.thevpc.scholar.hadruplot.console;

import net.thevpc.common.swing.win.WindowPath;
import net.thevpc.scholar.hadruplot.component.ValuesPlotPanel;
import net.thevpc.scholar.hadruplot.model.ValuesPlotModel;
import net.thevpc.common.swing.JListCardPanel;
import net.thevpc.scholar.hadruplot.PlotMatrix;
import net.thevpc.scholar.hadruplot.*;

import javax.swing.*;

public class ConsoleActionPlot implements ConsoleAction {
    private static final long serialVersionUID = 1L;
    private PlotMatrix yvalues;
    private String plotTitle;
    private String plotGroup;
    private double infiniteValue;
    private PlotType plotType;
    private WindowPath preferredPath;
    private PlotDoubleConverter toDoubleConverter;
    private String library;


    public ConsoleActionPlot(PlotMatrix yvalues, String plotTitle, String plotGroup, double infiniteValue, PlotType plotType, PlotDoubleConverter toDoubleConverter, WindowPath preferredPath, String library) {
        this.yvalues = yvalues;
        this.plotTitle = plotTitle;
        this.infiniteValue = infiniteValue;
        this.plotType = plotType;
        this.plotGroup = plotGroup;
        this.preferredPath = preferredPath;
        this.toDoubleConverter = toDoubleConverter;
        this.library = library;
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
                    pageComponent = (JComponent) Plot.create(new ValuesPlotModel().setPlotType(new LibraryPlotType(PlotType.CURVE,library)), Plot.getDefaultWindowManager());
                    window.addChild(PlotPath.of(plotGroup), pageComponent);
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
                        PlotPath.of(plotTitle),
                        (JComponent) Plot.create(new ValuesPlotModel(
                                plotTitle,
                                null, null, null, yvalues.getRows(), yvalues.getColumns(), yvalues.getMatrix(),
                                toDoubleConverter, new LibraryPlotType(PlotType.HEATMAP, library), null), Plot.getDefaultWindowManager())
                );
                break;
            }
            case MESH: {
                plotter.getPlotConsoleFrame().getWindow(preferredPath)
                        .addChild(
                                PlotPath.of(plotTitle),
                                (JComponent) Plot.create(new ValuesPlotModel(
                                        plotTitle,
                                        null, null, null, yvalues.getRows(), yvalues.getColumns(), yvalues.getMatrix(),
                                        toDoubleConverter, new LibraryPlotType(PlotType.MESH,library), null), Plot.getDefaultWindowManager())
                        );
                break;
            }
        }
    }
}
