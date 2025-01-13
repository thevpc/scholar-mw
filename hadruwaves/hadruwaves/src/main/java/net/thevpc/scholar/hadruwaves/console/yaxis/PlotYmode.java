package net.thevpc.scholar.hadruwaves.console.yaxis;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadruplot.PlotAxisSeriesSingleValue;
import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.console.params.ParamSet;
import net.thevpc.scholar.hadruwaves.ModeType;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

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