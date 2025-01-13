package net.thevpc.scholar.hadruwaves.mom.console.yaxis;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.scholar.hadruplot.console.ConsoleAction;
import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.scholar.hadrumaths.plot.FunctionsXYPlotConsoleAction;
import net.thevpc.scholar.hadruplot.console.yaxis.PlotAxisCustom;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadruwaves.mom.ModeFunctions;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

import java.util.ArrayList;
import java.util.Iterator;
import net.thevpc.scholar.hadruplot.LibraryPlotType;

public class PlotModeFunctions extends PlotAxisCustom implements Cloneable {
    public PlotModeFunctions(YType... types) {
        super("ModeFunctions", types);
    }

    @Override
    public Iterator<ConsoleAction> createConsoleActionIterator(ConsoleActionParams p) {
        ProgressMonitor mon = ProgressMonitors.nonnull(this);
//        mon.startm(getName());
        ArrayList<ConsoleAction> all = new ArrayList<ConsoleAction>();
        if (containsType(YType.REFERENCE)) {
            MomStructure str = (MomStructure) p.getStructure();
            ModeFunctions base = str.modeFunctions();
            DoubleToVector[] fn = base.arr();
            //WallBorders b = base.getBorders();
            all.add(new FunctionsXYPlotConsoleAction("Mode Functions", "Direct Base Functions " + p.getSerieTitle().toString(), fn, null, p.getPreferredPath(), new LibraryPlotType(getPlotType()), getLibraries()));
        }
        MomStructure str2 = (MomStructure) p.getStructure2();
        if (containsType(YType.MODELED) && str2 != null) {
            all.add(new FunctionsXYPlotConsoleAction("Mode Functions", "Model Base Functions" + p.getSerieTitle().toString(), str2.modeFunctions().arr(), null, p.getPreferredPath(), new LibraryPlotType(getPlotType()), getLibraries()));
        }
//        mon.terminatem(getName());
        return all.iterator();
    }

}
