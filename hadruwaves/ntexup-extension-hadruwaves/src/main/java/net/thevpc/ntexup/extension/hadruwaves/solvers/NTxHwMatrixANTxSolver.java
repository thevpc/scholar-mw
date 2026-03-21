package net.thevpc.ntexup.extension.hadruwaves.solvers;

import net.thevpc.ntexup.extension.hadruwaves.MoMStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.hadruwaves.base.NTxHwComplexMatrixNTxSolver;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

public class NTxHwMatrixANTxSolver extends NTxHwComplexMatrixNTxSolver {

    public NTxHwMatrixANTxSolver(MoMStrNTxSimulationPlan moMStrSimulationQuery, String computeName, String solverName) {
        super(moMStrSimulationQuery, computeName, solverName,"a-matrix");
    }

    @Override
    protected ComplexMatrix matrix(MomStructure str) {
        return str.matrixA().evalMatrix();
    }


}
