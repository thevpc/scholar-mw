package net.thevpc.ntexup.extension.hadruwaves.solvers;

import net.thevpc.ntexup.extension.hadruwaves.MoMStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.hadruwaves.base.NTxHwSpaceComplexMatrixNTxSolver;
import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

public class NTxHwMatrixCurrentNTxSolver extends NTxHwSpaceComplexMatrixNTxSolver {
    public NTxHwMatrixCurrentNTxSolver(String computeName, String solverName, MoMStrNTxSimulationPlan moMStrSimulationQuery) {
        super(computeName, solverName,"current", moMStrSimulationQuery);
    }

    @Override
    protected ComplexMatrix matrix(MomStructure str) {
        return str.current().evalMatrix(axis, xSweep.doubleValues(), ySweep.doubleValues(), 0);
    }


}
