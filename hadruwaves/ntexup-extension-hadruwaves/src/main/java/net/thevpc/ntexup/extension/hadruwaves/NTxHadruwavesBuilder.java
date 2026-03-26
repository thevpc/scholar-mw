package net.thevpc.ntexup.extension.hadruwaves;

import net.thevpc.ntexup.api.document.node.NTxNode;
import net.thevpc.ntexup.api.engine.NTxNodeBuilderContext;
import net.thevpc.ntexup.api.extension.NTxNodeBuilder;
import net.thevpc.ntexup.api.parser.NTxAllArgumentReader;
import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.extension.mwsimulator.*;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NElementFormatter;
import net.thevpc.nuts.text.NContentType;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;


/**
 *
 */
public class NTxHadruwavesBuilder implements NTxNodeBuilder {

    @Override
    public void build(NTxNodeBuilderContext builderContext) {
        builderContext.id("hadruwaves-mom-solver")
                .parseParam()
                .matchesAny().end()
                .processChildren(this::processChildren)
                .withToElem(this::toElement)
                .renderComponent(this::renderMain)
        ;
    }

    protected NElement toElement(NTxNode item, boolean semantic, NTxNodeBuilderContext buildContext){
        return item.getRaw().format(NContentType.TSON, NElementFormatter.ofPretty().builder().removeComments().build());
    }
    public void processChildren(NTxAllArgumentReader info, NTxNodeBuilderContext buildContext) {
        //info.node().setUserObject("def", all);
    }

    public void renderMain(NTxRendererContext rendererContext) {
        NTxMwSimulationUtils.doRender(rendererContext,
                (name, args) -> {
                    MomStructure str = MomParser.createMomStructure(args);
                    if (str == null) {
                        return null;
                    }
                    return new MoMStrNTxSimulationPlan(name,rendererContext, str);
                }
        );
    }
}
