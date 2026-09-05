package net.thevpc.ntexup.extension.openems.solvers;

import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationPlan;
import net.thevpc.ntexup.extension.openems.OpenEMSStrNTxSimulationPlan;
import net.thevpc.scholar.hadrumaths.Complex;

public class NTxHwZinNTxSolver extends NTxOpenEMSComplexNTxSolver {
    public NTxHwZinNTxSolver(String computeName, String solverName, NTxSimulationPlan query) {
        super(computeName, solverName, "input-impedance", query);
    }

    @Override
    protected Complex evalComplex(OpenEMSStrNTxSimulationPlan plan, double freq) {
        return plan.computeZin(freq);
    }
}
