package net.thevpc.ntexup.extension.hadruwaves.base;

import net.thevpc.ntexup.extension.hadruwaves.MoMStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.mwsimulator.*;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.time.NChronometer;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

import java.util.Arrays;
import java.util.List;

public abstract class NTxHwNopNTxSolver extends NTxHwNTxSolver {

    public NTxHwNopNTxSolver(MoMStrNTxSimulationPlan plan, String computeName, String solverName, String solverType) {
        super(plan,computeName, solverName, solverType);
    }

    @Override
    public List<NTxSimulationResult> execute() {
        NChronometer chronometer = NChronometer.of();
        log(NMsg.ofC("------------------"));
        log(NMsg.ofC("[%s] %s : ", outputName(), solverName()));
        log(NMsg.ofC("------------------"));
        nop();
        log(NMsg.ofC("[%s] %s Finished in %s : ", outputName(), solverName(),chronometer.stop()));
        return Arrays.asList(
                NTxSimulationResultFactory.createPlot2dCurve(outputName(), null, Arrays.asList(0.0))
        );
    }

    protected abstract void nop();

}
