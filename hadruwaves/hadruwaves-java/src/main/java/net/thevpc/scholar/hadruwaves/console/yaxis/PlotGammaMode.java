package net.thevpc.scholar.hadruwaves.console.yaxis;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadruplot.PlotAxisSeriesSingleValue;
import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.ModeType;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadruplot.console.params.ParamSet;
import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

public class PlotGammaMode extends PlotAxisSeriesSingleValue implements Cloneable {
    private int m;
    private int n;
    private ModeType mode;

    public PlotGammaMode(YType[] type, ModeInfo mode) {
        this(type, mode.mode.mtype,mode.mode.m,mode.mode.n);
    }
    public PlotGammaMode(YType[] type, ModeType mode, int m, int n) {
        super("Gamma", type);
        this.m = m;
        this.n = n;
        this.mode = mode;
    }

    protected Complex evalComplex(ConsoleAwareObject structure, ParamSet x, ConsoleActionParams p) {
        ModeInfo fnIndexes = ((MomStructure)structure).modeFunctions().getMode(mode, m, n);
        return fnIndexes==null?Complex.NaN : fnIndexes.firstBoxSpaceGamma;
    }

    public String toString() {
        return "Gamma" + mode+(m) + "" + (n);
    }
}