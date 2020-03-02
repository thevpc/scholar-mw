package net.vpc.scholar.hadruwaves.mom.console.yaxis;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.ProgressMonitors;
import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.CustomDDFunctionXY;
import net.vpc.scholar.hadrumaths.symbolic.CustomDDFunctionXYExpr;
import net.vpc.scholar.hadruplot.PlotType;
import net.vpc.scholar.hadruplot.console.ConsoleAction;
import net.vpc.scholar.hadruplot.console.ConsoleActionParams;
import net.vpc.scholar.hadrumaths.plot.FunctionsXYPlotConsoleAction;
import net.vpc.scholar.hadruplot.console.yaxis.PlotAxisCustom;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.sources.PlanarSources;
import net.vpc.scholar.hadruwaves.mom.sources.Sources;

import java.util.ArrayList;
import java.util.Iterator;

public class PlotStructureDefinition extends PlotAxisCustom implements Cloneable {

    public PlotStructureDefinition(YType... type) {
        super("PlotStructureDefinition", type);
        setPlotType(PlotType.HEATMAP);
    }

    @Override
    public Iterator<ConsoleAction> createConsoleActionIterator(ConsoleActionParams p) {
        ProgressMonitor mon = ProgressMonitors.nonnull(this);
//        mon.startm(getName());
        ArrayList<ConsoleAction> all = new ArrayList<ConsoleAction>();
        MomStructure str1 = (MomStructure) p.getStructure();
        MomStructure str2 = (MomStructure) p.getStructure2();
        if (containsType(YType.REFERENCE)) {
            DoubleToVector[] ff = null;
            Sources ss = str1.getSources();
            DoubleToVector[] sf = new DoubleToVector[0];
            if (ss instanceof PlanarSources) {
                PlanarSources ps = (PlanarSources) ss;
                sf = ps.getSourceFunctions();
            }
            all.add(new FunctionsXYPlotConsoleAction("Structure Definition", "Direct Structure Definition" + p.getSerieTitle().toString(), change(str1.getDomain(), str1.getTestFunctions().arr(), sf), str1.getDomain(), p.getPreferredPath(), getPlotType(), getLibraries()));
        }
        if (containsType(YType.MODELED)) {
            if (p.getStructure2() != null) {
                DoubleToVector[] sf = new DoubleToVector[0];
                Sources ss = ((MomStructure) p.getStructure2()).getSources();
                if (ss instanceof PlanarSources) {
                    PlanarSources ps = (PlanarSources) ss;
                    sf = ps.getSourceFunctions();
                }
                all.add(new FunctionsXYPlotConsoleAction("Structure Definition", "Modeled Structure Definition" + p.getSerieTitle().toString(), change(str2.getDomain(), str2.getTestFunctions().arr(), sf), str2.getDomain(), p.getPreferredPath(), getPlotType(), getLibraries()));
            }
        }
//        mon.terminatem(getName());
        return all.iterator();
    }

    public DoubleToVector[] change(Domain domain, final DoubleToVector[] gf, final DoubleToVector[] sf) {
        CustomDDFunctionXYExpr defX = Maths.define("defX", (CustomDDFunctionXY) (x, y) -> {
            for (DoubleToVector f : sf) {
                if (f.getComponent(Axis.X).toDC().evalComplex(x, y).absdbl() > 0) {
                    return 2;
                }
            }
            for (DoubleToVector f : gf) {
                if (f.getComponent(Axis.X).toDC().evalComplex(x, y).absdbl() > 0) {
                    return 1;
                }
            }
            return 0;
        });
        CustomDDFunctionXYExpr defY = Maths.define("defY", (CustomDDFunctionXY) (x, y) -> {
            for (DoubleToVector f : sf) {
                if (f.getComponent(Axis.Y).toDC().evalComplex(x, y).absdbl() > 0) {
                    return 2;
                }
            }
            for (DoubleToVector f : gf) {
                if (f.getComponent(Axis.Y).toDC().evalComplex(x, y).absdbl() > 0) {
                    return 1;
                }
            }
            return 0;
        });
        return new DoubleToVector[]{Maths.vector(defX.mul(domain), defY.mul(domain)).toDV()};
    }
}
