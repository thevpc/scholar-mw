package net.vpc.scholar.hadruwaves.console.yaxis;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadruplot.PlotAxisSeriesSingleValue;
import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruplot.console.params.ParamSet;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.ModeType;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadruplot.console.ConsoleActionParams;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

public class PlotZmode extends PlotAxisSeriesSingleValue implements Cloneable {
    private int m;
    private int n;
    private ModeType mode;

    public PlotZmode(YType[] type, ModeType mode, int m, int n) {
        super("Zmode", type);
        this.m = m;
        this.n = n;
        this.mode = mode;
    }

    protected Complex evalComplex(ConsoleAwareObject structure, ParamSet x, ConsoleActionParams p) {
        ModeInfo indexes = ((MomStructure)structure).getModeFunctions().getMode(mode, m, n);
        return indexes==null?Complex.NaN :indexes.impedance.impedanceValue();
    }

    public String toString() {
        return "Zmode" + mode+(m+1) + "" + (n+1);
    }
    
}