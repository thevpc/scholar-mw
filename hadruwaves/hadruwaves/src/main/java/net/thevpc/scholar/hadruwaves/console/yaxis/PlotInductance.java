package net.thevpc.scholar.hadruwaves.console.yaxis;

import static net.thevpc.scholar.hadrumaths.Complex.I;

import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.console.params.ParamSet;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruplot.PlotAxisSeriesMatrixValue;

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