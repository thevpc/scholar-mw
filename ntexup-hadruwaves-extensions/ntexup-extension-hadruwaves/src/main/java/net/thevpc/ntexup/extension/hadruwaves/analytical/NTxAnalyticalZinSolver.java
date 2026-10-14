package net.thevpc.ntexup.extension.hadruwaves.analytical;

import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationPlan;
import net.thevpc.scholar.hadrumaths.Complex;

public class NTxAnalyticalZinSolver extends NTxAnalyticalComplexNTxSolver {
    public NTxAnalyticalZinSolver(String computeName, String solverName, NTxSimulationPlan query) {
        super(computeName, solverName, "input-impedance", query);
    }

    @Override
    protected Complex evalComplex(AnalyticalStrNTxSimulationPlan plan, double freq) {
        return plan.computeZin(freq);
    }
}
