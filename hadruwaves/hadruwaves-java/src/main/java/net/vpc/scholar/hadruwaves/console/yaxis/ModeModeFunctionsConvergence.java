package net.vpc.scholar.hadruwaves.console.yaxis;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.plot.console.params.ParamSet;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;

public class ModeModeFunctionsConvergence extends PlotAxisSeriesComplex implements Cloneable {

    public ModeModeFunctionsConvergence(YType... type) {
        super("FnConvergence",type);
    }

    protected Complex computeComplex(MomStructure structure, ParamSet x, ConsoleActionParams p) {
        return Complex.valueOf(structure.getModeFunctionsCount());
    }
}
