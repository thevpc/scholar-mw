package net.vpc.scholar.hadruwaves.mom.str.zsfractalmodel;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.ProgressMonitors;
import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.TestFunctions;
import net.vpc.scholar.hadruwaves.mom.str.MatrixAEvaluator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 21:55:23
 */
public class ZsFactalMatrixAWaveguideParallelEvaluator implements MatrixAEvaluator {
    public ComplexMatrix evaluate(MomStructure str, ProgressMonitor monitor) {
        MomStructureFractalZop str2 = (MomStructureFractalZop) str;
        TestFunctions gpTestFunctions = str.getTestFunctions();
        DoubleToVector[] g = gpTestFunctions.arr();
        Complex[][] b = new Complex[g.length][g.length];
        ModeFunctions fn = str.getModeFunctions();
        ModeInfo[] modes = str.getModes();
        ModeInfo[] n_evan = str.getHintsManager().isHintRegularZnOperator() ? modes : fn.getVanishingModes();
        ComplexMatrix sp = str.getTestModeScalarProducts(ProgressMonitors.none());
        boolean complex = fn.isComplex() || gpTestFunctions.isComplex();
        boolean symMatrix = !complex;
        if (symMatrix) {
            for (int p = 0; p < g.length; p++) {
                ComplexVector spp = sp.getRow(p);
                for (int q = p; q < g.length; q++) {
                    ComplexVector spq = sp.getRow(q);
                    Complex c = Maths.CZERO;
                    for (ModeInfo n : n_evan) {
                        Complex yn = n.impedance.admittanceValue();
                        c = c.add(yn.mul(spp.get(n.index)).mul(spq.get(n.index).conj()));
                    }
                    b[p][q] = c;
                }
            }


            Yoperator[] opValues = str2.Yop();
            for (Yoperator opValue : opValues) {
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
                                    c = c.add((op[m][n]).mul(sp1).mul(sp2));
                                    //#@   END
                                }
                            }
                            b[p][q] = b[p][q].add(c);
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
                        c = c.add(n.impedance.impedanceValue().mul(spp.get(n.index)).mul(spq.get(n.index).conj()));
                    }
                    b[p][q] = c;
                }
            }


            Yoperator[] opValues = str2.Yop();
            for (Yoperator opValue : opValues) {
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
                                    c = c.add((op[m][n]).mul(sp1).mul(sp2));
                                    //#@   END
                                }
                            }
                            b[p][q] = b[p][q].add(c);
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
        return Tson.function(getClass().getSimpleName()).build();
    }

}
