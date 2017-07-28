package net.vpc.scholar.hadruwaves.mom.str.momstr;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductCache;

import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;
import net.vpc.scholar.hadruwaves.mom.TestFunctions;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.mom.str.MatrixAEvaluator;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 21:55:23
 */
public class MatrixAWaveguideSerialEvaluator implements MatrixAEvaluator {
    public static final MatrixAWaveguideSerialEvaluator INSTANCE=new MatrixAWaveguideSerialEvaluator();
    public Matrix evaluate(MomStructure str, ComputationMonitor monitor) {
        TestFunctions gpTestFunctions = str.getTestFunctions();
        DoubleToVector[] _g = gpTestFunctions.arr();
        Complex[][] b = new Complex[_g.length][_g.length];
        ModeFunctions fn = str.getModeFunctions();
        ModeInfo[] modes = str.getModes();
        ModeInfo[] n_eva = str.getHintsManager().isHintRegularZnOperator() ? modes : fn.getVanishingModes();
        ScalarProductCache sp = str.getTestModeScalarProducts(ComputationMonitorFactory.none());
        boolean complex = fn.isComplex() || gpTestFunctions.isComplex();
        boolean symMatrix = !complex;
        int gmax = _g.length;
        if (symMatrix) {
            for (int p = 0; p < gmax; p++) {
                Vector spp = sp.getRow(p);
                for (int q = p; q < gmax; q++) {
                    Vector spq = sp.getRow(q);
                    MutableComplex c = MutableComplex.Zero();
                    for (ModeInfo n : n_eva) {
                        Complex zn = n.impedance;
                        Complex sp1 = spp.get(n.index);
                        Complex sp2 = spq.get(n.index).conj();
                        c.addProduct(zn,sp1,sp2);
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
                Vector spp = sp.getRow(p);
                for (int q = 0; q < gmax; q++) {
                    Vector spq = sp.getRow(q);
                    MutableComplex c = MutableComplex.Zero();
                    for (ModeInfo n : n_eva) {
                        Complex zn = n.impedance;
                        Complex sp1 = spp.get(n.index);
                        Complex sp2 = spq.get(n.index).conj();
                        c.addProduct(zn,sp1,sp2);
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