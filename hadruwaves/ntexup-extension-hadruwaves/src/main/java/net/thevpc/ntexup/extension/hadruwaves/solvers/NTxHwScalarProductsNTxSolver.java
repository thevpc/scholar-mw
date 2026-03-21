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

public class NTxHwScalarProductsNTxSolver extends NTxHwNopNTxSolver {

    public NTxHwScalarProductsNTxSolver(MoMStrNTxSimulationPlan moMStrSimulationQuery, String computeName, String solverName) {
        super(moMStrSimulationQuery, computeName, solverName,"test-mode-scalar-products");
    }

    @Override
    protected void nop(MomStructure str) {
        ComplexMatrix testModeScalarProducts = str.getTestModeScalarProducts();
        ModeFunctions modes = str.modeFunctions();
        DoubleToVector[] testFunctionsArr = str.testFunctions().arr();
        ModeInfo[] modesArr = modes.getModes();

        str.log().log(NMsg.ofC("------------------"));
        str.log().log(NMsg.ofC("[%s] SCALAR PRODUCT (fn=%s,gp=%s): ", outputName(),modesArr.length,testFunctionsArr.length));
        str.log().log(NMsg.ofC("------------------"));
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
                str.log().log(NMsg.ofC("<f" + mn + ",g" + pq + ">=<" + modesArr[mn].getMode() + ",g" + pq + "> = " + testModeScalarProducts.get(pq, mn)));
            }
        }
        Plot.title(solverType()+"-"+outputName()).plot(testModeScalarProducts);
    }


}
