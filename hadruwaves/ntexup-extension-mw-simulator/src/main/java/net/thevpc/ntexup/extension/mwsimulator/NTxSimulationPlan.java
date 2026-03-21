package net.thevpc.ntexup.extension.mwsimulator;

import java.util.List;

public interface NTxSimulationPlan {
    NTxSolverRun add(String computeName, String solverName);

    void compile();

    String hash();

    String name();

    List<NTxSolverRun> runs();
}
