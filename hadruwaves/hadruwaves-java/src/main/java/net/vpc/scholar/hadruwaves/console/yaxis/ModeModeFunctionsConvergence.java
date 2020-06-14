package net.vpc.scholar.hadruwaves.console.yaxis;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadruplot.PlotAxisSeriesSingleValue;
import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruplot.console.params.ParamSet;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadruplot.console.ConsoleActionParams;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

public class ModeModeFunctionsConvergence extends PlotAxisSeriesSingleValue implements Cloneable {

    public ModeModeFunctionsConvergence(YType... type) {
        super("FnConvergence",type);
    }

    protected Complex evalComplex(ConsoleAwareObject structure, ParamSet x, ConsoleActionParams p) {
        return Complex.of(((MomStructure)structure).modeFunctions().getSize());
    }
}
