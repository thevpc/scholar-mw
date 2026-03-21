/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadruwaves.mom.str;

import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.nuts.text.NMsg;
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
                momStructure.log().log(NMsg.ofC("Exception @ getConvergenceFn [gp=" + _g.length + ";fn=" + n + "]: " + e).asError());
            }
            oldJ = newJ;
        }
        if (!converged) {
            momStructure.log().log(NMsg.ofC("No convergence for(gp=" + _g.length + ";fn=" + maxFn + " ; step=" + step + "; error=" + error + "]: ").asError());
        } else {
            momStructure.log().log(NMsg.ofC("Convergence for(gp=" + _g.length + ";fn=" + maxFn + " ; step=" + step + "; error=" + error + "] = " + (n - 1 + n_pro.length)).asDebug());
        }
        momStructure.modeFunctions().setSize(oldMaxFn);
        return n - 1 + n_pro.length;
    }
}
