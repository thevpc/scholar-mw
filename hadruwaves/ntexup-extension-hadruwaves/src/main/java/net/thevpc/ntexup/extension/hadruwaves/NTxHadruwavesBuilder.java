package net.thevpc.ntexup.extension.hadruwaves;

import net.thevpc.ntexup.api.engine.NTxNodeBuilderContext;
import net.thevpc.ntexup.api.extension.NTxNodeBuilder;
import net.thevpc.ntexup.api.parser.NTxAllArgumentReader;
import net.thevpc.ntexup.api.renderer.NTxNodeRendererContext;
import net.thevpc.ntexup.extension.mwsimulator.*;
import net.thevpc.nuts.util.*;
import net.thevpc.scholar.hadruwaves.mom.*;


/**
 *
 */
public class NTxHadruwavesBuilder implements NTxNodeBuilder {

    @Override
    public void build(NTxNodeBuilderContext builderContext) {
        builderContext.id("hadruwaves")
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
        MoMStrSimulationQuery q = MoMQueryBuilder.resolveMoMStrSimulationQuery(rendererContext);
        NTxMwSimulationUtils.doRender(rendererContext,q);
    }
}
