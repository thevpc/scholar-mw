package net.vpc.scholar.hadruwaves.console.yaxis;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.plot.console.params.ParamSet;
import net.vpc.scholar.hadruwaves.ModeType;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;

public class PlotYmode extends PlotAxisSeriesComplex implements Cloneable {
    private int m;
    private int n;
    private ModeType mode;

    public PlotYmode(YType[] type, ModeType mode, int m, int n) {
        super("Ymode", type);
        this.m = m;
        this.n = n;
        this.mode = mode;
    }

    protected Complex computeComplex(MomStructure structure, ParamSet x, ConsoleActionParams p) {
        return structure.getModeFunctions().getMode(mode, m, n).impedance.inv();
    }

    public String toString() {
        return "Ymode" + mode + (m) + "" + (n);
    }

}