package net.thevpc.ntexup.extension.hadruwaves.solvers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.thevpc.ntexup.extension.hadruwaves.MoMStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.hadruwaves.base.NTxHwNopNTxSolver;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.geom.HGeometry;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.TestFunctions;

public class NTxHwTestFunctionsSolver extends NTxHwNopNTxSolver {

    public NTxHwTestFunctionsSolver(MoMStrNTxSimulationPlan moMStrSimulationQuery, String computeName, String solverName) {
        super(moMStrSimulationQuery, computeName, solverName,"test-functions");
    }

    @Override
    protected void nop() {
        MomStructure str = momStructure();
        TestFunctions testFunctions = str.testFunctions();
        DoubleToVector[] testFunctionsArr = testFunctions.arr();
        log(NMsg.ofC("------------------"));
        log(NMsg.ofC("[%s] TEST FUNCTIONS (%s) : ", outputName(),testFunctionsArr.length));
        log(NMsg.ofC("------------------"));
        for (int i = 0; i < testFunctionsArr.length; i++) {
            log(NMsg.ofC("%s : %s", i, testFunctionsArr[i]));
        }
        MoMStrNTxSimulationPlan p = (MoMStrNTxSimulationPlan)plan();
        List<Expr> a=new ArrayList<>();
        a.addAll(Arrays.asList(testFunctionsArr));
        for (HGeometry g : p.antennaGeometries) {
            a.add(Maths.expr(g));
        }
        if (plan().rendererContext().isAnimate()) {
            Plot.cd(fullPath()).title(fullName()).plot(a);
        }
    }


}
