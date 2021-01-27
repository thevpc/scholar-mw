package net.thevpc.scholar.hadruwaves.console.yaxis;

import static net.thevpc.scholar.hadrumaths.Complex.I;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.console.params.ParamSet;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruplot.PlotAxisSeriesMatrixValue;

public class PlotCapacity extends PlotAxisSeriesMatrixValue implements Cloneable {

    public PlotCapacity(YType... type) {
        super("Capacity", type);
    }

    @Override
    protected Object[][] evalMatrixItems(ConsoleAwareObject structure, ParamSet x) {
        MomStructure s=(MomStructure) structure;
        ComplexMatrix cMatrix = s.capacity().monitor(this).evalMatrix();
        System.out.println("["+s.getName()+"]["+x+"] capa = " + cMatrix);
        return cMatrix.getArray();
    }

}
