package net.thevpc.scholar.hadruwaves.mom.str.zsmodel;

//package net.thevpc.scholar.tmwlib.mom.str.zsmodel;
//
//import net.thevpc.scholar.math.CMatrix;
//import net.thevpc.scholar.math.Complex;
//import net.thevpc.scholar.math.Math2;
//import net.thevpc.scholar.math.functions.cfxy.CFunctionVector2D;
//import net.thevpc.scholar.tmwlib.mom.MomStructure;
//import net.thevpc.scholar.tmwlib.mom.util.ScalarProductCache;
//import net.thevpc.scholar.tmwlib.mom.fnbase.FnBaseFunctions;
//import net.thevpc.scholar.tmwlib.mom.fnbase.ModeInfo;
//import net.thevpc.scholar.tmwlib.mom.gptest.GpTestFunctions;
//import net.thevpc.scholar.tmwlib.mom.str.AMatrixBuilder;
//
///**
// * @author Taha Ben Salah (taha.bensalah@gmail.com)
// * @creationtime 24 mai 2007 21:55:23
// */
//public class ZsAMatrixWaveguideSerialBuilder implements AMatrixBuilder {
//    public CMatrix build(MomStructure str) {
//        MethodeModeleZopStructure2D str2 = (MethodeModeleZopStructure2D) str;
//        GpTestFunctions gpTestFunctions = str.getGpTestFunctions();
//        CFunctionVector2D[] g = gpTestFunctions.gp();
//        Complex[][] b = new Complex[g.length][g.length];
//        FnBaseFunctions fn = str.getBaseFunctions();
//        ModeInfo[] n_evan = str.isParameter(MomStructure.HINT_REGULAR_ZN_OPERATOR) ? fn.getModes() : fn.getVanishingModes();
//        ScalarProductCache sp = str.getTestModeScalarProducts();
//        for (int p = 0; p < g.length; p++) {
//            for (int q = 0; q < g.length; q++) {
//                Complex c = Complex.CZERO;
//                for (ModeInfo aN_evan : n_evan) {
//                    Complex zn = aN_evan.zn;
//                    Complex sp1 = sp.gf(p, aN_evan.index);
//                    Complex sp2 = sp.fg(aN_evan.index, q);
//                    c = c.add(zn.multiply(sp1).multiply(sp2));
//                }
//                b[p][q] = c;
//            }
//        }
//
//        Zoperator[] ZopValues = new Zoperator[]{str2.ZopLeft(), str2.ZopRight()};
//        for (Zoperator zopValue : ZopValues) {
//            Complex[][] zop = zopValue == null ? null : zopValue.getMatrix().getArray();
//            if (zop != null) {//zop==null si k==1
//                ModeInfo[] n_propa = zopValue.getFn().getPropagatingModes();
//                for (int p = 0; p < g.length; p++) {
//                    for (int q = 0; q < g.length; q++) {
//                        Complex c = Complex.CZERO;
//                        for (int m = 0; m < zop.length; m++) {
//                            for (int n = 0; n < zop[m].length; n++) {
//                                Complex sp1 = Math2.scalarProduct(n_propa[n].fn, g[p]);
//                                Complex sp2 = Math2.scalarProduct(n_propa[m].fn, g[q]).conj();
//                                c = c.add((zop[n_propa[m].index][n_propa[n].index]).multiply(sp1).multiply(sp2));
//                            }
//                        }
//                        b[p][q] = b[p][q].add(c);
//                    }
//                }
//            }
//        }
//
//        return new CMatrix(b);
//    }
//}