package net.vpc.scholar.hadruwaves.console.yaxis;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.ModeType;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.plot.console.params.ParamSet;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;

public class PlotGammaMode extends PlotAxisSeriesComplex implements Cloneable {
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

    protected Complex computeComplex(MomStructure structure, ParamSet x, ConsoleActionParams p) {
        ModeInfo fnIndexes = structure.getModeFunctions().getMode(mode, m, n);
        return fnIndexes==null?Complex.NaN: fnIndexes.firstBoxSpaceGamma;
    }

    public String toString() {
        return "Gamma" + mode+(m) + "" + (n);
    }
}