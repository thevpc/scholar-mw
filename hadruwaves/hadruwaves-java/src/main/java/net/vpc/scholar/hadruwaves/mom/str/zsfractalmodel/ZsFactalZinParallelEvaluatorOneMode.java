package net.vpc.scholar.hadruwaves.mom.str.zsfractalmodel;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.str.ZinEvaluator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 août 2007 09:00:36
 */
public class ZsFactalZinParallelEvaluatorOneMode implements ZinEvaluator {

    public ComplexMatrix evaluate(MomStructure str, ProgressMonitor monitor) {
        MomStructureFractalZop strzop = (MomStructureFractalZop) str;
        ComplexMatrix ZinCond;
        try {
            ComplexMatrix a11 = A11(strzop);
            ComplexMatrix a = A(strzop);
            ComplexMatrix b = B(strzop);
            ComplexMatrix ZinPaire = a11.sub(a.mul(b.inv()).mul(a.transposeHermitian()));
//            CMatrix ZinPaire = a11.substract(a.multiply(b.inv()).multiply(a.transpose()));
            ZinCond = ZinPaire.div(2);
        } catch (Exception e) {
            str.getLog().error("Error Zin : " + e);
            str.wdebug("resolveZin", e);
            return Maths.NaNMatrix(1);
        }
        return ZinCond;
    }

    public ComplexMatrix A11(MomStructureFractalZop str) {
//        Zoperator[] zoperators = str.Zop();
//        CMatrix a11 = CMatrix.zerosMatrix(zoperators[0].getMatrix());
//        for (int i = 0; i < zoperators.length; i++) {
//            Zoperator zoperator = zoperators[i];
//            CFunctionVector2D[] fnPG = zoperator.getFn().fn();//fn du petit guide
//            ScalarProductCache c = new ScalarProductCache(str.getBaseFunctions());
//            for (int j = 1; j < fnPG.length; j++) {
//
//            }
//        }
//        ModeInfo[] n_propa = str.getBaseFunctions().getPropagatingModes();
//        ScalarProductCache spc2 = str.getTestModeScalarProducts();
//        for (int p = 0; p < g.length; p++) {
//            for (int q = p; q < g.length; q++) {
//                Complex c = Complex.ZERO;
//                for (int m = 0; m < op.length; m++) {
//                    for (int n = 0; n < op[m].length; n++) {
//                        //#@ ID=1@20070423
//                        //#@   OLD
////                                    Complex sp1 = spc2.fg(n, p);
////                                    Complex sp2 = spc2.gf(q, m);
////                                    c = c.add((zop[n_propa[m].index][n_propa[n].index]).multiply(sp1).multiply(sp2));
//                        //#@   NEW
////                                    Complex sp1 = spc2.fg(n_propa[n].index, p);
////                                    Complex sp2 = spc2.gf(q, n_propa[m].index);
//                        Complex sp1 = spc2.gf(p, n_propa[m].index);
//                        Complex sp2 = spc2.fg(n_propa[n].index, q);
//                        c = c.add((op[m][n]).multiply(sp1).multiply(sp2));
//                        //#@   END
//                    }
//                }
//                b[p][q] = b[p][q].add(c);
//            }
//        }
        return null;
    }

    public ComplexMatrix A(MomStructure str) {
        return null;
    }

    public ComplexMatrix B(MomStructure str) {
        return null;
    }

    @Override
    public String toString() {
        return getClass().getName();
    }

    @Override
    public String dump() {
        return getClass().getName();
    }

}