package net.thevpc.scholar.hadruwavesstudio.standalone.v1.buildactions;

import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.PlotConfigData;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.scholar.hadruwaves.mom.util.MomStrHelper;

/**
 * User: vpc
 * Date: 11 févr. 2005
 * Time: 00:33:09
 */
public class BuildSourceBaseAction extends BuildCCubeVectorAction {
    int progress_percent_build = 0;
    //private Jxy jxy;
    int progress_percent_build_max;


    double[] x;
    double[] y;

    public BuildSourceBaseAction(MomStrHelper helper, PlotConfigData config, double[] x, double[] y) {
        super(helper,config);
        this.x = x;
        this.y = y;
        progress_percent_build_max = 0;
//        int max_mTE = jxy.dielectric.getMaxTEM();
//        int max_nTE = jxy.dielectric.getMaxTEN();
//        int max_mTM = jxy.dielectric.getMaxTMM();
//        int max_nTM = jxy.dielectric.getMaxTMN();
//        for (int metalIndex = 0; metalIndex < jxy.metal.length; metalIndex++) {
//            int max_p = jxy.metal[metalIndex].getFunctionMax();
//            progress_percent_build_max += max_p * (max_mTE * max_nTE + max_mTM * max_nTM);
//        }
    }


    public Complex[][] go0() {
        return (Complex[][])go();
    }

    public VDiscrete run(MomStrHelper helper) {
        return null;
    }

//    public Object run() {
//        final BoxArea localDielectric = jxy.dielectric;
//        final Area[] localSource = jxy.source;
//        Chronometer chronometer = new Chronometer();
//        chronometer.start();
//        int axis = 0;
//        String axisString = (axis == 0) ? "X" : "Y";
//        Log.trace("[Source (O" + axisString + ") on Base Functions] Started...");
//        //System.out.println("*--------------------------------*");
//        Complex[][] cvalues = CArrays.fill(new Complex[y.length][x.length], Complex.CZERO);
//        int max_mTE = localDielectric.getMaxTEM();
//        int max_nTE = localDielectric.getMaxTEN();
//        int max_mTM = localDielectric.getMaxTMM();
//        int max_nTM = localDielectric.getMaxTMN();
//        for (int s = 0; s < localSource.length; s++) {
//            int max_p = localSource[s].getFunctionMax();
//            for (int p = 0; p < max_p; p++) {
//                DFunctionVector2D sourceFct = localSource[s].getFunction(p);
//                for (int m = 0; m < max_mTE; m++) {
//                    for (int n = 0; n < max_nTE; n++) {
//                        DFunctionVector2D fctTE = localDielectric.fctTE(m, n);
//                        double ps = Math2.scalarProduct(sourceFct, fctTE);
//                        BuildUtils.updateMatrixAddFxyMultCoeff(fctTE.F[axis], cvalues, x, y, new Complex(ps));
//                        progress_percent_build++;
//                    }
//                }
//                for (int m = 0; m < max_mTM; m++) {
//                    for (int n = 0; n < max_nTM; n++) {
//                        DFunctionVector2D fctTM = localDielectric.fctTM(m, n);
//                        double ps = Math2.scalarProduct(sourceFct, fctTM);
//                        BuildUtils.updateMatrixAddFxyMultCoeff(fctTM.F[axis], cvalues, x, y, new Complex(ps));
//                        progress_percent_build++;
//                    }
//                }
//            }
//        }
//        chronometer.stop();
//        Log.trace("[Source (O" + axisString + ") on Base Functions] Finished... " + chronometer);
//        jxy.logStatistics(getClass().getName() + ":O" + axisString, chronometer);
//        return cvalues;
//        return null;
//        }
}
