package net.thevpc.ntexup.extension.hadruwaves.solvers;

import net.thevpc.ntexup.extension.hadruwaves.MoMStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.hadruwaves.base.NTxHwNopNTxSolver;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.TestFunctions;

public class NTxHwTestFunctionsNTxSolver extends NTxHwNopNTxSolver {

    public NTxHwTestFunctionsNTxSolver(MoMStrNTxSimulationPlan moMStrSimulationQuery, String computeName, String solverName) {
        super(moMStrSimulationQuery, computeName, solverName,"test-functions");
    }

    @Override
    protected void nop(MomStructure str) {
        TestFunctions testFunctions = str.testFunctions();
        DoubleToVector[] testFunctionsArr = testFunctions.arr();
        str.log().log(NMsg.ofC("------------------"));
        str.log().log(NMsg.ofC("[%s] TEST FUNCTIONS (%s) : ", outputName(),testFunctionsArr.length));
        str.log().log(NMsg.ofC("------------------"));
        for (int i = 0; i < testFunctionsArr.length; i++) {
            str.log().log(NMsg.ofC("%s : %s", i, testFunctionsArr[i]));
        }
        Plot.title(solverType()+"-"+outputName()).plot(testFunctionsArr);
    }


}
