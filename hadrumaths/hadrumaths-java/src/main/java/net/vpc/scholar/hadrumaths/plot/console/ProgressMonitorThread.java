package net.vpc.scholar.hadrumaths.plot.console;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 6 janv. 2007 12:23:10
 */
class ProgressMonitorThread extends UserStopThread {
    private PlotConsole plotConsole;

    public ProgressMonitorThread(PlotConsole plotConsole, long sleep) {
        super(sleep);
        this.plotConsole = plotConsole;
        setName("ProgressMonitorThread");
    }

    public void execute() {
        plotConsole.ticMonitor();
    }
}
