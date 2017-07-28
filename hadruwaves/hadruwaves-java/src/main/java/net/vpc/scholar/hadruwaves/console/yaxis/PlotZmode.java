package net.vpc.scholar.hadruwaves.console.yaxis;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.plot.console.params.ParamSet;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.ModeType;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;

public class PlotZmode extends PlotAxisSeriesComplex implements Cloneable {
    private int m;
    private int n;
    private ModeType mode;

    public PlotZmode(YType[] type, ModeType mode, int m, int n) {
        super("Zmode", type);
        this.m = m;
        this.n = n;
        this.mode = mode;
    }

    protected Complex computeComplex(MomStructure structure, ParamSet x, ConsoleActionParams p) {
        ModeInfo indexes = structure.getModeFunctions().getMode(mode, m, n);
        return indexes==null?Complex.NaN:indexes.impedance;
    }

    public String toString() {
        return "Zmode" + mode+(m+1) + "" + (n+1);
    }
    
}