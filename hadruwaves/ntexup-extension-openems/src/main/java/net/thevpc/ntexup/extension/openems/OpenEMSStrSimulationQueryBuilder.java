package net.thevpc.ntexup.extension.openems;

import net.thevpc.ntexup.api.document.node.NTxNode;
import net.thevpc.ntexup.api.renderer.NTxNodeRendererContext;
import net.thevpc.ntexup.extension.mwsimulator.NTxMwSimulationUtils;
import net.thevpc.ntexup.extension.openems.configurers.NTxHwFreqConfigurer;
import net.thevpc.ntexup.extension.openems.solvers.NTxHwS11Solver;
import net.thevpc.ntexup.extension.openems.solvers.NTxHwZinSolver;
import net.thevpc.nuts.io.NDigest;

import java.nio.charset.StandardCharsets;

public class OpenEMSStrSimulationQueryBuilder {
    static OpenEMSStrSimulationQuery resolveQuery(NTxNodeRendererContext rendererContext) {
        OpenEMSStrSimulationQuery query = new OpenEMSStrSimulationQuery();

        NTxNode node = rendererContext.node();

        //parse more

        NTxMwSimulationUtils.fillStrSimulationQuery(query, rendererContext);

        switch (query.compute) {
            case S11: {
                query.computer = new NTxHwS11Solver();
                break;
            }
            case ZIN: {
                query.computer = new NTxHwZinSolver();
                break;
            }
        }

        if (query.sweep != null) {
            switch (query.sweep.target) {
                case FREQ: {
                    query.applier = new NTxHwFreqConfigurer();
                    break;
                }
            }
        }

        query.hash = NDigest.of()
//                .addSource(NPath.of(query.configurationPath))
                .addSource(NTxMwSimulationUtils.createDefaultQueryHashObject(query).build().toString().getBytes(StandardCharsets.UTF_8))
                .computeString();
        return query;
    }
}
