package net.vpc.scholar.hadruwaves.console.yaxis;

import static net.vpc.scholar.hadrumaths.Complex.I;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.plot.console.params.ParamSet;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadruwaves.mom.console.yaxis.PlotAxisSeriesMatrixContent;

public class PlotInductance extends PlotAxisSeriesMatrixContent implements Cloneable {

    public PlotInductance(YType... type) {
        super("Inductance", type);
    }

    @Override
    protected Matrix computeMatrixItems(MomStructure structure, ParamSet x) {
        return structure.self().monitor(this).computeMatrix();
    }

}