package net.thevpc.ntexup.extension.hadruwaves.solvers;

import net.thevpc.ntexup.extension.hadruwaves.MoMStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.hadruwaves.base.NTxHwNopNTxSolver;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.mom.ModeFunctions;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

public class NTxHwScalarProductsSolver extends NTxHwNopNTxSolver {

    public NTxHwScalarProductsSolver(MoMStrNTxSimulationPlan moMStrSimulationQuery, String computeName, String solverName) {
        super(moMStrSimulationQuery, computeName, solverName,"test-mode-scalar-products");
    }

    @Override
    protected void nop() {
        MomStructure str = momStructure();
        ComplexMatrix testModeScalarProducts = str.getTestModeScalarProducts();
        ModeFunctions modes = str.modeFunctions();
        DoubleToVector[] testFunctionsArr = str.testFunctions().toArray();
        ModeInfo[] modesArr = modes.getModes();

        log(NMsg.ofC("------------------"));
        log(NMsg.ofC("[%s] SCALAR PRODUCT (fn=%s,gp=%s): ", outputName(),modesArr.length,testFunctionsArr.length));
        log(NMsg.ofC("------------------"));
        for (int pq = 0; pq < testFunctionsArr.length; pq++) {
            if (pq >= 10) {
                //just a snapshot!
                break;
            }
            for (int mn = 0; mn < modesArr.length; mn++) {
                if (mn >= 10) {
                    //just a snapshot!
                    break;
                }
                log(NMsg.ofC("<f" + mn + ",g" + pq + ">=<" + modesArr[mn].getMode() + ",g" + pq + "> = " + testModeScalarProducts.get(pq, mn)));
            }
        }
        if (plan().rendererContext().isAnimate()) {
            Plot.cd(fullPath()).title(fullName())
                    .asMatrix()
                    .plot(testModeScalarProducts);
        }
    }


}
