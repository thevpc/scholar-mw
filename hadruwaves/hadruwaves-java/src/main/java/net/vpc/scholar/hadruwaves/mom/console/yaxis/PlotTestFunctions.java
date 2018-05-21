package net.vpc.scholar.hadruwaves.mom.console.yaxis;

import net.vpc.scholar.hadrumaths.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.PlotAxisCustom;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleAction;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;
import net.vpc.scholar.hadrumaths.plot.console.FunctionsXYPlotConsoleAction;
import net.vpc.scholar.hadrumaths.monitors.EnhancedProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

import java.util.ArrayList;
import java.util.Iterator;

public class PlotTestFunctions extends PlotAxisCustom implements Cloneable {
    public PlotTestFunctions(YType... type) {
        super("TestFunctions", type);
    }

    public Iterator<ConsoleAction> createConsoleActionIterator(ConsoleActionParams p) {
        EnhancedProgressMonitor mon = ProgressMonitorFactory.enhance(this);
//        mon.startm(getName());
        ArrayList<ConsoleAction> all = new ArrayList<ConsoleAction>();
        if (containsType(YType.REFERENCE)) {
            MomStructure str1 = (MomStructure)p.getStructure();
            Domain d = str1.getDomain();
            //TODO FIX ME
            all.add(new FunctionsXYPlotConsoleAction("Test Functions", "Direct Test Functions " + p.getSerieTitle().toString(),
                    str1.getTestFunctions().arr(),
                    d,
                    p.getPreferredPath(), getPlotType()
                    ,getPreferredLibraries()
            ));
        }
        if (containsType(YType.MODELED)) {
            MomStructure str2 = (MomStructure)p.getStructure2();
            if (str2 != null) {
                all.add(new FunctionsXYPlotConsoleAction("Test Functions", "Model Test Functions " + p.getSerieTitle().toString(), str2.getTestFunctions().arr(), str2.getDomain(), p.getPreferredPath(), getPlotType(),getPreferredLibraries()));
            }
        }
//        mon.terminatem(getName());
        return all.iterator();
    }

}
