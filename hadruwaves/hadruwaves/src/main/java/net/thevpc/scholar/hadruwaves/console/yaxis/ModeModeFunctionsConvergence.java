package net.thevpc.scholar.hadruwaves.console.yaxis;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadruplot.PlotAxisSeriesSingleValue;
import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.console.params.ParamSet;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

public class ModeModeFunctionsConvergence extends PlotAxisSeriesSingleValue implements Cloneable {

    public ModeModeFunctionsConvergence(YType... type) {
        super("FnConvergence",type);
    }

    protected Complex evalComplex(ConsoleAwareObject structure, ParamSet x, ConsoleActionParams p) {
        return Complex.of(((MomStructure)structure).modeFunctions().getSize());
    }
}
