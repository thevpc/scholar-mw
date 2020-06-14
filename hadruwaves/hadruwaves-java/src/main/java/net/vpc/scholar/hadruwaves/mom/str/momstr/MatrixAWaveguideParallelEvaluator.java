package net.vpc.scholar.hadruwaves.mom.str.momstr;

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

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 21:55:23
 */
public class MatrixAWaveguideParallelEvaluator implements MatrixAEvaluator {
    public static final MatrixAWaveguideParallelEvaluator INSTANCE = new MatrixAWaveguideParallelEvaluator();

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
        if (symMatrix) {
            for (int p = 0; p < _g.length; p++) {
                ComplexVector spp = sp.getRow(p);
                for (int q = p; q < _g.length; q++) {
                    ComplexVector spq = sp.getRow(q);
                    MutableComplex c = MutableComplex.Zero();
                    for (ModeInfo n : n_eva) {
                        //TODO see serial!!
                        Complex yn = n.impedance.admittanceValue();
                        Complex sp1 = spp.get(n.index);
                        Complex sp2 = spq.get(n.index).conj();
//                        System.out.println(n+" ; yn = " + yn+" ; sp = " + sp1+" ; "+sp2);
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
                        Complex yn = n.impedance.admittanceValue();
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
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.function(getClass().getSimpleName()).build();
    }

}