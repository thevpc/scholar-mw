package net.thevpc.ntexup.extension.mwsimulator;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NToElement;

import java.util.List;

public interface NTxSolverRun extends NToElement {
    String outputName();
    String solverName();
    String solverType();
    NTxSolverRun add(String paramName, NElement paramValue);

    NTxSolverRun compile();

    List<NTxSimulationResult> execute();

}
