package net.vpc.scholar.hadrumaths.plot.console;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 août 2007 15:54:23
 */
public class ConsoleActionParams {
    private ComputeTitle serieTitle;
    private ConsoleAwareObject structure;
    private ConsoleAwareObject structure2;
    private ConsoleAxis axis;
    private PlotConsole plotter;
    private WindowPath preferredPath;

    public ConsoleActionParams(ComputeTitle serieTitle, ConsoleAwareObject structure, ConsoleAwareObject structure2, ConsoleAxis axis, PlotConsole plotter, WindowPath preferredPath) {
        this.serieTitle = serieTitle;
        this.structure = structure;
        this.structure2 = structure2;
        this.axis = axis;
        this.plotter = plotter;
        this.preferredPath = preferredPath;
    }

    public ComputeTitle getSerieTitle() {
        return serieTitle;
    }

    public ConsoleAwareObject getStructure() {
        return structure;
    }

    public ConsoleAwareObject getStructure2() {
        return structure2;
    }

    public ConsoleAxis getAxis() {
        return axis;
    }

    public PlotConsole getPlotter() {
        return plotter;
    }

    public WindowPath getPreferredPath() {
        return preferredPath;
    }
}
