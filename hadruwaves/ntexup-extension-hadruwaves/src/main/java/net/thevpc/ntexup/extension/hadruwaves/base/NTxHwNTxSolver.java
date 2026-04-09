package net.thevpc.ntexup.extension.hadruwaves.base;

import net.thevpc.ntexup.extension.hadruwaves.MoMStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.mwsimulator.NTxSolverRunImpl;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

public abstract class NTxHwNTxSolver extends NTxSolverRunImpl {

    public NTxHwNTxSolver(MoMStrNTxSimulationPlan moMStrSimulationQuery, String computeName, String solverName, String solverType) {
        super(computeName, solverName, solverType,moMStrSimulationQuery);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedUplet(outputName(),NElement.ofPair("solver",solverType()));
    }

    protected void log(NMsg msg) {
        momStructure().log().log(msg);
    }

    protected MomStructure momStructure() {
        return ((MoMStrNTxSimulationPlan) plan()).str;
    }



}
