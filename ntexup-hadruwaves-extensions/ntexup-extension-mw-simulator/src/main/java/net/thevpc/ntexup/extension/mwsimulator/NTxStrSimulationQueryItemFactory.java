package net.thevpc.ntexup.extension.mwsimulator;

import net.thevpc.nuts.elem.NElement;

public interface NTxStrSimulationQueryItemFactory<T extends NTxSimulationPlan> extends NTxSimulationResults {
    NTxSolverRun create(T query, NElement element);
}
