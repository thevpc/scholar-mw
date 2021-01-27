package net.thevpc.scholar.hadruwaves.mom.console.yaxis;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.CustomDDFunctionXY;
import net.thevpc.scholar.hadrumaths.symbolic.CustomDDFunctionXYExpr;
import net.thevpc.scholar.hadruplot.PlotType;
import net.thevpc.scholar.hadruplot.console.ConsoleAction;
import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.scholar.hadrumaths.plot.FunctionsXYPlotConsoleAction;
import net.thevpc.scholar.hadruplot.console.yaxis.PlotAxisCustom;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.sources.PlanarSources;
import net.thevpc.scholar.hadruwaves.mom.sources.Sources;

import java.util.ArrayList;
import java.util.Iterator;
import net.thevpc.scholar.hadruplot.LibraryPlotType;

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
            all.add(new FunctionsXYPlotConsoleAction("Structure Definition", "Direct Structure Definition" + p.getSerieTitle().toString(), change(str1.getDomain(), str1.testFunctions().arr(), sf), str1.getDomain(), p.getPreferredPath(), new LibraryPlotType(getPlotType()), getLibraries()));
        }
        if (containsType(YType.MODELED)) {
            if (p.getStructure2() != null) {
                DoubleToVector[] sf = new DoubleToVector[0];
                Sources ss = ((MomStructure) p.getStructure2()).getSources();
                if (ss instanceof PlanarSources) {
                    PlanarSources ps = (PlanarSources) ss;
                    sf = ps.getSourceFunctions();
                }
                all.add(new FunctionsXYPlotConsoleAction("Structure Definition", "Modeled Structure Definition" + p.getSerieTitle().toString(), change(str2.getDomain(), str2.testFunctions().arr(), sf), str2.getDomain(), p.getPreferredPath(), new LibraryPlotType(getPlotType()), getLibraries()));
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
