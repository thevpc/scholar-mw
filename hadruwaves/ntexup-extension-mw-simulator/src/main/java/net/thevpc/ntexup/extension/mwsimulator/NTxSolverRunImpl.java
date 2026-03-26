package net.thevpc.ntexup.extension.mwsimulator;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.util.NAssert;
import net.thevpc.nuts.util.NNameFormat;

public abstract class NTxSolverRunImpl implements NTxSolverRun {
    private final NTxSimulationPlan plan;
    private final String outputName;
    private final String solverName;
    private final String solverType;
    private boolean compiled;

    public NTxSolverRunImpl(String outputName, String solverName, String solverType, NTxSimulationPlan plan) {
        this.outputName = outputName;
        this.solverName = solverName;
        this.solverType = NNameFormat.LOWER_KEBAB_CASE.format(solverType);
        this.plan = plan;
    }

    protected void compileImpl() {

    }

    @Override
    public final NTxSolverRun add(String paramName, NElement paramValue) {
        ensureNotCompiled();
        addImpl(paramName, paramValue);
        return this;
    }

    protected void addImpl(String paramName, NElement paramValue) {

    }

    protected NTxSolverRun ensureNotCompiled() {
        NAssert.requireNamedFalse(compiled, "compiled");
        return this;
    }

    @Override
    public final NTxSolverRun compile() {
        if (!compiled) {
            compileImpl();
            this.compiled = true;
        }
        return this;
    }

    public String outputName() {
        return outputName;
    }

    public String solverName() {
        return solverName;
    }

    public String solverType() {
        return solverType;
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedUplet(outputName, NElement.ofPair("solver", solverName));
    }

    public NTxSimulationPlan plan() {
        return plan;
    }
}
