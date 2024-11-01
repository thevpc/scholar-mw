package net.thevpc.scholar.hadruwaves.mom.str.momstr;

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

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 21:55:23
 */
public class MatrixAWaveguideSerialEvaluator implements MatrixAEvaluator {
    public static final MatrixAWaveguideSerialEvaluator INSTANCE = new MatrixAWaveguideSerialEvaluator();

    public ComplexMatrix evaluate(MomStructure str, ProgressMonitor monitor) {
        TestFunctions gpTestFunctions = str.testFunctions();
        DoubleToVector[] _g = gpTestFunctions.arr();
        Complex[][] b = new Complex[_g.length][_g.length];
        ModeFunctions fn = str.modeFunctions();
        ModeInfo[] modes = str.getModes();
        ModeInfo[] n_eva = str.getHintsManager().isHintRegularZnOperator() ? modes : fn.getVanishingModes();
        ComplexMatrix sp = str.getTestModeScalarProducts(ProgressMonitors.none());
        boolean complex = fn.isComplex() || gpTestFunctions.isComplex();
        boolean symMatrix = !complex;
        int gmax = _g.length;
        if (symMatrix) {
            for (int p = 0; p < gmax; p++) {
                ComplexVector spp = sp.getRow(p);
                for (int q = p; q < gmax; q++) {
                    ComplexVector spq = sp.getRow(q);
                    MutableComplex c = MutableComplex.Zero();
                    for (ModeInfo n : n_eva) {
                        Complex zn = n.impedance.impedanceValue();
                        Complex sp1 = spp.get(n.index);
                        Complex sp2 = spq.get(n.index).conj();
                        c.addProduct(zn, sp1, sp2);
                    }
                    b[p][q] = c.toComplex();
                }
            }
            for (int p = 0; p < gmax; p++) {
                for (int q = 0; q < p; q++) {
                    b[p][q] = b[q][p];
                }
            }
        } else {
            for (int p = 0; p < gmax; p++) {
                ComplexVector spp = sp.getRow(p);
                for (int q = 0; q < gmax; q++) {
                    ComplexVector spq = sp.getRow(q);
                    MutableComplex c = MutableComplex.Zero();
                    for (ModeInfo n : n_eva) {
                        Complex zn = n.impedance.impedanceValue();
                        Complex sp1 = spp.get(n.index);
                        Complex sp2 = spq.get(n.index).conj();
                        c.addProduct(zn, sp1, sp2);
                    }
                    b[p][q] = c.toComplex();
                }
            }
        }
        return Maths.matrix(b);
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