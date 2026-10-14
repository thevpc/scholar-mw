package net.thevpc.ntexup.extension.scuffem;

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

public class NTxScuffEMBuilder implements NTxNodeBuilder {

    @Override
    public void build(NTxNodeBuilderContext builderContext) {
        builderContext.id("scuff-solver")
                .alias("scuff", "scuff-em-solver", "scuff-rf-solver", "scuffem", "scuffSolver", "scuff-rf")
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
                        ScuffEMStrNTxSimulationPlan plan = new ScuffEMStrNTxSimulationPlan(id, name, rendererContext);
                        plan.modelInfo = ScuffEMParser.parse(args);
                        return plan;
                    }
                });
    }
}
