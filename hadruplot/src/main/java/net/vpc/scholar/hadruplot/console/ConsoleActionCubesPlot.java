package net.vpc.scholar.hadruplot.console;

import net.vpc.scholar.hadruplot.*;

import java.util.Set;

public class ConsoleActionCubesPlot implements ConsoleAction {
    private static final long serialVersionUID = 1L;
    private PlotHyperCube yvalues;
    private String plotTitle;
    private String plotGroup;
    private double infiniteValue;
    private PlotType plotType;
    private WindowPath preferredPath;
    private String libraries;


    public ConsoleActionCubesPlot(PlotHyperCube yvalues, String plotTitle, String plotGroup, double infiniteValue, PlotType plotType, WindowPath preferredPath, String libraries) {
        this.yvalues = yvalues;
        this.plotTitle = plotTitle;
        this.infiniteValue = infiniteValue;
        this.plotType = plotType;
        this.plotGroup = plotGroup;
        this.preferredPath = preferredPath;
        this.libraries = libraries;
    }

    public void execute(PlotConsole plotter) {
        if (yvalues == null) {
            return;
        }
        plotter.getPlotConsoleFrame().getWindow(preferredPath)
                .addChild(
                        "/" + plotTitle,
                        Plot.create(
                                new PlotHyperCubePlotModel()
                                        .setLibraries(libraries)
                                        .setVdiscretes(yvalues)
                                        .setTitle(plotTitle)
                                ,
                                Plot.getDefaultWindowManager()
                        ).toComponent()
                );
    }
}