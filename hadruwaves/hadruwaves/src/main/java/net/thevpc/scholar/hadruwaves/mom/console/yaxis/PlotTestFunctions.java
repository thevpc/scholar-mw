package net.thevpc.scholar.hadruwaves.mom.console.yaxis;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadruplot.console.yaxis.PlotAxisCustom;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadruplot.console.ConsoleAction;
import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.scholar.hadrumaths.plot.FunctionsXYPlotConsoleAction;

import net.thevpc.scholar.hadruwaves.mom.MomStructure;

import java.util.ArrayList;
import java.util.Iterator;
import net.thevpc.scholar.hadruplot.LibraryPlotType;

public class PlotTestFunctions extends PlotAxisCustom implements Cloneable {
    public PlotTestFunctions(YType... type) {
        super("TestFunctions", type);
    }

    public Iterator<ConsoleAction> createConsoleActionIterator(ConsoleActionParams p) {
        ProgressMonitor mon = ProgressMonitors.nonnull(this);
//        mon.startm(getName());
        ArrayList<ConsoleAction> all = new ArrayList<ConsoleAction>();
        if (containsType(YType.REFERENCE)) {
            MomStructure str1 = (MomStructure)p.getStructure();
            Domain d = str1.getDomain();
            //TODO FIX ME
            all.add(new FunctionsXYPlotConsoleAction("Test Functions", "Direct Test Functions " + p.getSerieTitle().toString(),
                    str1.testFunctions().arr(),
                    d,
                    p.getPreferredPath(), new LibraryPlotType(getPlotType())
                    ,getLibraries()
            ));
        }
        if (containsType(YType.MODELED)) {
            MomStructure str2 = (MomStructure)p.getStructure2();
            if (str2 != null) {
                all.add(new FunctionsXYPlotConsoleAction("Test Functions", "Model Test Functions " + p.getSerieTitle().toString(), str2.testFunctions().arr(), str2.getDomain(), p.getPreferredPath(), new LibraryPlotType(getPlotType()),getLibraries()));
            }
        }
//        mon.terminatem(getName());
        return all.iterator();
    }

}
