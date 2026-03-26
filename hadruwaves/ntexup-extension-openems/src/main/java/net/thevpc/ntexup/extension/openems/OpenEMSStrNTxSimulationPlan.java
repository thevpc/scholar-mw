package net.thevpc.ntexup.extension.openems;

import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationPlanImpl;
import net.thevpc.ntexup.extension.mwsimulator.NTxSolverRun;
import net.thevpc.ntexup.extension.openems.solvers.NTxHwS11NTxSolver;
import net.thevpc.nuts.io.NDigest;
import net.thevpc.nuts.log.NLogger;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.util.NNameFormat;

import java.nio.charset.StandardCharsets;

public class OpenEMSStrNTxSimulationPlan extends NTxSimulationPlanImpl {
    public OpenEMSStrNTxSimulationPlan(String name, NTxRendererContext rendererContext) {
        super(name, rendererContext);
    }

    @Override
    public NTxSolverRun createItem(String computeName, String solverName) {
        switch (NNameFormat.LOWER_KEBAB_CASE.format(solverName)) {
            case "s11":
            case "sparam":
            case "sparams":
            case "s-param":
            case "s-params": {
                return new NTxHwS11NTxSolver(computeName, solverName, this);
            }
        }
        return null;
    }

    @Override
    public String computeHash() {
        NDigest d = NDigest.of();
        d.addSource("OpenEMS".getBytes(StandardCharsets.UTF_8));
        for (NTxSolverRun item : items) {
            d.addSource(item.toElement().toString().getBytes(StandardCharsets.UTF_8));
        }
        return d.computeString();
    }
}
