package net.vpc.scholar.hadruwaves.console.yaxis;

import static net.vpc.scholar.hadrumaths.Complex.I;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.plot.console.params.ParamSet;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadruwaves.mom.console.yaxis.PlotAxisSeriesMatrixContent;

public class PlotCapacity extends PlotAxisSeriesMatrixContent implements Cloneable {

    public PlotCapacity(YType... type) {
        super("Capacity", type);
    }

    @Override
    protected Matrix computeMatrixItems(MomStructure structure, ParamSet x) {
        Matrix cMatrix = structure.capacity().monitor(this).computeMatrix();
        System.out.println("["+structure.getName()+"]["+x+"] capa = " + cMatrix);
        return cMatrix;
    }

}
