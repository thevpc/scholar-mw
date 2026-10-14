package net.thevpc.ntexup.extension.hadruwaves.analytical;

import net.thevpc.ntexup.api.document.style.NTxPropName;
import net.thevpc.ntexup.api.engine.NTxNodeBuilderContext;
import net.thevpc.ntexup.api.eval.NTxFunctionCallContext;
import net.thevpc.ntexup.api.extension.NTxNodeBuilder;
import net.thevpc.ntexup.api.parser.NTxAllArgumentReader;
import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.extension.mwsimulator.NTxMwSimulationUtils;
import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationPlan;
import net.thevpc.ntexup.extension.mwsimulator.NTxStrSimulationQueryFactory;
import net.thevpc.nuts.elem.NElement;

public class NTxHadruwavesAnalyticalBuilder implements NTxNodeBuilder {

    @Override
    public void build(NTxNodeBuilderContext builderContext) {
        builderContext.id("hadruwaves-analytical-solver")
                .alias("analytical-solver", "hadruwavesAnalytical", "analyticalSolver", "analytical")
                .initializeNodeAction((node, ctx) -> node.setProperty(NTxPropName.LAYOUT, NElement.ofString("none")))
                .parseParam()
                .matchesAny().end()
                .processChildren(this::processChildren)
                .renderComponent(this::renderMain);
    }

    public void processChildren(NTxAllArgumentReader info, NTxNodeBuilderContext buildContext) {
    }

    public void renderMain(NTxRendererContext rendererContext) {
        NTxMwSimulationUtils.doRender(rendererContext,
                new NTxStrSimulationQueryFactory() {
                    @Override
                    public NTxSimulationPlan newInstance(String id, String name, NTxFunctionCallContext args) {
                        AnalyticalStrNTxSimulationPlan plan = new AnalyticalStrNTxSimulationPlan(id, name, rendererContext);
                        plan.modelInfo = AnalyticalParser.parse(args);
                        return plan;
                    }
                });
    }
}
