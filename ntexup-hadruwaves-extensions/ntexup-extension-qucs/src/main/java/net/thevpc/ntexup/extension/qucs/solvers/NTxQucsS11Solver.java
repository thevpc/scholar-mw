package net.thevpc.ntexup.extension.qucs.solvers;

import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationPlan;
import net.thevpc.ntexup.extension.qucs.QucsStrNTxSimulationPlan;
import net.thevpc.scholar.hadrumaths.Complex;

public class NTxQucsS11Solver extends NTxQucsComplexNTxSolver {

    public NTxQucsS11Solver(String computeName, String solverName, NTxSimulationPlan plan) {
        super(computeName, solverName, "S11", plan);
    }

    @Override
    protected Complex evalComplex(QucsStrNTxSimulationPlan plan, double freq) {
        return plan.computeS11(freq);
    }
}
