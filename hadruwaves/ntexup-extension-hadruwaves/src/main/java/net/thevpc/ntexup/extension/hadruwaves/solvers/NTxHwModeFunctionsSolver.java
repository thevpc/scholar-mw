package net.thevpc.ntexup.extension.hadruwaves.solvers;

import net.thevpc.ntexup.extension.hadruwaves.MoMStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.hadruwaves.base.NTxHwNopNTxSolver;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.mom.ModeFunctions;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

public class NTxHwModeFunctionsSolver extends NTxHwNopNTxSolver {

    public NTxHwModeFunctionsSolver(MoMStrNTxSimulationPlan moMStrSimulationQuery, String computeName, String solverName) {
        super(moMStrSimulationQuery, computeName, solverName,"mode-functions");
    }

    @Override
    protected void nop() {
        MomStructure str = momStructure();
        ModeFunctions modes = str.modeFunctions();
        ModeInfo[] modesArr = modes.getModes();
        log(NMsg.ofC("------------------"));
        log(NMsg.ofC("[%s] MODE FUNCTIONS (%s): ", outputName(),modesArr.length));
        log(NMsg.ofC("------------------"));
        for (int i = 0; i < modesArr.length; i++) {
            if(i>10){
                break;
            }
            ModeInfo mode = modesArr[i];
            log(NMsg.ofC("%s : %s", mode.mode, mode.fn));
        }
        if (plan().rendererContext().isAnimate()) {
            Plot.cd(fullPath()).title(fullName()).plot(modes.arr());
        }
    }


}
