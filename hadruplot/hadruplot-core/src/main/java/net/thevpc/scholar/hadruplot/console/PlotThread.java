package net.thevpc.scholar.hadruplot.console;

import net.thevpc.common.mon.AbstractProgressMonitor;
import net.thevpc.common.swing.SwingUtilities3;
import net.thevpc.common.swing.win.WindowPath;
import net.thevpc.common.time.Chronometer;
import net.thevpc.scholar.hadruplot.console.params.ParamSet;
import net.thevpc.scholar.hadruplot.console.yaxis.PlotAxis;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import net.thevpc.common.msg.Message;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 6 janv. 2007 12:22:57
 */
public class PlotThread extends AbstractProgressMonitor {
    private PlotAxis currentY = null;
    private ComputeTitle serieTitle;
    private ConsoleAwareObject direct;
    private ConsoleAwareObject modele;
    private Chronometer chronometer;
    private ConsoleAxis axis;
    private PlotConsole plotter;
    private PlotData plotData;
    //    private ProgressMonitorHelper ph = new ProgressMonitorHelper();
    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            PlotThread.this.run();
        }
    }, "PlotThread");

    public PlotThread(PlotAxis currentY, PlotData plotData, ComputeTitle serieTitle, ConsoleAwareObject direct, ConsoleAwareObject modele, ConsoleAxis axis, PlotConsole plotter) {
        super(nextId());
        chronometer = Chronometer.start();
        this.currentY = currentY;
        this.serieTitle = serieTitle;
        this.direct = direct;
        this.modele = modele;
        this.axis = axis;
        this.plotter = plotter;
        this.plotData = plotData;
    }


    public Chronometer getChronometer() {
        return chronometer;
    }

    private void run() {
        chronometer.restart();
        try {
            try {
                if (currentY.containsType(YType.REFERENCE) || currentY.containsType(YType.ABSOLUTE_ERROR) || currentY.containsType(YType.RELATIVE_ERROR)) {
                    if (direct == null) {
                        throw new IllegalArgumentException("Y Type is " + Arrays.asList(currentY.getTypes()) + " but direct is null");
                    }
                }
                if (currentY.containsType(YType.MODELED) || currentY.containsType(YType.ABSOLUTE_ERROR) || currentY.containsType(YType.RELATIVE_ERROR)) {
                    if (direct == null) {
                        throw new IllegalArgumentException("Y Type is " + Arrays.asList(currentY.getTypes()) + " but modele is null");
                    }
                }
                ArrayList<String> all = new ArrayList<String>();
                all.add(plotData.getWindowTitle());
                if (null != currentY.getPreferredPath()) {
                    for (String s : currentY.getPreferredPath().toArray()) {
                        for (ParamSet param : plotData.getParams()) {
                            s = s.replace("{" + param.getName() + "}", String.valueOf(param.getValue()));
                        }
                        all.add(s);
                    }
                } else {
                    all.add("");//default
                }
                WindowPath path = new WindowPath(all.toArray(new String[0]));
                for (Iterator<ConsoleAction> action = currentY.createConsoleActionIterator(new ConsoleActionParams(serieTitle, direct, modele, axis, plotter, path)); action.hasNext(); ) {
                    plotter.run(action.next());
                }
            } catch (IllegalArgumentException e) {
                plotter.getLog().error(e);
            } catch (ThreadDeath e) {
                //do nothing
            } catch (Throwable e) {
                plotter.getLog().error(e);
            }
            SwingUtilities3.invokeLater(new Runnable() {
                @Override
                public void run() {
                    plotter.ticMonitor();
                }
            });
            plotter.getLog().trace("End Running : " + currentY.getName(serieTitle) + " => Time=" + chronometer);
        } finally {
            terminate();
        }
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        terminate();
    }

    public PlotAxis getCurrentY() {
        return currentY;
    }

    public String getWindowTitle() {
        return plotData.getWindowTitle();
    }

    public ComputeTitle getSerieTitle() {
        return serieTitle;
    }

    public void cancel() {
        super.cancel();
        thread.stop();
    }

    @Override
    protected void setMessageImpl(Message message) {
        getCurrentY().setMessage(message);
    }


    @Override
    public double getProgress() {
        return getCurrentY().getProgress();
    }

    @Override
    public Message getMessage() {
        PlotAxis currentY = getCurrentY();
        return currentY.getMessage();
    }

    protected void startImpl() {
        super.startImpl();
        thread.start();
    }

    @Override
    protected void terminateImpl(boolean b) {
        if (b) {
            super.startImpl();
            thread.stop();
        }
    }

    @Override
    protected void setProgressImpl(double progress) {
        this.getCurrentY().setProgress(progress);
    }
}
