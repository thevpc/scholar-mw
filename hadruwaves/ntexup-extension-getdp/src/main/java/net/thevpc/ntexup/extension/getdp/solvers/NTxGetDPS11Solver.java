package net.thevpc.ntexup.extension.getdp.solvers;

import net.thevpc.ntexup.extension.getdp.GetDPStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationPlan;
import net.thevpc.scholar.hadrumaths.Complex;

public class NTxGetDPS11Solver extends NTxGetDPComplexNTxSolver {

    public NTxGetDPS11Solver(String computeName, String solverName, NTxSimulationPlan plan) {
        super(computeName, solverName, "S11", plan);
    }

    @Override
    protected Complex evalComplex(GetDPStrNTxSimulationPlan plan, double freq) {
        return plan.computeS11(freq);
    }
}
