package net.vpc.scholar.hadruwaves.console.yaxis;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadruplot.PlotAxisSeriesSingleValue;
import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruwaves.Physics;
import net.vpc.scholar.hadruplot.console.params.ParamSet;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadruplot.console.ConsoleActionParams;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

public class PlotSelfL extends PlotAxisSeriesSingleValue implements Cloneable {
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
    protected Complex computeComplex(ConsoleAwareObject structure, ParamSet x, ConsoleActionParams p) {
        MomStructure ss = (MomStructure) structure;
        Complex z = ss.self().monitor(this).computeMatrix().get(this.x, this.y);
        return z.div(Complex.I(Physics.omega(ss.getFrequency())));
    }
}
