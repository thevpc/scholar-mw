package net.vpc.scholar.hadruwaves.console.yaxis;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadruplot.PlotAxisSeriesSingleValue;
import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruplot.console.params.ParamSet;
import net.vpc.scholar.hadruplot.console.ConsoleActionParams;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

public class PlotExecTime extends PlotAxisSeriesSingleValue implements Cloneable {
    private String cacheName;
    public PlotExecTime(String cacheName, YType... type) {
        super("Time("+cacheName+")", type);
        this.cacheName=cacheName;
    }

    protected Complex evalComplex(ConsoleAwareObject structure, ParamSet x, ConsoleActionParams p) {
        long time = ((MomStructure)structure).getExecutionTime(cacheName);
        return Complex.of(time/1000.0);
    }

    public String toString() {
        return getName();
    }
}