package net.thevpc.scholar.hadruwaves.console.yaxis;

import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.console.params.ParamSet;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruplot.PlotAxisSeriesMatrixValue;

public class PlotSParameters extends PlotAxisSeriesMatrixValue implements Cloneable {

    public PlotSParameters(YType... type) {
        super("S", type);
    }

    @Override
    protected Object[][] evalMatrixItems(ConsoleAwareObject structure, ParamSet x) {
        MomStructure s=(MomStructure) structure;
        return s.sparameters().monitor(this).evalMatrix().getArray();
    }

}
