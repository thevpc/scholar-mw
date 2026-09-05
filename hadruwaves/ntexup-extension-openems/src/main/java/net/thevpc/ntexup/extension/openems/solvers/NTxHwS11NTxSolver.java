package net.thevpc.ntexup.extension.openems.solvers;

import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationPlan;
import net.thevpc.ntexup.extension.openems.OpenEMSStrNTxSimulationPlan;
import net.thevpc.scholar.hadrumaths.Complex;

public class NTxHwS11NTxSolver extends NTxOpenEMSComplexNTxSolver {
    public NTxHwS11NTxSolver(String computeName, String solverName, NTxSimulationPlan query) {
        super(computeName, solverName, "s-parameters", query);
    }

    @Override
    protected Complex evalComplex(OpenEMSStrNTxSimulationPlan plan, double freq) {
        return plan.computeS11(freq);
    }
}
