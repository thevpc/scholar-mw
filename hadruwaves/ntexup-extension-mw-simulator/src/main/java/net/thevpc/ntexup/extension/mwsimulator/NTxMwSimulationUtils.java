package net.thevpc.ntexup.extension.mwsimulator;

import net.thevpc.ntexup.api.document.elem2d.NTxBounds2D;
import net.thevpc.ntexup.api.document.node.NTxNode;
import net.thevpc.ntexup.api.document.style.NTxProp;
import net.thevpc.ntexup.api.eval.NTxFunctionArg;
import net.thevpc.ntexup.api.eval.NTxFunctionCallContext;
import net.thevpc.ntexup.api.eval.NTxResolutionContext;
import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.api.util.NTxUtils;
import net.thevpc.ntexup.lib.geometry3d.NTxNumberElement3;
import net.thevpc.ntexup.lib.geometry3d.impl.NTx3DUtils;
import net.thevpc.nuts.elem.*;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.util.NBlankable;
import net.thevpc.nuts.util.NNameFormat;
import net.thevpc.nuts.util.NOptional;
import net.thevpc.nuts.util.NStringUtils;

import java.util.*;

public class NTxMwSimulationUtils {


    public static NOptional<NTxNumberElement3> findSceneSize(NTxNode scene3D, NTxResolutionContext context) {
        NOptional<NTxProp> sceneRealSizeP = scene3D.getProperty("real-size");
        if (!sceneRealSizeP.isPresent()) {
            context.log().log(NMsg.ofC("missing scene real-size in %s", scene3D).asError());
            return null;
        }
        NTxNumberElement3 sceneSize = NTx3DUtils.resolveSize3D(sceneRealSizeP.get().getValue(), context);
        if (sceneSize == null) {
            return NOptional.<NTxNumberElement3>ofError(NMsg.ofC("missing scene real-size in %s", scene3D).asError()).withDefault(() -> NTxNumberElement3.ofOne());
        }
        return NOptional.of(sceneSize);
    }

    public static NOptional<NTxNumberElement3> findScenePosition(NTxNode scene3D, NTxResolutionContext context) {
        NOptional<NTxProp> sceneRealSizeP = scene3D.getProperty("real-position");
        if (!sceneRealSizeP.isPresent()) {
            context.log().log(NMsg.ofC("missing scene real-position in %s", scene3D).asError());
            return null;
        }
        NTxNumberElement3 sceneSize = NTx3DUtils.resolveSize3D(sceneRealSizeP.get().getValue(), context);
        if (sceneSize == null) {
            return NOptional.<NTxNumberElement3>ofError(NMsg.ofC("missing scene real-position in %s", scene3D).asError())
                    .withDefault(() -> NTxNumberElement3.ofZero());
        }
        return NOptional.of(sceneSize);
    }


