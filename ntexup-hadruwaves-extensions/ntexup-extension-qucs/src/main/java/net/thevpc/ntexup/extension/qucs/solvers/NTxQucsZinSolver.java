package net.thevpc.ntexup.extension.qucs.solvers;

import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationPlan;
import net.thevpc.ntexup.extension.qucs.QucsStrNTxSimulationPlan;
import net.thevpc.scholar.hadrumaths.Complex;

public class NTxQucsZinSolver extends NTxQucsComplexNTxSolver {

    public NTxQucsZinSolver(String computeName, String solverName, NTxSimulationPlan plan) {
        super(computeName, solverName, "Zin", plan);
    }

    @Override
    protected Complex evalComplex(QucsStrNTxSimulationPlan plan, double freq) {
        return plan.computeZin(freq);
    }
}
