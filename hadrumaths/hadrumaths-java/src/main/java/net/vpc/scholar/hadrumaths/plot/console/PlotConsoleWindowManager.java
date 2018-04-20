package net.vpc.scholar.hadrumaths.plot.console;

import net.vpc.scholar.hadrumaths.plot.AbstractPlotContainer;
import net.vpc.scholar.hadrumaths.plot.AbstractPlotWindowManager;
import net.vpc.scholar.hadrumaths.plot.PlotComponent;
import net.vpc.scholar.hadrumaths.plot.PlotContainer;
import net.vpc.scholar.hadrumaths.util.StringUtils;

import javax.swing.*;

/**
 * @author taha.bensalah@gmail.com on 7/22/16.
 */
public class PlotConsoleWindowManager extends AbstractPlotWindowManager {
    private PlotConsole plotConsole;
    PlotContainer rootContainer = new AbstractPlotContainer() {
        @Override
        public void removePlotComponentImpl(PlotComponent component) {
            plotConsole.getPlotConsoleFrame().removeWindow(component.toComponent());
        }

        @Override
        public void addComponentImpl(PlotComponent component, int index) {
            plotConsole.display(component);
            //if(index<=0) {
//                ConsoleWindow w = plotConsole.getPlotConsoleFrame().getWindow(createWindowPath(component.getPlotTitle()));
//                w.setComponent(component.toComponent());
            //}
        }

        @Override
        public PlotComponent getPlotComponent(int index) {
            return null;
        }

        @Override
        public int getPlotComponentsCount() {
            return 0;
        }


        @Override
        public void clear() {

        }

        @Override
        public int indexOfPlotComponent(PlotComponent plotComponent) {
            return 0;
        }

        @Override
        public JComponent toComponent() {
            return null;
        }
    };
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
    public PlotContainer getRootContainer() {
        return rootContainer;
    }

    public void addPlotComponentImpl(PlotComponent component, String[] path) {
        plotConsole.display(component, StringUtils.toPath(path,"/"));
    }
}
