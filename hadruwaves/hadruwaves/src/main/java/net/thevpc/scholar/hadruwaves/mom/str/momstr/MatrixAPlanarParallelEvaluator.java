package net.thevpc.scholar.hadruwaves.mom.str.momstr;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitors;

import net.thevpc.nuts.elem.NElement;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.mom.ModeFunctions;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.TestFunctions;
import net.thevpc.scholar.hadruwaves.mom.str.MatrixAEvaluator;
import net.thevpc.scholar.hadruwaves.util.Impedance;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 21:55:23
 */
public class MatrixAPlanarParallelEvaluator implements MatrixAEvaluator {
    public static final MatrixAPlanarParallelEvaluator INSTANCE = new MatrixAPlanarParallelEvaluator();

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
        Impedance scalarSurfaceImpedance=str.getSerialZs();
        if (symMatrix) {
            for (int p = 0; p < _g.length; p++) {
                ComplexVector spp = sp.getRow(p);
                for (int q = p; q < _g.length; q++) {
                    ComplexVector spq = sp.getRow(q);
                    MutableComplex c = MutableComplex.Zero();
                    for (ModeInfo n : n_eva) {
                        Complex yn = n.impedance.serial(scalarSurfaceImpedance).admittanceValue();
                        Complex sp1 = spp.get(n.index);
                        Complex sp2 = spq.get(n.index).conj();
                        c.addProduct(yn, sp1, sp2);
                    }
                    b[p][q] = c.toComplex();
                }
            }
            for (int p = 0; p < _g.length; p++) {
                for (int q = 0; q < p; q++) {
                    b[p][q] = b[q][p];
                }
            }
        } else {
            for (int p = 0; p < _g.length; p++) {
                ComplexVector spp = sp.getRow(p);
                for (int q = 0; q < _g.length; q++) {
                    ComplexVector spq = sp.getRow(q);
                    MutableComplex c = MutableComplex.Zero();
                    for (ModeInfo n : n_eva) {
                        Complex yn = n.impedance.serial(scalarSurfaceImpedance).admittanceValue();
                        Complex sp1 = spp.get(n.index);
                        Complex sp2 = spq.get(q).conj();
                        c.addProduct(yn, sp1, sp2);
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
    public NElement toElement() {
        return NElement.ofUplet(getClass().getSimpleName());
    }

}