    public static NElement doRender(NTxRendererContext rendererContext, NTxStrSimulationQueryFactory ctx) {
        String nodeName = NStringUtils.trimToNull(rendererContext.node().getName());
        if (NBlankable.isBlank(nodeName)) {
            nodeName = "hw_" + UUID.randomUUID().toString().replace("-", "");
        }

        NElement raw = rendererContext.node().getRaw();
        if (!raw.isNamedUplet()) {
            rendererContext.log().log(NMsg.ofC("unable to resolveQueries from node : %s", rendererContext.node()));
            return null;
        }
        NUpletElement u = raw.asUplet().get();
        NTxFunctionCallContext args = rendererContext.engine().createFunctionArgs(
                u.name().get(),
                u.params().toArray(new NElement[0]),
                rendererContext
        );
        NTxSimulationPlan query = ctx.newInstance(nodeName, args);
        if (query == null) {
            return null;
        }
        for (NTxFunctionArg arg : args.args()) {
            NElement a = arg.eval();
            if (a.isNamedPair()) {
                NPairElement np = a.asNamedPair().get();
                switch (NTxUtils.uid(np.key().asStringValue().orElse(""))) {
                    case "results": {
                        NOptional<NElement> vv = args.scopedContext().evalExpression(np.value());
                        if (vv.isPresent()) {
                            NElement nElement = vv.get();
                            if (nElement.isListContainer()) {
                                for (NElement child : nElement.asListContainer().get().children()) {
                                    String solverName = null;
                                    String computeName = null;
                                    List<NElement> body = null;
                                    if (child.isNamedPair()) {
                                        NPairElement cnp = child.asNamedPair().get();
                                        computeName = cnp.key().asStringValue().get();
                                        NElement element = cnp.value();
                                        if (element.isListContainer() && element.isNamed()) {
                                            solverName = element.asNamed().get().name().get();
                                            body = element.asListContainer().get().children();
                                        } else if (element.isName()) {
                                            solverName = element.asStringValue().get();
                                        }
                                    } else if (child.isNamedListContainer()) {
                                        computeName = child.asNamed().get().name().get();
                                        solverName = child.asNamed().get().name().get();
                                        body = child.asListContainer().get().children();
                                    } else if (child.isAnyStringOrName()) {
                                        solverName = child.asStringValue().get();
                                        computeName = solverName;
                                    } else {
                                        args.scopedContext().log().log(NMsg.ofC("unsupported result %s", child));
                                    }
                                    if (solverName != null) {
                                        NTxSolverRun item = query.add(computeName, solverName);
                                        if (item != null) {
                                            if (body != null) {
                                                for (NElement e : body) {
                                                    if (e.isNamedPair()) {
                                                        NPairElement p = e.asPair().get();
                                                        String n = p.key().asStringValue().get();
                                                        item.add(NTxUtils.uid(n), p.value());
                                                    }
                                                }
                                            }
                                            item.compile();
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        NTxSimulationRunner t = rendererContext.engine().computeIfAbsent(NTxSimulationRunner.class.getName(), s -> new NTxSimulationRunner()).get();
        query.compile();
        NTxSimulationRunningProcess r = t.add(() -> {
            List<NTxSimulationResult> allResults = new ArrayList<>();
            for (NTxSolverRun item : query.runs()) {
                List<NTxSimulationResult> results = item.execute();
                if (results != null) {
                    for (NTxSimulationResult result : results) {
                        allResults.add(result);
                        processResult(result, rendererContext);
                    }
                }
            }
            return new NTxSimulationResultsImpl(allResults);
        }, query);

        return NElement.ofName("NTxSimulationRunningProcess")
                .builder()
                .addAnnotation("@reference", NElement.ofPair("scope", NElement.ofString("mw-simulation")), NElement.ofPair("id", r.id()))
                .build();
    }

    public static void processResult(NTxSimulationResult result, NTxRendererContext rendererContext) {
        NElement element = result.toPlotElement();
        if (element != null && !element.isNull()) {
            if (rendererContext instanceof NTxRendererContext) {
                rendererContext.renderDetachedNode(element, NTxBounds2D.ofFull());
            }
        }
    }

    public static boolean isSimulationNode(NTxNode child, String name) {
        return Arrays.stream(child.getStyleClasses()).anyMatch(styleClass -> styleClass.equalsIgnoreCase(name))
                || NNameFormat.equalsIgnoreFormat(child.getName(), name);
    }


//    public static NObjectElementBuilder createDefaultQueryHashObject(NTxStrSimulationQuery query) {
//        NObjectElementBuilder b = NElement.ofObjectBuilder();
//        b.add("resultType", query.solver.name());
//        b.add("sweep", NElements.of().toElement(query.sweep));
//        return b;
//    }


    public static NElement toPlot2dCurve(NTxSweep sweep, List<Number> numbers) {
        NObjectElementBuilder plot2d = NElement.ofObjectBuilder("plot2d");
        if (sweep == null) {
            sweep = new NTxSweep();
            sweep.count = 1;
            sweep.rangeFrom = 0;
            sweep.rangeTo = 0;
        }
        if (sweep.count != null) {
            plot2d.addParam("x", NElement.ofNamedUplet("dtimes", NElement.ofNumber(sweep.rangeFrom), NElement.ofNumber(sweep.rangeTo), NElement.ofNumber(sweep.count)));
        } else {
            plot2d.addParam("x", NElement.ofNamedUplet("dsteps", NElement.ofNumber(sweep.rangeFrom), NElement.ofNumber(sweep.rangeTo), NElement.ofNumber(sweep.step)));
        }
        if (numbers == null || numbers.isEmpty()) {
            numbers = new ArrayList<>(Collections.singletonList(0.0));
        }
        plot2d.add(
                NElement.ofObjectBuilder()
                        .name("curve")
                        .add("y", NElement.ofArray(numbers.stream().map(x -> NElement.ofNumber(x)).toArray(NElement[]::new)))
                        .build()
        );
        return plot2d.build();
    }

    public static String findSceneGeometryId(NTxFunctionCallContext args) {
        for (NTxFunctionArg arg : args.args()) {
            NElement a = arg.eval();
            if (a.isNamedPair()) {
                NPairElement np = a.asNamedPair().get();
                switch (NTxUtils.uid(np.key().asStringValue().orElse(""))) {
                    case "geometry": {
                        return args.scopedContext().evalExpression(np.value()).flatMap(NElement::asStringValue).orNull();
                    }
                }
            }
        }
        return null;
    }
}
