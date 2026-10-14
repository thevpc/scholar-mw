package net.thevpc.ntexup.extension.mwsimulator;

import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.nuts.elem.NToElement;

public interface NTxSolverListener {

    default void onStart(NTxRendererContext rendererContext, NTxSimulationPlan plan){}

    default void onFinish(NTxRendererContext rendererContext, NTxSimulationPlan plan, boolean error, Throwable throwable){}
}
