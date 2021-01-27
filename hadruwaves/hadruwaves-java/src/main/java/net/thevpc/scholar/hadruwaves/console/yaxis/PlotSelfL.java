package net.thevpc.scholar.hadruwaves.console.yaxis;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadruplot.PlotAxisSeriesSingleValue;
import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruwaves.Physics;
import net.thevpc.scholar.hadruplot.console.params.ParamSet;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

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
    protected Complex evalComplex(ConsoleAwareObject structure, ParamSet x, ConsoleActionParams p) {
        MomStructure ss = (MomStructure) structure;
        Complex z = ss.self().monitor(this).evalMatrix().get(this.x, this.y);
        return z.div(Complex.I(Physics.omega(ss.getFrequency())));
    }
}
