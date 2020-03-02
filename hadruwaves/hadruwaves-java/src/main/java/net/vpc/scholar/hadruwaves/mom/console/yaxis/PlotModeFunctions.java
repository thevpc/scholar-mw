package net.vpc.scholar.hadruwaves.mom.console.yaxis;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.ProgressMonitors;
import net.vpc.scholar.hadruplot.console.ConsoleAction;
import net.vpc.scholar.hadruplot.console.ConsoleActionParams;
import net.vpc.scholar.hadrumaths.plot.FunctionsXYPlotConsoleAction;
import net.vpc.scholar.hadruplot.console.yaxis.PlotAxisCustom;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

import java.util.ArrayList;
import java.util.Iterator;

public class PlotModeFunctions extends PlotAxisCustom implements Cloneable {
    public PlotModeFunctions(YType... types) {
        super("ModeFunctions", types);
    }

    public Iterator<ConsoleAction> createConsoleActionIterator(ConsoleActionParams p) {
        ProgressMonitor mon = ProgressMonitors.nonnull(this);
//        mon.startm(getName());
        ArrayList<ConsoleAction> all = new ArrayList<ConsoleAction>();
        if (containsType(YType.REFERENCE)) {
            MomStructure str = (MomStructure) p.getStructure();
            ModeFunctions base = str.getModeFunctions();
            DoubleToVector[] fn = base.arr();
            //WallBorders b = base.getBorders();
            all.add(new FunctionsXYPlotConsoleAction("Mode Functions", "Direct Base Functions " + p.getSerieTitle().toString(), fn, null, p.getPreferredPath(), getPlotType(), getLibraries()));
        }
        MomStructure str2 = (MomStructure) p.getStructure2();
        if (containsType(YType.MODELED) && str2 != null) {
            all.add(new FunctionsXYPlotConsoleAction("Mode Functions", "Model Base Functions" + p.getSerieTitle().toString(), str2.getModeFunctions().arr(), null, p.getPreferredPath(), getPlotType(), getLibraries()));
        }
//        mon.terminatem(getName());
        return all.iterator();
    }

}
