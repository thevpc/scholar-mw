package net.thevpc.ntexup.extension.hadruwaves.solvers;

import net.thevpc.ntexup.extension.hadruwaves.MoMStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.hadruwaves.base.NTxHwNopNTxSolver;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

public class NTxHwChronometerSolver extends NTxHwNopNTxSolver {

    public NTxHwChronometerSolver(MoMStrNTxSimulationPlan moMStrSimulationQuery, String computeName, String solverName) {
        super(moMStrSimulationQuery, computeName, solverName, "test-functions");
    }

    @Override
    public void beforeAll() {
        log(NMsg.ofC("------------------"));
        log(NMsg.ofC("[%s] PLAN STARTED : %s", fullName(), plan().chronometerView()));
        log(NMsg.ofC("------------------"));
    }

    @Override
    protected void nop() {
        if (plan().rendererContext().isAnimate()) {
            Plot.cd(fullPath())
                    .title("Plan Chronometer")
                    .plot(plan().chronometerView());
        }
    }

    @Override
    public void afterAll() {
        log(NMsg.ofC("------------------"));
        log(NMsg.ofC("[%s] PLAN STOPPED : %s", fullName(), plan().chronometerView()));
        log(NMsg.ofC("------------------"));
    }
}
