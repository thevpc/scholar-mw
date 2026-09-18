package net.thevpc.ntexup.extension.scuffem.solvers;

import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationPlan;
import net.thevpc.ntexup.extension.scuffem.ScuffEMStrNTxSimulationPlan;
import net.thevpc.scholar.hadrumaths.Complex;

public class NTxScuffEMS11Solver extends NTxScuffEMComplexNTxSolver {
    public NTxScuffEMS11Solver(String computeName, String solverName, NTxSimulationPlan query) {
        super(computeName, solverName, "s-parameters", query);
    }

    @Override
    protected Complex evalComplex(ScuffEMStrNTxSimulationPlan plan, double freq) {
        return plan.computeS11(freq);
    }
}
