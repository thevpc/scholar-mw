package net.vpc.scholar.hadrumaths.plot.console;

import net.vpc.scholar.hadrumaths.Plot;
import net.vpc.scholar.hadrumaths.plot.PlotComponent;

import java.io.File;
import java.io.IOException;

public class ConsoleActionPlotFile implements ConsoleAction {
    private static final long serialVersionUID = 1L;
    private File file;


    public ConsoleActionPlotFile(File file) {
        this.file = file;
    }


    public void execute(PlotConsole plotter) {
        if (file == null || !file.exists()) {
            return;
        }
        PlotComponent plot = null;
        try {
            plot = Plot.loadPlot(file);
        } catch (IOException e) {
            plotter.getLog().error(e);
        }
        if (plot == null) {
            return;
        }
        plotter.displayImpl(plot, null);
    }
}
