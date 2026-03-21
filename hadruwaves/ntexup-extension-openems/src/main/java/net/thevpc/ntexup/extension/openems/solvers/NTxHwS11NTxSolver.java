package net.thevpc.ntexup.extension.openems.solvers;

import net.thevpc.ntexup.extension.mwsimulator.*;
import net.thevpc.nuts.command.NExec;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.io.NPath;

import java.util.Arrays;
import java.util.List;

public class NTxHwS11NTxSolver extends NTxSolverRunImpl {
    public NTxHwS11NTxSolver(String computeName, String solverName, NTxSimulationPlan query) {
        super(computeName, solverName, "s-parameters", query);
    }

    @Override
    public List<NTxSimulationResult> execute() {
        NPath path = NPath.ofTempFile("openems.xml");
        NExec cmd = NExec.ofSystem("openEMS", "-f", path.toString());
        if (false) {
            cmd.run();
        }
        return Arrays.asList(
                NTxSimulationResultFactory.createPlot2dCurve(outputName(), null, Arrays.asList(0.0))
        );
    }

    @Override
    protected void addImpl(String paramName, NElement paramValue) {

    }
}
