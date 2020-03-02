package net.vpc.scholar.hadruwaves.console.yaxis;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadruplot.PlotAxisSeriesSingleValue;
import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.ModeType;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadruplot.console.params.ParamSet;
import net.vpc.scholar.hadruplot.console.ConsoleActionParams;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

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
        ModeInfo fnIndexes = ((MomStructure)structure).getModeFunctions().getMode(mode, m, n);
        return fnIndexes==null?Complex.NaN : fnIndexes.firstBoxSpaceGamma;
    }

    public String toString() {
        return "Gamma" + mode+(m) + "" + (n);
    }
}