package net.vpc.scholar.hadruwaves.mom.console.yaxis;

import net.vpc.common.util.mon.ProgressMonitor;
import net.vpc.common.util.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.BooleanMarker;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.plot.PlotType;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleAction;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;
import net.vpc.scholar.hadrumaths.plot.console.FunctionsXYPlotConsoleAction;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.PlotAxisCustom;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.symbolic.AbstractDoubleToDouble;
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
        ProgressMonitor mon = ProgressMonitorFactory.enhance(this);
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
            all.add(new FunctionsXYPlotConsoleAction("Structure Definition", "Direct Structure Definition" + p.getSerieTitle().toString(), change(str1.getDomain(), str1.getTestFunctions().arr(), sf), str1.getDomain(), p.getPreferredPath(), getPlotType(), getPreferredLibraries()));
        }
        if (containsType(YType.MODELED)) {
            if (p.getStructure2() != null) {
                DoubleToVector[] sf = new DoubleToVector[0];
                Sources ss = ((MomStructure) p.getStructure2()).getSources();
                if (ss instanceof PlanarSources) {
                    PlanarSources ps = (PlanarSources) ss;
                    sf = ps.getSourceFunctions();
                }
                all.add(new FunctionsXYPlotConsoleAction("Structure Definition", "Modeled Structure Definition" + p.getSerieTitle().toString(), change(str2.getDomain(), str2.getTestFunctions().arr(), sf), str2.getDomain(), p.getPreferredPath(), getPlotType(), getPreferredLibraries()));
            }
        }
//        mon.terminatem(getName());
        return all.iterator();
    }

    public DoubleToVector[] change(Domain domain, final DoubleToVector[] gf, final DoubleToVector[] sf) {
        DoubleToVector m = Maths.vector(
                new AbstractDoubleToDouble(domain) {

                    @Override
                    public double computeDouble0(double x, BooleanMarker defined) {
                        throw new IllegalArgumentException("Missing y");
                    }

                    @Override
                    public double computeDouble0(double x, double y, double z, BooleanMarker defined) {
                        return computeDouble(x, y);
                    }

                    @Override
                    public double computeDouble0(double x, double y, BooleanMarker defined) {
                        for (DoubleToVector f : sf) {
                            if (f.getComponent(Axis.X).toDC().computeComplex(x, y).absdbl() > 0) {
                                return 2;
                            }
                        }
                        for (DoubleToVector f : gf) {
                            if (f.getComponent(Axis.X).toDC().computeComplex(x, y).absdbl() > 0) {
                                return 1;
                            }
                        }
                        return 0;
                    }

                    @Override
                    public boolean equals(Object o) {
                        return o == this;
                    }
                },
                new AbstractDoubleToDouble(domain) {

                    @Override
                    public double computeDouble0(double x, BooleanMarker defined) {
                        throw new IllegalArgumentException("Missing y");
                    }

                    @Override
                    public double computeDouble0(double x, double y, double z, BooleanMarker defined) {
                        return computeDouble(x, y);
                    }

                    @Override
                    public double computeDouble0(double x, double y, BooleanMarker defined) {
                        for (DoubleToVector f : sf) {
                            if (f.getComponent(Axis.Y).toDC().computeComplex(x, y) != null) {
                                return 10;
                            }
                        }
                        for (DoubleToVector f : gf) {
                            if (f.getComponent(Axis.Y).toDC().computeComplex(x, y) != null) {
                                return 1;
                            }
                        }
                        return 0;
                    }

                    @Override
                    public int getDomainDimension() {
                        return 2;
                    }

                    public boolean equals(Object o) {
                        return o == this;
                    }
                });
        return new DoubleToVector[]{m};
    }
}
