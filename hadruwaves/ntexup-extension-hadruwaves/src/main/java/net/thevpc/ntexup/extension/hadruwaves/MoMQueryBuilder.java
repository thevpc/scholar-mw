package net.thevpc.ntexup.extension.hadruwaves;

import net.thevpc.ntexup.api.document.node.NTxNode;
import net.thevpc.ntexup.api.renderer.NTxNodeRendererContext;
import net.thevpc.ntexup.extension.hadruwaves.configurers.NTxHwFreqConfigurer;
import net.thevpc.ntexup.extension.hadruwaves.solvers.NTxHwS11Solver;
import net.thevpc.ntexup.extension.hadruwaves.solvers.NTxHwZinSolver;
import net.thevpc.ntexup.extension.mwsimulator.NTxMwSimulationUtils;
import net.thevpc.nuts.elem.NElements;
import net.thevpc.nuts.io.NDigest;

import java.nio.charset.StandardCharsets;

public class MoMQueryBuilder {
    public static MoMStrSimulationQuery resolveMoMStrSimulationQuery(NTxNodeRendererContext rendererContext) {
        NTxNode node = rendererContext.node();
        MoMStrSimulationQuery query = new MoMStrSimulationQuery();
        query.str = MomParser.createMomStructure(node, rendererContext);
        if (query.str == null) {
            return null;
        }

        NTxMwSimulationUtils.fillStrSimulationQuery(query, rendererContext);

        switch (query.compute) {
            case S11:{
                query.computer = new NTxHwS11Solver();
                break;
            }
            case ZIN:{
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

        query.hash =
                NDigest.of()
                        .addSource(query.str.toElement().toString().getBytes(StandardCharsets.UTF_8))
                        .addSource(NTxMwSimulationUtils.createDefaultQueryHashObject(query).build().toString().getBytes(StandardCharsets.UTF_8))
                        .computeString()
        ;
        return query;
    }

}
