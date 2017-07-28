package net.vpc.scholar.hadrumaths.plot.console;

import java.io.Serializable;

public interface ConsoleAction extends Serializable {
    void execute(PlotConsole plotter);
}
