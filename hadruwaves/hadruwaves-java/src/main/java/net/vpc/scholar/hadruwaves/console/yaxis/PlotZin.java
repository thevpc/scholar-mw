package net.vpc.scholar.hadruwaves.console.yaxis;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.plot.console.params.ParamSet;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.console.yaxis.PlotAxisSeriesMatrixContent;

public class PlotZin extends PlotAxisSeriesMatrixContent implements Cloneable {

    public PlotZin(YType... type) {
        super("Zin", type);
    }

    @Override
    protected Matrix computeMatrixItems(MomStructure structure, ParamSet x) {
        return structure.inputImpedance().monitor(this).computeMatrix();
    }

    protected Complex computeComplexSingleGpSingleModeSerial(MomStructure structure, ParamSet x) {
//        Complex z = structure.getZin().get(this.x, this.y);

        Complex z0 = Complex.ZERO;
        ModeFunctions fn = structure.getModeFunctions();
        DoubleToVector g = structure.getTestFunctions().arr()[0];
        ModeInfo[] evan = structure.getHintsManager().isHintRegularZnOperator() ? fn.getModes(null,structure.getCurrentCache(true)) : fn.getVanishingModes();
        ModeInfo f0 = fn.getPropagatingModes()[0];
//        System.out.println("------------------------------------------------------------------------");
//        System.out.println("freq="+structure.getF());
        for (ModeInfo fi : evan) {
//            System.out.println(fi.mode+"[m"+fi.m+",n"+fi.n+"] ; "+ "GAMMAmn = " + fi.firstBoxSpaceGamma+"  : Zmn = " + fi.zn);
            z0 = z0.add(Maths.scalarProduct(g, fi.fn).square().mul(fi.impedance));
        }
        Complex gf02 = Maths.scalarProduct(g, f0.fn).square();
        z0 = z0.div(gf02);
//        System.out.println("z="+z0);
//        System.out.println("------------------------------------------------------------------------");
        return z0;
//        System.out.println("PlotZin["+x.getValue()+"] : ");
//        System.out.println("\t\t: z = " + z);
        //return z;
    }

    protected Complex computeComplexSingleGpSingleModeParallel(MomStructure structure, ParamSet x) {
//        Complex z = structure.getZin().get(this.x, this.y);

        Complex z0 = Complex.ZERO;
        ModeFunctions fn = structure.getModeFunctions();
        DoubleToVector g = structure.getTestFunctions().arr()[0];
        ModeInfo[] evan = structure.getHintsManager().isHintRegularZnOperator() ? fn.getModes(null,structure.getCurrentCache(true)) : fn.getVanishingModes();
        ModeInfo f0 = fn.getPropagatingModes()[0];
        System.out.println("------------------------------------------------------------------------");
        System.out.println("freq=" + structure.getFrequency());
        for (ModeInfo fi : evan) {
            System.out.println(fi.mode.mtype + "[m" + fi.mode.m + ",n" + fi.mode.n + "] ; " + "GAMMAmn = " + fi.firstBoxSpaceGamma + "/" + fi.secondBoxSpaceGamma + "  : Zmn = " + fi.impedance + "  : Ymn = " + fi.impedance.inv());
            z0 = z0.add(Maths.scalarProduct(g, fi.fn).square().mul(fi.impedance.inv()));
        }
        Complex gf02 = Maths.scalarProduct(g, f0.fn).square();
        z0 = gf02.div(z0);
        System.out.println("z=" + z0);
        System.out.println("------------------------------------------------------------------------");
        return z0;
//        System.out.println("PlotZin["+x.getValue()+"] : ");
//        System.out.println("\t\t: z = " + z);
        //return z;
    }

    @Override
    public String toString() {
        return "Zin";
    }
}
