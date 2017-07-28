/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadruwaves.mom.str;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductCache;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.TestFunctions;
import net.vpc.scholar.hadruwaves.ModeInfo;

import static java.lang.Math.abs;

/**
 *
 * @author vpc
 */
public class MomConvergenceManager {
    MomStructure momStructure;

    public MomConvergenceManager(MomStructure momStructure) {
        this.momStructure = momStructure;
    }

//    public int computeTestFunctionsForReelFreqConvergence(double precision, double freq[], ComputationMonitor monitor) {
//        monitor = ComputationMonitorFactory.enhance(monitor);
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
        ModeFunctions fn = momStructure.getModeFunctionsTemplate();
//        momStructure.applyModeFunctionsChanges(fn);
        fn.setSize(maxFn);
        TestFunctions gp = momStructure.getGpTestFunctionsTemplate();
        gp.setStructure(momStructure);
        ScalarProductCache sp = momStructure.createScalarProductCache(fn, gp, ComputationMonitorFactory.none());
        ModeInfo[] n_pro = fn.getPropagatingModes();
        ModeInfo[] n_eva = momStructure.getHintsManager().isHintRegularZnOperator() ? momStructure.getModes() : fn.getVanishingModes();

        DoubleToVector[] _g = gp.arr();

        Complex[][] b = new Complex[_g.length][n_pro.length];
        for (int n = 0; n < n_pro.length; n++) {
            Vector spc = sp.getColumn(n_pro[n].index);
            for (int p = 0; p < _g.length; p++) {
                b[p][n] = spc.get(p).neg();
            }
        }
        Matrix B = Maths.matrix(b);

        Matrix oldJ = null;

        Complex[][] a = new Complex[_g.length][_g.length];
        for (int p = 0; p < _g.length; p++) {
            for (int q = 0; q < _g.length; q++) {
                a[p][q] = Complex.ZERO;
            }
        }
        int n = 0;
        boolean converged = false;
        while (n < n_eva.length) {
            Vector spc = sp.getColumn(n_eva[n].index);
            Complex zn = n_eva[n].impedance;
            for (int s = 0; s < step && n < n_eva.length; s++) {
                for (int p = 0; p < _g.length; p++) {
                    for (int q = 0; q < _g.length; q++) {
                        Complex c = a[p][q];
                        Complex sp1 = spc.get(p);
                        Complex sp2 = spc.get(q).conj();
                        c = c.add(zn.mul(sp1).mul(sp2));
                        a[p][q] = c;
                    }
                }
                n++;
            }
            Matrix A = Maths.matrix(a);

            Matrix newJ = null;
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
        return n - 1 + n_pro.length;
    }
}
