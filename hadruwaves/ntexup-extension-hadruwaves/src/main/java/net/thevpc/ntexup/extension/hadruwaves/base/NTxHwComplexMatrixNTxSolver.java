package net.thevpc.ntexup.extension.hadruwaves.base;

import net.thevpc.ntexup.extension.hadruwaves.MoMStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationResult;
import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationResultFactory;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.time.NChronometer;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

import java.util.Arrays;
import java.util.List;

public abstract class NTxHwComplexMatrixNTxSolver extends NTxHwNTxSolver {

    public NTxHwComplexMatrixNTxSolver(MoMStrNTxSimulationPlan moMStrSimulationQuery, String computeName, String solverName, String solverType) {
        super(moMStrSimulationQuery,computeName, solverName, solverType);
    }

    @Override
    public List<NTxSimulationResult> execute() {
        MomStructure str = momStructure();
        NChronometer chronometer = NChronometer.of();
        log(NMsg.ofC("------------------"));
        log(NMsg.ofC("[%s] %s: ", outputName(), solverName()));
        log(NMsg.ofC("------------------"));
        ComplexMatrix matrix = matrix(str);
        for (int r = 0; r < matrix.getRowCount(); r++) {
            for (int c = 0;c < matrix.getColumnCount(); c++) {
                log(NMsg.ofC(" %s-%s[%s,%s]=%s", solverName(), outputName(), c,r, matrix.get(r, c)));
            }
        }
        if (plan().rendererContext().isAnimate()) {
            Plot.cd(fullPath()).title(NMsg.ofC("[%s] %s", outputName(), solverName()).toString())
                    .asMatrix()
                    .plot(matrix);
        }
        log(NMsg.ofC("[%s] %s Finished in %s : ", outputName(), solverName(),chronometer.stop()));

        return Arrays.asList(
                NTxSimulationResultFactory.createPlot2dCurve(outputName(), null, Arrays.asList(0.0))
        );
    }

    protected abstract ComplexMatrix matrix(MomStructure str);


}
