package net.vpc.scholar.hadruplot.console;

import java.io.Serializable;

public interface ConsoleAction extends Serializable {
    void execute(PlotConsole plotter);
}
