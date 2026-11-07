package net.thevpc.ntexup.extension.palace.solvers;

import net.thevpc.ntexup.extension.palace.PalaceStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationPlan;
import net.thevpc.scholar.hadrumaths.Complex;

public class NTxPalaceZinSolver extends NTxPalaceComplexNTxSolver {

    public NTxPalaceZinSolver(String computeName, String solverName, NTxSimulationPlan plan) {
        super(computeName, solverName, "Zin", plan);
    }

    @Override
    protected Complex evalComplex(PalaceStrNTxSimulationPlan plan, double freq) {
        return plan.computeZin(freq);
    }
}
