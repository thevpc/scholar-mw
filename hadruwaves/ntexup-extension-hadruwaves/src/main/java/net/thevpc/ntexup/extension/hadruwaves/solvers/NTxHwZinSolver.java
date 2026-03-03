package net.thevpc.ntexup.extension.hadruwaves.solvers;

import net.thevpc.ntexup.extension.hadruwaves.MoMStrSimulationQuery;
import net.thevpc.ntexup.extension.mwsimulator.NTxStrSimulationQuery;
import net.thevpc.ntexup.extension.mwsimulator.NTxTargetSolver;

public class NTxHwZinSolver implements NTxTargetSolver {
    @Override
    public Object solve(NTxStrSimulationQuery query) {
        return ((MoMStrSimulationQuery)query).str.inputImpedance().evalComplex();
    }
}
