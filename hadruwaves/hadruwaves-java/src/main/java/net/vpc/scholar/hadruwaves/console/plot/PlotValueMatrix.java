package net.vpc.scholar.hadruwaves.console.plot;

import net.vpc.scholar.hadrumaths.plot.PlotType;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleAwareObject;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.NamedMatrix;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadruwaves.console.PlotEvaluator;
import net.vpc.scholar.hadruwaves.mom.console.yaxis.PlotAxisSeries;

public class PlotValueMatrix extends PlotAxisSeries implements Cloneable {
    private PlotEvaluator<NamedMatrix> value;
    public PlotValueMatrix(String name, PlotEvaluator<NamedMatrix> value, PlotType plotType, YType... type) {
        super(name, type, plotType);
        this.value=value;
    }

   @Override
    protected NamedMatrix computeValue(ConsoleAwareObject structure, ComputationMonitor monitor, ConsoleActionParams p) {
        return value.computeValue(structure,monitor,p);
    }

}
