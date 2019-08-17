package net.vpc.scholar.hadruwaves.console.yaxis;

import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruplot.console.params.ParamSet;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruplot.PlotAxisSeriesMatrixValue;

public class PlotSParameters extends PlotAxisSeriesMatrixValue implements Cloneable {

    public PlotSParameters(YType... type) {
        super("S", type);
    }

    @Override
    protected Object[][] computeMatrixItems(ConsoleAwareObject structure, ParamSet x) {
        MomStructure s=(MomStructure) structure;
        return s.sparameters().monitor(this).computeMatrix().getArray();
    }

}
