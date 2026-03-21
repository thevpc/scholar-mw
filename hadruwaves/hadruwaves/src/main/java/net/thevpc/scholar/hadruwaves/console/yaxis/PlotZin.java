package net.thevpc.scholar.hadruwaves.console.yaxis;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.MutableComplex;
import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.console.params.ParamSet;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.mom.ModeFunctions;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruplot.PlotAxisSeriesMatrixValue;

public class PlotZin extends PlotAxisSeriesMatrixValue implements Cloneable {

    public PlotZin(YType... type) {
        super("Zin", type);
    }

    @Override
    protected Object[][] evalMatrixItems(ConsoleAwareObject structure, ParamSet x) {
        MomStructure s=(MomStructure) structure;
        return s.inputImpedance().monitor(this).evalMatrix().getArray();
    }

    protected Complex evalComplexSingleGpSingleModeSerial(MomStructure structure, ParamSet x) {
//        Complex z = structure.getZin().get(this.x, this.y);

        Complex z0 = Maths.CZERO;
        ModeFunctions fn = structure.modeFunctions();
        DoubleToVector g = structure.testFunctions().arr()[0];
        ModeInfo[] evan = structure.getHintsManager().isHintRegularZnOperator() ? fn.getModes() : fn.getVanishingModes();
        ModeInfo f0 = fn.getPropagatingModes()[0];
        for (ModeInfo fi : evan) {
            z0 = z0.plus(Maths.scalarProduct(g, fi.fn).toComplex().sqr().mul(fi.impedance.impedanceValue()));
        }
        Complex gf02 = Maths.scalarProduct(g, f0.fn).toComplex().sqr();
        z0 = z0.div(gf02);
        return z0;
    }

    protected Complex evalComplexSingleGpSingleModeParallel(MomStructure structure, ParamSet x) {
//        Complex z = structure.getZin().get(this.x, this.y);

        MutableComplex z0 = new MutableComplex();
        ModeFunctions fn = structure.modeFunctions();
        DoubleToVector g = structure.testFunctions().arr()[0];
        ModeInfo[] evan = structure.getHintsManager().isHintRegularZnOperator() ? fn.getModes() : fn.getVanishingModes();
        ModeInfo f0 = fn.getPropagatingModes()[0];
        for (ModeInfo fi : evan) {
            z0.add(Maths.scalarProduct(g, fi.fn).toComplex().sqr().mul(fi.impedance.admittanceValue()));
        }
        Complex gf02 = Maths.scalarProduct(g, f0.fn).toComplex().sqr();
        Complex z1 = gf02.div(z0.toComplex());
        return z1;
    }

    @Override
    public String toString() {
        return "Zin";
    }
}
