package net.thevpc.ntexup.extension.openems;

import net.thevpc.ntexup.api.engine.NTxNodeBuilderContext;
import net.thevpc.ntexup.api.extension.NTxNodeBuilder;
import net.thevpc.ntexup.api.parser.NTxAllArgumentReader;
import net.thevpc.ntexup.api.renderer.NTxNodeRendererContext;
import net.thevpc.ntexup.extension.mwsimulator.NTxMwSimulationUtils;


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

    public void renderMain(NTxNodeRendererContext rendererContext) {
        OpenEMSStrSimulationQuery q = OpenEMSStrSimulationQueryBuilder.resolveQuery(rendererContext);
        NTxMwSimulationUtils.doRender(rendererContext,q);
    }
}
