package net.thevpc.ntexup.extension.scuffem.solvers;

import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationPlan;
import net.thevpc.ntexup.extension.scuffem.ScuffEMStrNTxSimulationPlan;
import net.thevpc.scholar.hadrumaths.Complex;

public class NTxScuffEMZinSolver extends NTxScuffEMComplexNTxSolver {
    public NTxScuffEMZinSolver(String computeName, String solverName, NTxSimulationPlan query) {
        super(computeName, solverName, "input-impedance", query);
    }

    @Override
    protected Complex evalComplex(ScuffEMStrNTxSimulationPlan plan, double freq) {
        return plan.computeZin(freq);
    }
}
