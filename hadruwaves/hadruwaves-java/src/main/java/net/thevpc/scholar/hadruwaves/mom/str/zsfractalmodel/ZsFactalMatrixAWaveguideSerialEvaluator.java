package net.thevpc.scholar.hadruwaves.mom.str.zsfractalmodel;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.mom.ModeFunctions;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.TestFunctions;
import net.thevpc.scholar.hadruwaves.mom.str.MatrixAEvaluator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 21:55:23
 */
public class ZsFactalMatrixAWaveguideSerialEvaluator implements MatrixAEvaluator {
    @Override
    public ComplexMatrix evaluate(MomStructure str, ProgressMonitor monitor) {
        MomStructureFractalZop str2 = (MomStructureFractalZop) str;
        TestFunctions gpTestFunctions = str.testFunctions();
        DoubleToVector[] g = gpTestFunctions.arr();
        Complex[][] b = new Complex[g.length][g.length];
        ModeFunctions fn = str.modeFunctions();
        str.getModes();//just to load it
        ModeInfo[] n_evan = str.getHintsManager().isHintRegularZnOperator() ? str.getModes() : fn.getVanishingModes();
        ComplexMatrix sp = str.getTestModeScalarProducts(ProgressMonitors.none());
        boolean complex = fn.isComplex() || gpTestFunctions.isComplex();
        boolean symMatrix = !complex;
        boolean hintUseOldZsStyle = str2.isHintUseOldZsStyle();
        if (hintUseOldZsStyle && fn.getPropagatingModes().length > 1) {
            throw new IllegalArgumentException("HintUseOldZsStyle is not allowed if getPropagatingModes>1 (value is " + fn.getPropagatingModes().length + ")");
        }
        if (symMatrix) {
            for (int p = 0; p < g.length; p++) {
                ComplexVector spp = sp.getRow(p);
                for (int q = p; q < g.length; q++) {
                    ComplexVector spq = sp.getRow(q);
                    Complex c = Maths.CZERO;
                    for (ModeInfo n : n_evan) {
                        Complex zn = n.impedance.impedanceValue();
                        c = c.plus(zn.mul(spp.get(n.index)).mul(spq.get(n.index).conj()));
                    }
                    b[p][q] = c;
                }
            }


            Zoperator[] opValues = str2.Zop();
            for (Zoperator opValue : opValues) {
                //System.out.println(">>opValue = " + opValue.getMatrix());
                if (hintUseOldZsStyle) {
                    Complex zs = opValue.getMatrix().get(0, 0);
                    Domain zsdomain = opValue.getFn().getEnv().getDomain();
                    DoubleToVector[] gzs = new DoubleToVector[g.length];
                    for (int i = 0; i < gzs.length; i++) {
                        gzs[i] = ExpressionTransformFactory.transform(g[i], ExpressionTransformFactory.domainMul(zsdomain)).toDV();
                    }
                    ComplexMatrix spc2 = Maths.scalarProductCache(gzs, gzs, str.getHintsManager().getHintAxisType().toAxisXY(), ProgressMonitors.none());
                    for (int p = 0; p < g.length; p++) {
                        ComplexVector spc2p = spc2.getRow(p);
                        for (int q = p; q < g.length; q++) {
                            b[p][q] = b[p][q].plus(zs.mul(spc2p.get(q)));
                        }
                    }
                } else {
                    Complex[][] op = opValue == null ? null : opValue.getMatrix().getArray();
                    if (op != null) {//op==null si k==1
                        //System.out.println("op = " + opValue.getMatrix());
                        ModeInfo[] n_propa = opValue.getFn().getPropagatingModes();
                        ComplexMatrix spc2 = Maths.scalarProductCache(g, opValue.getFn().arr(), str.getHintsManager().getHintAxisType().toAxisXY(), ProgressMonitors.none());
                        for (int p = 0; p < g.length; p++) {
                            ComplexVector spc2p = spc2.getRow(p);
                            for (int q = p; q < g.length; q++) {
                                ComplexVector spc2q = spc2.getRow(q);
                                Complex c = Maths.CZERO;
                                for (int m = 0; m < op.length; m++) {
                                    for (int n = 0; n < op[m].length; n++) {
                                        //#@ ID=1@20070423
                                        //#@   OLD
//                                    Complex sp1 = spc2.fg(n, p);
//                                    Complex sp2 = spc2.gf(q, m);
//                                    c = c.add((zop[n_propa[m].index][n_propa[n].index]).multiply(sp1).multiply(sp2));
                                        //#@   NEW
//                                    Complex sp1 = spc2.fg(n_propa[n].index, p);
//                                    Complex sp2 = spc2.gf(q, n_propa[m].index);
                                        Complex sp1 = spc2p.get(n_propa[m].index);
                                        Complex sp2 = spc2q.get(n_propa[n].index).conj();
                                        Complex zs = op[m][n];
                                        c = c.plus(zs.mul(sp1).mul(sp2));
                                        //#@   END
                                    }
                                }
                                b[p][q] = b[p][q].plus(c);
                            }
                        }
                    }
                }
            }
            for (int p = 0; p < g.length; p++) {
                for (int q = 0; q < p; q++) {
                    b[p][q] = b[q][p];
                }
            }

        } else {
            for (int p = 0; p < g.length; p++) {
                ComplexVector spp = sp.getRow(p);
                for (int q = 0; q < g.length; q++) {
                    ComplexVector spq = sp.getRow(q);
                    Complex c = Maths.CZERO;
                    for (ModeInfo n : n_evan) {
                        c = c.plus(n.impedance.impedanceValue().mul(spp.get(n.index)).mul(spq.get(n.index).conj()));
                    }
                    b[p][q] = c;
                }
            }


            Zoperator[] opValues = str2.Zop();
            for (Zoperator opValue : opValues) {
                //System.out.println(">>opValue = " + opValue.getMatrix());
                if (hintUseOldZsStyle) {
                    Complex zs = opValue.getMatrix().get(0, 0);
                    Domain zsdomain = opValue.getFn().getEnv().getDomain();
                    DoubleToVector[] gzs = new DoubleToVector[g.length];
                    for (int i = 0; i < gzs.length; i++) {
                        gzs[i] = ExpressionTransformFactory.transform(g[i], ExpressionTransformFactory.domainMul(zsdomain)).toDV();
                    }
                    ComplexMatrix spc2 = Maths.scalarProductCache(gzs, gzs, str.getHintsManager().getHintAxisType().toAxisXY(), ProgressMonitors.none());
                    for (int p = 0; p < g.length; p++) {
                        ComplexVector spc2p = spc2.getRow(p);
                        for (int q = p; q < g.length; q++) {
                            b[p][q] = b[p][q].plus(zs.mul(spc2p.get(q)));
                        }
                    }
                } else {
                    Complex[][] op = opValue == null ? null : opValue.getMatrix().getArray();
                    if (op != null) {//op==null si k==1
                        ModeInfo[] n_propa = opValue.getFn().getPropagatingModes();
                        ComplexMatrix spc2 = Maths.scalarProductCache(g, opValue.getFn().arr(), str.getHintsManager().getHintAxisType().toAxisXY(), ProgressMonitors.none());
                        for (int p = 0; p < g.length; p++) {
                            ComplexVector spc2p = spc2.getRow(p);
                            for (int q = 0; q < g.length; q++) {
                                ComplexVector spc2q = spc2.getRow(q);
                                Complex c = Maths.CZERO;
                                for (int m = 0; m < op.length; m++) {
                                    for (int n = 0; n < op[m].length; n++) {
                                        //#@ ID=1@20070423
                                        //#@   OLD
//                                    Complex sp1 = spc2.fg(n, p);
//                                    Complex sp2 = spc2.gf(q, m);
//                                    c = c.add((zop[n_propa[m].index][n_propa[n].index]).multiply(sp1).multiply(sp2));
                                        //#@   NEW
                                        Complex sp1 = spc2p.get(n_propa[n].index);
                                        Complex sp2 = spc2q.get(n_propa[m].index).conj();
                                        Complex zs = op[m][n];
                                        c = c.plus(zs.mul(sp1).mul(sp2));
                                        //#@   END
                                    }
                                }
                                b[p][q] = b[p][q].plus(c);
                            }
                        }
                    }
                }
            }
        }
        return Maths.matrix(b);
    }


    public static void storeString(String content, File file) throws IOException {
        System.out.println("file = " + file.getCanonicalPath());
        PrintStream os = null;
        try {
            os = new PrintStream(new FileOutputStream(file));
            os.print(content);
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    @Override
    public String toString() {
        return dump();
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.ofFunction(getClass().getSimpleName()).build();
    }

}