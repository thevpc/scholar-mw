/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadruwaves.mom.str;

import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.TestFunctions;

/**
 * @author vpc
 */
public class MomConvergenceManager {
    MomStructure momStructure;

    public MomConvergenceManager(MomStructure momStructure) {
        this.momStructure = momStructure;
    }

//    public int computeTestFunctionsForReelFreqConvergence(double precision, double freq[], ProgressMonitor monitor) {
//        monitor = ProgressMonitors.nonnull(monitor);
//        momStructure.build();
//        double[] old = new double[freq.length];
//        for (int fi = 0; fi < freq.length; fi++) {
//            old[fi] = Double.NaN;
//        }
//        for (int essai_i = 1; essai_i < 100; essai_i++) {
//            double worstDiff = Double.NaN;
//            momStructure.setTestFunctionsCount(essai_i);
//            for (int fi = 0; fi < freq.length; fi++) {
//                momStructure.setFrequency(freq[fi]);
//                Matrix z = momStructure.inputImpedance().monitor(monitor).computeMatrix();
//                double d = z.norm1();
//                if (!Double.isNaN(old[fi])) {
//                    double dd = absdbl((old[fi] - d) / old[fi]);
//                    if (Double.isNaN(worstDiff) || dd > worstDiff) {
//                        worstDiff = dd;
//                    }
//                }
//                old[fi] = d;
//            }
//            System.out.println("precision : " + worstDiff);
//            if (worstDiff < precision) {
//
//                return essai_i;
//            }
//        }
//        throw new RuntimeException("precision non atteinte");
//    }


    /**
     * calculer la convergence du vecteur J
     *
     * @param maxFn maxFn
     * @param step  step
     * @param error error
     * @return fn at convergence
     */
    public int getConvergenceFn(int maxFn, int step, double error) {
        int oldMaxFn = momStructure.modeFunctions().getSize();
//        momStructure.applyModeFunctionsChanges(fn);

        TestFunctions gp = momStructure.testFunctions();
        ComplexMatrix sp = momStructure.createScalarProductCache(ProgressMonitors.none());
        ModeInfo[] n_pro = momStructure.modeFunctions().getPropagatingModes();
        ModeInfo[] n_eva = momStructure.getHintsManager().isHintRegularZnOperator() ? momStructure.getModes() : momStructure.modeFunctions().getVanishingModes();

        DoubleToVector[] _g = gp.arr();

        Complex[][] b = new Complex[_g.length][n_pro.length];
        for (int n = 0; n < n_pro.length; n++) {
            ComplexVector spc = sp.getColumn(n_pro[n].index);
            for (int p = 0; p < _g.length; p++) {
                b[p][n] = spc.get(p).neg();
            }
        }
        ComplexMatrix B = Maths.matrix(b);

        ComplexMatrix oldJ = null;

        Complex[][] a = new Complex[_g.length][_g.length];
        for (int p = 0; p < _g.length; p++) {
            for (int q = 0; q < _g.length; q++) {
                a[p][q] = Maths.CZERO;
            }
        }
        int n = 0;
        boolean converged = false;
        while (n < n_eva.length) {
            ComplexVector spc = sp.getColumn(n_eva[n].index);
            Complex zn = n_eva[n].impedance.impedanceValue();
            for (int s = 0; s < step && n < n_eva.length; s++) {
                for (int p = 0; p < _g.length; p++) {
                    for (int q = 0; q < _g.length; q++) {
                        Complex c = a[p][q];
                        Complex sp1 = spc.get(p);
                        Complex sp2 = spc.get(q).conj();
                        a[p][q] = c.plus(zn.mul(sp1).mul(sp2));
                    }
                }
                n++;
            }
            ComplexMatrix A = Maths.matrix(a);

            ComplexMatrix newJ = null;
            try {
                newJ = A.solve(B);
                if (oldJ != null) {
                    double err = newJ.getErrorMatrix(oldJ, Double.NaN).norm1();
                    converged = true;
//                    getLog().debug("getConvergenceFn [gp="+_g.length+";fn="+n+"]: err = " + err);
                    if (err < error) {
                        break;
                    }
                }
            } catch (Exception e) {
                momStructure.getLog().error("Exception @ getConvergenceFn [gp=" + _g.length + ";fn=" + n + "]: " + e);
            }
            oldJ = newJ;
        }
        if (!converged) {
            momStructure.getLog().error("No convergence for(gp=" + _g.length + ";fn=" + maxFn + " ; step=" + step + "; error=" + error + "]: ");
        } else {
            momStructure.getLog().debug("Convergence for(gp=" + _g.length + ";fn=" + maxFn + " ; step=" + step + "; error=" + error + "] = " + (n - 1 + n_pro.length));
        }
        momStructure.modeFunctions().setSize(oldMaxFn);
        return n - 1 + n_pro.length;
    }
}
