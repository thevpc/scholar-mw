package net.vpc.scholar.hadrumaths.plot.console;

import net.vpc.scholar.hadrumaths.ExternalLibrary;
import net.vpc.scholar.hadrumaths.Plot;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadrumaths.plot.*;

import java.util.Set;

public class ConsoleActionCubesPlot implements ConsoleAction {
    public static final long serialVersionUID = -1231231231240000006L;
    private VDiscrete yvalues;
    private String plotTitle;
    private String plotGroup;
    private double infiniteValue;
    private PlotType plotType;
    private WindowPath preferredPath;
    private Set<ExternalLibrary> preferredLibraries;


    public ConsoleActionCubesPlot(VDiscrete yvalues, String plotTitle, String plotGroup, double infiniteValue, PlotType plotType, WindowPath preferredPath,Set<ExternalLibrary> preferredLibraries) {
        this.yvalues = yvalues;
        this.plotTitle = plotTitle;
        this.infiniteValue = infiniteValue;
        this.plotType = plotType;
        this.plotGroup = plotGroup;
        this.preferredPath = preferredPath;
        this.preferredLibraries = preferredLibraries;
    }

    public void execute(PlotConsole plotter) {
        if (yvalues == null) {
            return;
        }
        plotter.getPlotConsoleFrame().getWindow(preferredPath)
                .addChild(
                        plotTitle,
                        Plot.create(
                                new VDiscretePlotModel()
                                .setTitle(plotTitle)
                                .setPreferredLibraries(preferredLibraries)
                                .setVdiscretes(yvalues)
                                ,
                                Plot.getDefaultWindowManager()
                        ).toComponent()
                );
    }
}