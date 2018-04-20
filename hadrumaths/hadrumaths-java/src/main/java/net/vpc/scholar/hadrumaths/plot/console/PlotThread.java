package net.vpc.scholar.hadrumaths.plot.console;

import net.vpc.scholar.hadrumaths.plot.console.params.ParamSet;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.PlotAxis;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.Chronometer;
import net.vpc.scholar.hadrumaths.util.ProgressMonitor;
import net.vpc.scholar.hadrumaths.util.ProgressMessage;
import net.vpc.scholar.hadrumaths.util.StringProgressMessage;
import net.vpc.scholar.hadrumaths.util.SwingUtils;


import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;
import java.util.logging.Level;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 6 janv. 2007 12:22:57
 */
public class PlotThread extends Thread implements ProgressMonitor {
    private PlotAxis currentY = null;
    private ComputeTitle serieTitle;
    private ConsoleAwareObject direct;
    private ConsoleAwareObject modele;
    private Chronometer chronometer;
    private ConsoleAxis axis;
    private PlotConsole plotter;
    private boolean stopped;
    private PlotData plotData;

    public PlotThread(PlotAxis currentY, PlotData plotData, ComputeTitle serieTitle, ConsoleAwareObject direct, ConsoleAwareObject modele, ConsoleAxis axis, PlotConsole plotter) {
        super("PlotThread");
        chronometer = new Chronometer();
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

    public void run() {
        chronometer.start();
        try {
            try {
                if(currentY.containsType(YType.REFERENCE) || currentY.containsType(YType.ABSOLUTE_ERROR) || currentY.containsType(YType.RELATIVE_ERROR)){
                    if (direct == null) {
                        throw new IllegalArgumentException("Y Type is " + Arrays.asList(currentY.getTypes()) + " but direct is null");
                    }
                }
                if(currentY.containsType(YType.MODELED) || currentY.containsType(YType.ABSOLUTE_ERROR) || currentY.containsType(YType.RELATIVE_ERROR)){
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
                WindowPath path = new WindowPath(all.toArray(new String[all.size()]));
                for (Iterator<ConsoleAction> action =currentY.createConsoleActionIterator(new ConsoleActionParams(serieTitle, direct, modele, axis, plotter, path));action.hasNext();) {
                    plotter.run(action.next());
                }
            } catch (IllegalArgumentException e) {
                plotter.getLog().error(e);
            } catch (ThreadDeath e) {
                //do nothing
            } catch (Throwable e) {
                plotter.getLog().error(e);
            }
            SwingUtils.invokeLater(new Runnable() {
                @Override
                public void run() {
                    plotter.ticMonitor();
                }
            });
            plotter.getLog().trace("End Running : " + currentY.getName(serieTitle) + " => Time=" + chronometer);
        } finally {
            stopped = true;
        }
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        stopped = true;
    }

    public boolean isStopped() {
        return stopped;
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

    public void cancel(){
        stop();
    }


    @Override
    public double getProgressValue() {
        return getCurrentY().getProgressValue();
    }

    @Override
    public ProgressMessage getProgressMessage() {
        PlotAxis currentY = getCurrentY();
        return new StringProgressMessage(Level.FINE, currentY.getName()+"-"+currentY.getProgressMessage());
    }

    @Override
    public void setProgress(double progress, ProgressMessage message) {
        //
    }

    @Override
    public boolean isCanceled() {
        return false;
    }
}
