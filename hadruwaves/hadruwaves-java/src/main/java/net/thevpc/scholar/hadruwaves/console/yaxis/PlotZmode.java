package net.thevpc.scholar.hadruwaves.console.yaxis;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadruplot.PlotAxisSeriesSingleValue;
import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.console.params.ParamSet;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.ModeType;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

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
        ModeInfo indexes = ((MomStructure)structure).modeFunctions().getMode(mode, m, n);
        return indexes==null?Complex.NaN :indexes.impedance.impedanceValue();
    }

    public String toString() {
        return "Zmode" + mode+(m+1) + "" + (n+1);
    }
    
}