package net.thevpc.ntexup.extension.mwsimulator;

import net.thevpc.ntexup.api.renderer.NTxRendererContext;

import java.util.List;

public interface NTxSimulationPlan {
    NTxRendererContext rendererContext();
    NTxSolverRun add(String computeName, String solverName);

    void compile();

    String hash();

    String name();

    List<NTxSolverRun> runs();
}
