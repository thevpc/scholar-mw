package net.vpc.scholar.hadruwaves.mom.str.momstr;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.TestFunctions;
import net.vpc.scholar.hadruwaves.mom.str.MatrixAEvaluator;
import net.vpc.scholar.hadruwaves.util.Impedance;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 21:55:23
 */
public class MatrixAPlanarParallelEvaluator implements MatrixAEvaluator {
    public static final MatrixAPlanarParallelEvaluator INSTANCE = new MatrixAPlanarParallelEvaluator();

    public Matrix evaluate(MomStructure str, ProgressMonitor monitor) {
        TestFunctions gpTestFunctions = str.getTestFunctions();
        DoubleToVector[] _g = gpTestFunctions.arr();
        Complex[][] b = new Complex[_g.length][_g.length];
        ModeFunctions fn = str.getModeFunctions();
        ModeInfo[] modes = str.getModes();
        ModeInfo[] n_eva = str.getHintsManager().isHintRegularZnOperator() ? modes : fn.getVanishingModes();
        TMatrix<Complex> sp = str.getTestModeScalarProducts(ProgressMonitorFactory.none());
        boolean complex = fn.isComplex() || gpTestFunctions.isComplex();
        boolean symMatrix = !complex;
        Impedance scalarSurfaceImpedance=str.getSerialZs();
        if (symMatrix) {
            for (int p = 0; p < _g.length; p++) {
                TVector<Complex> spp = sp.getRow(p);
                for (int q = p; q < _g.length; q++) {
                    TVector<Complex> spq = sp.getRow(q);
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
                TVector<Complex> spp = sp.getRow(p);
                for (int q = 0; q < _g.length; q++) {
                    TVector<Complex> spq = sp.getRow(q);
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
        return getClass().getName();
    }

    @Override
    public String dump() {
        return getClass().getName();
    }

}