package net.thevpc.scholar.hadruplot.console;

public class LogConsoleAction implements ConsoleAction {
    private static final long serialVersionUID = 1L;
    private String msg;

    public LogConsoleAction(String msg) {
        this.msg = msg;
    }

    public void execute(PlotConsole plotter) {
        plotter.silentLogLn(msg);
    }
}
