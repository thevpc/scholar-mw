package net.vpc.scholar.hadrumaths.plot.console;

import net.vpc.scholar.hadrumaths.plot.AbstractPlotWindowManager;
import net.vpc.scholar.hadrumaths.plot.PlotComponent;

/**
 * @author taha.bensalah@gmail.com on 7/22/16.
 */
public class PlotConsoleWindowManager extends AbstractPlotWindowManager {
    private PlotConsole plotConsole;

    public PlotConsoleWindowManager(PlotConsole plotConsole) {
        this.plotConsole = plotConsole;
    }

    private WindowPath createWindowPath(String plotTitle){
        if(plotTitle==null){
            plotTitle="";
        }
        plotTitle=plotTitle.trim();
        if(plotTitle.length()==0){
            return new WindowPath("Plot","Default");
        }
        int slash = plotTitle.indexOf("/");
        if(slash <0){
            return new WindowPath("Plot",plotTitle);
        }else{
            String p = plotTitle.substring(0, slash);
            String q = plotTitle.substring(slash+1);
            if(p.length()==0){
                p="Plot";
            }
            if(q.length()==0){
                q="Default";
            }
            return new WindowPath(p,q);
        }
    }
    @Override
    public void removePlotComponentImpl(PlotComponent component) {
        plotConsole.getMainPlotterFrame().removeWindow(component.toComponent());
    }

    @Override
    public void addPlotComponentImpl(PlotComponent component) {
        ConsoleWindow w = plotConsole.getMainPlotterFrame().getWindow(createWindowPath(component.getPlotTitle()));
        w.setComponent(component.toComponent());
    }
}
