package net.thevpc.ntexup.extension.hadruwaves.solvers;

import net.thevpc.ntexup.extension.hadruwaves.MoMStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.hadruwaves.base.NTxHwNopNTxSolver;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.mom.ModeFunctions;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

import java.util.Arrays;

public class NTxHwModeImpedanceNTxSolver extends NTxHwNopNTxSolver {

    public NTxHwModeImpedanceNTxSolver(MoMStrNTxSimulationPlan moMStrSimulationQuery, String computeName, String solverName) {
        super(moMStrSimulationQuery, computeName, solverName,"mode-impedance");
    }

    @Override
    protected void nop(MomStructure str) {
        ModeFunctions modes = str.modeFunctions();
        ModeInfo[] modesArr = modes.getModes();
        str.log().log(NMsg.ofC("------------------"));
        str.log().log(NMsg.ofC("[%s] MODE FUNCTIONS (%s) : ", outputName(),modesArr.length));
        str.log().log(NMsg.ofC("------------------"));
        for (int i = 0; i < modesArr.length; i++) {
            if(i>10){
                break;
            }
            ModeInfo mode = modesArr[i];
            str.log().log(NMsg.ofC("%s : %s", mode.mode, mode.impedance.impedanceValue()));
        }
        Plot.title(solverType()+"-"+outputName()).plot(Arrays.stream(str.modeFunctions().getModes()).map(x -> x.impedance.impedanceValue()).toArray(Complex[]::new));
    }


}
