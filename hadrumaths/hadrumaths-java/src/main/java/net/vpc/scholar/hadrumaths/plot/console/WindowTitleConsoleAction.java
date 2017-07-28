package net.vpc.scholar.hadrumaths.plot.console;

public class WindowTitleConsoleAction implements ConsoleAction {
    public static final long serialVersionUID = -1231231231240000006L;
    private String windowTitle;

    public WindowTitleConsoleAction(String windowTitle) {
        this.windowTitle = windowTitle;
    }

    public void execute(PlotConsole plotter) {
        plotter.silentSetWindowTitle(windowTitle);
    }
}
