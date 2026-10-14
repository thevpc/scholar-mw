package net.thevpc.ntexup.extension.mwsimulator;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NToElement;

import java.util.List;

public interface NTxSolverRun extends NToElement {
    String outputName();

    String solverName();

    String solverType();

    NTxSolverRun add(String paramName, NElement paramValue);

    String fullName();

    NTxSolverRun compile();

    default void beforeAll(){}

    List<NTxSimulationResult> execute();

    default void afterAll(){}
}
