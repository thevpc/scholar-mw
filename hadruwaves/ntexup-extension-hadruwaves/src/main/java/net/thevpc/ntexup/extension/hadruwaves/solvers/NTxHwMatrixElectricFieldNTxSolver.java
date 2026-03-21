package net.thevpc.ntexup.extension.hadruwaves.solvers;

import net.thevpc.ntexup.extension.hadruwaves.MoMStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.hadruwaves.base.NTxHwSpaceComplexMatrixNTxSolver;
import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

public class NTxHwMatrixElectricFieldNTxSolver extends NTxHwSpaceComplexMatrixNTxSolver {
    public NTxHwMatrixElectricFieldNTxSolver(MoMStrNTxSimulationPlan moMStrSimulationQuery, String computeName, String solverName) {
        super(computeName, solverName, "electric-field", moMStrSimulationQuery);
    }

    @Override
    protected ComplexMatrix matrix(MomStructure str) {
        return str.electricField().cartesian().evalMatrix(axis, xSweep.doubleValues(), ySweep.doubleValues(), 0);
    }


}
