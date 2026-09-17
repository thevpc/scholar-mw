package net.thevpc.ntexup.extension.getdp.solvers;

import net.thevpc.ntexup.extension.getdp.GetDPStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationPlan;
import net.thevpc.scholar.hadrumaths.Complex;

public class NTxGetDPZinSolver extends NTxGetDPComplexNTxSolver {

    public NTxGetDPZinSolver(String computeName, String solverName, NTxSimulationPlan plan) {
        super(computeName, solverName, "Zin", plan);
    }

    @Override
    protected Complex evalComplex(GetDPStrNTxSimulationPlan plan, double freq) {
        return plan.computeZin(freq);
    }
}
