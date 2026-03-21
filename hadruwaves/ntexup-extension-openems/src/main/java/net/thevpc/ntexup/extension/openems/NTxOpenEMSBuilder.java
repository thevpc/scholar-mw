package net.thevpc.ntexup.extension.openems;

import net.thevpc.ntexup.api.engine.NTxNodeBuilderContext;
import net.thevpc.ntexup.api.eval.NTxFunctionCallContext;
import net.thevpc.ntexup.api.extension.NTxNodeBuilder;
import net.thevpc.ntexup.api.parser.NTxAllArgumentReader;
import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.extension.mwsimulator.*;


/**
 *
 */
public class NTxOpenEMSBuilder implements NTxNodeBuilder {

    @Override
    public void build(NTxNodeBuilderContext builderContext) {
        builderContext.id("openems")
                .parseParam()
                .matchesAny().end()
                .processChildren(this::processChildren)
                .renderComponent(this::renderMain)
        ;
    }

    public void processChildren(NTxAllArgumentReader info, NTxNodeBuilderContext buildContext) {
        //info.node().setUserObject("def", all);
    }

    public void renderMain(NTxRendererContext rendererContext) {
        NTxMwSimulationUtils.doRender(rendererContext,
                new NTxStrSimulationQueryFactory() {
                    @Override
                    public NTxSimulationPlan newInstance(String name, NTxFunctionCallContext args) {
                        return new OpenEMSStrNTxSimulationPlan(name,rendererContext.log());
                    }
                });
        ;
    }
}
