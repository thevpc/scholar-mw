package net.thevpc.ntexup.extension.hadruwaves.base;

import net.thevpc.ntexup.extension.hadruwaves.MoMStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationResult;
import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationResultFactory;
import net.thevpc.ntexup.extension.mwsimulator.NTxSolverRunImpl;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.time.NChronometer;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

import java.util.Arrays;
import java.util.List;

public abstract class NTxHwComplexMatrixNTxSolver extends NTxSolverRunImpl {

    public NTxHwComplexMatrixNTxSolver(MoMStrNTxSimulationPlan moMStrSimulationQuery, String computeName, String solverName, String solverType) {
        super(computeName, solverName, solverType,moMStrSimulationQuery);
    }

    @Override
    public NElement toElement() {
        return NElement.ofNamedUplet(outputName(),NElement.ofPair("solver",solverType()));
    }

    @Override
    public List<NTxSimulationResult> execute() {
        MomStructure str = ((MoMStrNTxSimulationPlan) query()).str;
        NChronometer chronometer = NChronometer.startNow();
        str.log().log(NMsg.ofC("------------------"));
        str.log().log(NMsg.ofC("[%s] %s: ", outputName(), solverName()));
        str.log().log(NMsg.ofC("------------------"));
        ComplexMatrix matrix = matrix(str);
        for (int r = 0; r < matrix.getRowCount(); r++) {
            for (int c = 0;c < matrix.getColumnCount(); c++) {
                str.log().log(NMsg.ofC(" %s-%s[%s,%s]=%s", solverName(), outputName(), c,r, matrix.get(r, c)));
            }
        }
        Plot.title(NMsg.ofC("[%s] %s", outputName(), solverName()).toString()).plot(matrix);
        str.log().log(NMsg.ofC("[%s] %s Finished in %s : ", outputName(), solverName(),chronometer.stop()));
        return Arrays.asList(
                NTxSimulationResultFactory.createPlot2dCurve(outputName(), null, Arrays.asList(0.0))
        );
    }

    protected abstract ComplexMatrix matrix(MomStructure str);


}
