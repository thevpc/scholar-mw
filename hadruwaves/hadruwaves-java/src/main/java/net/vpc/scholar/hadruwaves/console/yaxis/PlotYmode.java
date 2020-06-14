package net.vpc.scholar.hadruwaves.console.yaxis;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadruplot.PlotAxisSeriesSingleValue;
import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruplot.console.params.ParamSet;
import net.vpc.scholar.hadruwaves.ModeType;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadruplot.console.ConsoleActionParams;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

public class PlotYmode extends PlotAxisSeriesSingleValue implements Cloneable {
    private int m;
    private int n;
    private ModeType mode;

    public PlotYmode(YType[] type, ModeType mode, int m, int n) {
        super("Ymode", type);
        this.m = m;
        this.n = n;
        this.mode = mode;
    }

    protected Complex evalComplex(ConsoleAwareObject structure, ParamSet x, ConsoleActionParams p) {
        return ((MomStructure)structure).modeFunctions().getMode(mode, m, n).impedance.admittanceValue();
    }

    public String toString() {
        return "Ymode" + mode + (m) + "" + (n);
    }

}