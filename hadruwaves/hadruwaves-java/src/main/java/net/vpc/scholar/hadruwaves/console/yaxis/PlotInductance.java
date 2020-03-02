package net.vpc.scholar.hadruwaves.console.yaxis;

import static net.vpc.scholar.hadrumaths.Complex.I;

import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruplot.console.params.ParamSet;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruplot.PlotAxisSeriesMatrixValue;

public class PlotInductance extends PlotAxisSeriesMatrixValue implements Cloneable {

    public PlotInductance(YType... type) {
        super("Inductance", type);
    }

    @Override
    protected Object[][] evalMatrixItems(ConsoleAwareObject structure, ParamSet x) {
        MomStructure s=(MomStructure) structure;
        return s.self().monitor(this).evalMatrix().getArray();
    }

}