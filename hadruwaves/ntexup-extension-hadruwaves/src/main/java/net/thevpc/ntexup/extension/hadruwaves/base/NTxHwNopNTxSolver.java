package net.thevpc.ntexup.extension.hadruwaves.base;

import net.thevpc.ntexup.extension.hadruwaves.MoMStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.mwsimulator.*;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.time.NChronometer;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

import java.util.Arrays;
import java.util.List;

public abstract class NTxHwNopNTxSolver extends NTxSolverRunImpl {

    public NTxHwNopNTxSolver(MoMStrNTxSimulationPlan moMStrSimulationQuery, String computeName, String solverName, String solverType) {
        super(computeName, solverName, solverType,moMStrSimulationQuery);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedUplet(outputName(),NElement.ofPair("solver",solverType()));
    }

    @Override
    public List<NTxSimulationResult> execute() {
        MomStructure str = ((MoMStrNTxSimulationPlan) plan()).str;
        NChronometer chronometer = NChronometer.startNow();
        str.log().log(NMsg.ofC("------------------"));
        str.log().log(NMsg.ofC("[%s] %s : ", outputName(), solverName()));
        str.log().log(NMsg.ofC("------------------"));
        nop(str);
        str.log().log(NMsg.ofC("[%s] %s Finished in %s : ", outputName(), solverName(),chronometer.stop()));
        return Arrays.asList(
                NTxSimulationResultFactory.createPlot2dCurve(outputName(), null, Arrays.asList(0.0))
        );
    }

    protected abstract void nop(MomStructure str);


}
