package net.vpc.scholar.hadruwaves.mom.console.yaxis;

import net.vpc.scholar.hadrumaths.ComputationMonitorFactory;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.PlotAxisCustom;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.util.EnhancedComputationMonitor;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleAction;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;
import net.vpc.scholar.hadrumaths.plot.console.FunctionsXYPlotConsoleAction;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

import java.util.Iterator;
import java.util.ArrayList;

public class PlotModeFunctions extends PlotAxisCustom implements Cloneable {
    public PlotModeFunctions(YType... types) {
        super("ModeFunctions", types);
    }

    public Iterator<ConsoleAction> createConsoleActionIterator(ConsoleActionParams p){
        EnhancedComputationMonitor mon = ComputationMonitorFactory.enhance(this);
//        mon.startm(getName());
        ArrayList<ConsoleAction> all=new ArrayList<ConsoleAction>();
        if(containsType(YType.REFERENCE)){
            MomStructure str = (MomStructure) p.getStructure();
            ModeFunctions base = str.getModeFunctions();
            DoubleToVector[] fn = base.arr();
            //WallBorders b = base.getBorders();
            all.add(new FunctionsXYPlotConsoleAction("Mode Functions","Direct Base Functions "+p.getSerieTitle().toString(), fn,null,p.getPreferredPath(), getPlotType(),getPreferredLibraries()));
        }
        MomStructure str2 = (MomStructure) p.getStructure2();
        if(containsType(YType.MODELED) && str2 !=null){
            all.add(new FunctionsXYPlotConsoleAction("Mode Functions","Model Base Functions"+p.getSerieTitle().toString(), str2.getModeFunctions().arr(),null,p.getPreferredPath(), getPlotType(),getPreferredLibraries()));
        }
//        mon.terminatem(getName());
        return all.iterator();
    }

}
