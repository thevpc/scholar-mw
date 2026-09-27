package net.thevpc.ntexup.extension.hadruwaves.solvers;

import net.thevpc.ntexup.extension.hadruwaves.MoMStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.hadruwaves.base.NTxHwComplexNTxSolver;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

public class NTxHwS11Solver extends NTxHwComplexNTxSolver {

    public NTxHwS11Solver(MoMStrNTxSimulationPlan moMStrSimulationQuery, String computeName, String solverName) {
        super(moMStrSimulationQuery,computeName, solverName,"s-parameters");
    }

    protected Complex evalComplex(MomStructure str) {
        Complex zin = str.inputImpedance().evalComplex();
        Complex s11 = str.sparameters().evalComplex();
        net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector[] tfs = str.testFunctions().toArray();
        net.thevpc.scholar.hadrumaths.ComplexMatrix matB = str.matrixB().evalMatrix();
        int bNonZero = 0;
        StringBuilder bInfo = new StringBuilder();
        for (int i = 0; i < matB.getRowCount(); i++) {
            Complex bv = matB.get(i, 0);
            if (!bv.isZero()) {
                bNonZero++;
                net.thevpc.scholar.hadrumaths.symbolic.double2double.RWG rwg = net.thevpc.scholar.hadruwaves.mom.RWGDeltaGapBMatrix.tryUnwrapRWG(tfs[i]);
                if (rwg != null) {
                    net.thevpc.scholar.hadrumaths.geom.HPoint mid = rwg.getSharedEdgeMidpoint();
                    bInfo.append(String.format(" [i=%d, B=%s, y=%.3fmm]", i, bv, mid.y * 1000));
                }
            }
        }
        str.log().log(net.thevpc.nuts.text.NMsg.ofC("DEBUG S11: freq=%.4e, tfs=%d, bNonZero=%d%s, Zin=%s, S11=%s (%.2f dB)",
                str.getFrequency(), tfs.length, bNonZero, bInfo.toString(), zin, s11, 20 * Math.log10(s11.abs().toDouble())));
        return s11;
    }

}
