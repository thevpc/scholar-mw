package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadruplot.CellPosition;
import net.vpc.scholar.hadruplot.Plot;
import net.vpc.scholar.hadruplot.PlotType;
import net.vpc.scholar.hadruplot.console.ConsoleAction;
import net.vpc.scholar.hadruplot.console.PlotConsole;
import net.vpc.scholar.hadruplot.console.WindowPath;

import javax.swing.*;
import java.util.Set;


public class FunctionsXYPlotConsoleAction implements ConsoleAction {
    private static final long serialVersionUID = 1L;
    private DoubleToVector[] fn;
    private String title;
    private Domain domain;
    private WindowPath preferredPath;
    private PlotType plotType;
    private String libraries;

    public FunctionsXYPlotConsoleAction(String typeTitle, String title, DoubleToVector[] fn, Domain domain, WindowPath preferredPath, PlotType plotType, String libraries) {
        this.fn = fn;
        this.title = (title == null || title.length() == 0) ? typeTitle : title;
        this.domain = domain;
        this.preferredPath = new WindowPath(preferredPath, typeTitle);
        this.plotType = plotType;
        this.libraries = libraries;
    }

    public void execute(PlotConsole plotter) {
        ExpressionsPlotModel m = new ExpressionsPlotModel();
        m.setDomain(domain).setExpressions(fn)
                .setPlotType(plotType)
                .setLibraries(libraries)
                .setSelectedAxis(new CellPosition[]{new CellPosition(0, 0)})
                .setTitle(title)
        ;
        plotter.getPlotConsoleFrame().getWindow(preferredPath).addChild(
                title, (JComponent) Plot.create(m, Plot.getDefaultWindowManager())
        );
    }
}