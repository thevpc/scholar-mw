package net.thevpc.scholar.hadruwaves.console.yaxis;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadruplot.PlotAxisSeriesSingleValue;
import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.console.params.ParamSet;
import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

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