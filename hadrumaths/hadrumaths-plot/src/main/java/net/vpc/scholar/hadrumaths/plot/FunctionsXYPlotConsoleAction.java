package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.plot.model.ExpressionsPlotModel;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadruplot.CellPosition;
import net.vpc.scholar.hadruplot.Plot;
import net.vpc.scholar.hadruplot.PlotType;
import net.vpc.scholar.hadruplot.console.ConsoleAction;
import net.vpc.scholar.hadruplot.console.PlotConsole;
import net.vpc.scholar.hadruplot.console.WindowPath;

import javax.swing.*;
import net.vpc.scholar.hadruplot.LibraryPlotType;
import net.vpc.scholar.hadruplot.console.PlotConfigManager;


public class FunctionsXYPlotConsoleAction implements ConsoleAction {
    private static final long serialVersionUID = 1L;
    private final DoubleToVector[] fn;
    private final String title;
    private final Domain domain;
    private final WindowPath preferredPath;
    private final LibraryPlotType plotType;

    public FunctionsXYPlotConsoleAction(String typeTitle, String title, DoubleToVector[] fn, Domain domain, WindowPath preferredPath, LibraryPlotType plotType, String libraries) {
        this.fn = fn;
        this.title = (title == null || title.length() == 0) ? typeTitle : title;
        this.domain = domain;
        this.preferredPath = new WindowPath(preferredPath, typeTitle);
        this.plotType = plotType;
    }

    public void execute(PlotConsole plotter) {
        ExpressionsPlotModel m = new ExpressionsPlotModel();
        m.setDomain(
                PlotConfigManager.getPlotDomainResolvers().resolve(domain)
        ).setExpressions(fn)
                //.setLibraries(libraries)
                .setSelectedAxis(new CellPosition[]{new CellPosition(0, 0)})
                .setTitle(title)
                .setPlotType(plotType)
        ;
        plotter.getPlotConsoleFrame().getWindow(preferredPath).addChild(
                title, (JComponent) Plot.create(m, Plot.getDefaultWindowManager())
        );
    }
}