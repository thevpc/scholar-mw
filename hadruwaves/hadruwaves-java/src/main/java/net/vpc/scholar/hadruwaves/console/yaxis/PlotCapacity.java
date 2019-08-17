package net.vpc.scholar.hadruwaves.console.yaxis;

import static net.vpc.scholar.hadrumaths.Complex.I;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruplot.console.params.ParamSet;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruplot.PlotAxisSeriesMatrixValue;

public class PlotCapacity extends PlotAxisSeriesMatrixValue implements Cloneable {

    public PlotCapacity(YType... type) {
        super("Capacity", type);
    }

    @Override
    protected Object[][] computeMatrixItems(ConsoleAwareObject structure, ParamSet x) {
        MomStructure s=(MomStructure) structure;
        Matrix cMatrix = s.capacity().monitor(this).computeMatrix();
        System.out.println("["+s.getName()+"]["+x+"] capa = " + cMatrix);
        return cMatrix.getArray();
    }

}
