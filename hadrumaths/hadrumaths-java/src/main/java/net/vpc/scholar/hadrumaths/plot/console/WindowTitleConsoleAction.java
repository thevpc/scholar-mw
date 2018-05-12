package net.vpc.scholar.hadrumaths.plot.console;

public class WindowTitleConsoleAction implements ConsoleAction {
    private static final long serialVersionUID = 1L;
    private String windowTitle;

    public WindowTitleConsoleAction(String windowTitle) {
        this.windowTitle = windowTitle;
    }

    public void execute(PlotConsole plotter) {
        plotter.silentSetFrameTitle(windowTitle);
    }
}
