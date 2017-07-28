package net.vpc.scholar.hadruwaves.console.yaxis;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadruwaves.Physics;
import net.vpc.scholar.hadrumaths.plot.console.params.ParamSet;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;

public class PlotSelfL extends PlotAxisSeriesComplex implements Cloneable {
    private int x;
    private int y;

    public PlotSelfL(YType... type) {
        this(type, 1, 1);
    }

    public PlotSelfL(YType[] type, int x, int y) {
        super("Capacity(Z'"+(x)+""+(y)+")",type);
        this.x = x - 1;
        this.y = y - 1;
    }

    @Override
    protected Complex computeComplex(MomStructure structure, ParamSet x, ConsoleActionParams p) {
        Complex z = structure.self().monitor(this).computeMatrix().get(this.x, this.y);
        return z.div(Complex.I(Physics.omega(structure.getFrequency())));
    }
}
