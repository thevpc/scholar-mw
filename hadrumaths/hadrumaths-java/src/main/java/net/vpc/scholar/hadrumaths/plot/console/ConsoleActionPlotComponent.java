package net.vpc.scholar.hadrumaths.plot.console;

import net.vpc.scholar.hadrumaths.plot.*;

public class ConsoleActionPlotComponent implements ConsoleAction {
    public static final long serialVersionUID = -1231231231240000002L;
    private String path;
    private PlotComponent component;


    public ConsoleActionPlotComponent(PlotComponent component,String path) {
        this.component = component;
        this.path = path;
    }

    public void execute(PlotConsole plotter) {
        if (component == null) {
            return;
        }
        plotter.displayImpl(component,path);
    }
}
