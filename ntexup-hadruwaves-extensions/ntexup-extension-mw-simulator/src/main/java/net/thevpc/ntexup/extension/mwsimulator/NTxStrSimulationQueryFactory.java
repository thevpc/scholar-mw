package net.thevpc.ntexup.extension.mwsimulator;

import net.thevpc.ntexup.api.eval.NTxFunctionCallContext;

public interface NTxStrSimulationQueryFactory {
    NTxSimulationPlan newInstance(String id,String name, NTxFunctionCallContext args);
}
