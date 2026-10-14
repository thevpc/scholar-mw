package net.thevpc.ntexup.extension.hadruwaves.analytical;

import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationPlan;
import net.thevpc.scholar.hadrumaths.Complex;

public class NTxAnalyticalS11Solver extends NTxAnalyticalComplexNTxSolver {
    public NTxAnalyticalS11Solver(String computeName, String solverName, NTxSimulationPlan query) {
        super(computeName, solverName, "s-parameters", query);
    }

    @Override
    protected Complex evalComplex(AnalyticalStrNTxSimulationPlan plan, double freq) {
        return plan.computeS11(freq);
    }
}
