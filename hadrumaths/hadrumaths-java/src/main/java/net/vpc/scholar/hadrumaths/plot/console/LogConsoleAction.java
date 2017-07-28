package net.vpc.scholar.hadrumaths.plot.console;

public class LogConsoleAction implements ConsoleAction {
    public static final long serialVersionUID = -1231231231240000001L;
    private String msg;

    public LogConsoleAction(String msg) {
        this.msg=msg;
    }

    public void execute(PlotConsole plotter) {
        plotter.silentLogLn(msg);
    }
}
