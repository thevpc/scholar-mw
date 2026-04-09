package net.thevpc.ntexup.extension.mwsimulator;

import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.nuts.time.NChronometerView;

import java.util.List;

public interface NTxSimulationPlan {

    NTxRendererContext rendererContext();

    NTxSolverRun add(String computeName, String solverName);

    void compile();

    String hash();

    String id();

    NChronometerView chronometerView();
    String name();

    void addSolverListener(NTxSolverListener listener);

    List<NTxSolverListener> solverListeners();

    List<NTxSolverRun> runs();
}
