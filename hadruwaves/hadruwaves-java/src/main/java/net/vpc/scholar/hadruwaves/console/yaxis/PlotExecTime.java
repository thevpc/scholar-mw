package net.vpc.scholar.hadruwaves.console.yaxis;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.plot.console.params.ParamSet;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;

public class PlotExecTime extends PlotAxisSeriesComplex implements Cloneable {
    private String cacheName;
    public PlotExecTime(String cacheName, YType... type) {
        super("Time("+cacheName+")", type);
        this.cacheName=cacheName;
    }

    protected Complex computeComplex(MomStructure structure, ParamSet x, ConsoleActionParams p) {
        long time = structure.getExecutionTime(cacheName);
        return Complex.valueOf(time/1000.0);
    }

    public String toString() {
        return getName();
    }
}