package net.vpc.scholar.hadruwaves.console.yaxis;

import net.vpc.scholar.hadrumaths.plot.console.params.ParamSet;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadruwaves.mom.console.yaxis.PlotAxisSeriesMatrixContent;

public class PlotSParameters extends PlotAxisSeriesMatrixContent implements Cloneable {

    public PlotSParameters(YType... type) {
        super("S", type);
    }

    @Override
    protected Matrix computeMatrixItems(MomStructure structure, ParamSet x) {
        return structure.sparameters().monitor(this).computeMatrix();
    }

}
