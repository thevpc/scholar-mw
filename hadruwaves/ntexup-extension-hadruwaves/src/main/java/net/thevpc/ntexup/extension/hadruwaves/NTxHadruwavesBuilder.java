package net.thevpc.ntexup.extension.hadruwaves;

import net.thevpc.ntexup.api.document.elem2d.NTxBounds2;
import net.thevpc.ntexup.api.document.elem2d.NTxDouble2;
import net.thevpc.ntexup.api.document.elem2d.NTxInt2;
import net.thevpc.ntexup.api.document.node.NTxNode;
import net.thevpc.ntexup.api.engine.NTxNodeBuilderContext;
import net.thevpc.ntexup.api.eval.NTxValue;
import net.thevpc.ntexup.api.extension.NTxNodeBuilder;
import net.thevpc.ntexup.api.parser.NTxAllArgumentReader;
import net.thevpc.ntexup.api.renderer.NTxNodeRendererContext;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NElements;
import net.thevpc.nuts.elem.NObjectElement;
import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.nuts.io.NDigest;
import net.thevpc.nuts.util.*;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruwaves.Material;
import net.thevpc.scholar.hadruwaves.WallBorders;
import net.thevpc.scholar.hadruwaves.mom.*;
import net.thevpc.scholar.hadruwaves.mom.modes.BoxModeFunctions;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;


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


    private MoMStrSimulationQuery resolveMoMStrSimulationQuery(NTxNodeRendererContext rendererContext, NTxNodeBuilderContext builderContext) {
        NTxNode node = rendererContext.node();
        MoMStrSimulationQuery query = new MoMStrSimulationQuery();
        query.str = createMomStructure(node, rendererContext, builderContext);
        if (query.str == null) {
            return null;
        }

        query.sweep = rendererContext.evalExpression(node.getPropertyValue("sweep").orNull(), node).map(x -> deserializeSweep(x)).orNull();
        query.compute = rendererContext.evalExpression(node.getPropertyValue("compute").orNull(), node).flatMap(NElement::asStringValue).orNull();
        if (NBlankable.isBlank(query.compute)) {
            query.compute = "s11";
        }
        switch (NStringUtils.trim(query.compute)) {
            case "s11":
            case "sparameters":
            case "sparams": {
                query.computer = m -> m.sparameters().evalComplex();
                query.computeName = "S11";
                break;
            }
            case "zin":
            case "inputImpedance": {
                query.computer = m -> m.inputImpedance().evalComplex();
                query.computeName = "Zin";
                break;
            }
            default: {
                query.computer = m -> m.sparameters().evalComplex();
                query.computeName = "S11";
                break;
            }
        }

        if (query.sweep == null) {
            query.sweep = new Sweep();
            query.sweep.target = "nothing";
            query.sweep.rangeFrom = 0;
            query.sweep.rangeTo = 1;
            query.sweep.times = 1;
        }
        if (query.sweep.target == null) {
            query.sweep.target = "freq";
        }
        switch (NStringUtils.trim(query.sweep.target).toLowerCase()) {
            case "freq":
            case "frequency":
            case "frequencies": {
                query.sweep.target = "Frequency";
                query.applier = (m, a) -> query.str.setFrequency(((Number) a).doubleValue());
                break;
            }
            case "structure":
            case "nothing":
            case "none": {
                query.sweep.target = "Structure";
                query.applier = (m, a) -> query.str;
                break;
            }
            default: {
                query.applier = (m, a) -> query.str;
                break;
            }
        }

        NObjectElement hashObj = query.str.toElement().asObject().get().builder().add("sweep", NElements.of().toElement(query.sweep)).build();
        query.hash = NDigest.of().addSource(hashObj.toString().getBytes(StandardCharsets.UTF_8)).computeString();
        return query;
    }

    public void renderMain(NTxNodeRendererContext rendererContext, NTxNodeBuilderContext builderContext) {
        MoMStrSimulationQuery q = resolveMoMStrSimulationQuery(rendererContext, builderContext);
        NTxNode node = rendererContext.node();
        MomStructure str = createMomStructure(node, rendererContext, builderContext);
        if (str == null) {
            return;
        }
        NTxHadruwavesBuilderMomRunner t = rendererContext.engine().computeIfAbsent(NTxHadruwavesBuilderMomRunner.class.getName(), s -> new NTxHadruwavesBuilderMomRunner()).get();
        NTxHadruwavesBuilderMomRunner.RunningProcess r = t.add(() -> doSweep(q.computeName, q.sweep, a -> {
            q.applier.apply(str, (Number) a);
            return q.computer.apply(q.str);
        }), q.compute, q.hash);
        if (r.isDone()) {
            GoodResult<Number> result = (GoodResult) r.getResult();
            NElement plot2d = toPlot2d(result, q);
            rendererContext.renderDetachedNode(plot2d, NTxBounds2.ofFull());
        }
    }

    static class MoMStrSimulationQuery {
        MomStructure str;
        NFunction2<MomStructure, Number, Object> applier = null;
        NFunction<MomStructure, Object> computer = null;
        String hash;
        Sweep sweep;
        String compute;
        String computeName;
    }

    static class GoodResult<T> {
        String yName;
        String xName;
        List<Number> x;
        List<T> y;
    }


    <T> NElement toPlot2d(GoodResult<T> result, MoMStrSimulationQuery q) {
        NObjectElementBuilder plot2d = NElement.ofObjectBuilder("plot2d");
        if (q.sweep.times != null) {
            plot2d.addParam("x", NElement.ofNamedUplet("dtimes", NElement.ofNumber(q.sweep.rangeFrom), NElement.ofNumber(q.sweep.rangeTo), NElement.ofNumber(q.sweep.times)));
        } else {
            plot2d.addParam("x", NElement.ofNamedUplet("dsteps", NElement.ofNumber(q.sweep.rangeFrom), NElement.ofNumber(q.sweep.rangeTo), NElement.ofNumber(q.sweep.step)));
        }
        plot2d.add(
                NElement.ofObjectBuilder()
                        .name("curve")
                        .add("y", NElement.ofArray(result.y.map(x -> NElement.ofNumber((Number) x)).toArray(NElement[]::new)))
                        .build()
        );
        return plot2d.build();
    }

    private <T> GoodResult<T> doSweep(String computeName, Sweep finalSweep, Function<Object, T> fct) {
        GoodResult<T> gr = new GoodResult<>();
        gr.xName = finalSweep.target;
        gr.yName = computeName;
        gr.x = new ArrayList<>();
        gr.y = new ArrayList<>();
        if (finalSweep.step != null) {
            if (finalSweep.rangeFrom instanceof Double || finalSweep.rangeTo instanceof Double || finalSweep.step instanceof Double) {
                double[] dsteps = Maths.dsteps(finalSweep.rangeFrom.doubleValue(), finalSweep.rangeTo.doubleValue(), finalSweep.step.doubleValue());
                gr.x.addAll(Arrays.stream(dsteps).mapToObj(x -> x).collect(Collectors.toList()));
                for (double dstep : dsteps) {
                    gr.y.add(fct.apply(dstep));
                }
            } else {
                int[] isteps = Maths.isteps(finalSweep.rangeFrom.intValue(), finalSweep.rangeTo.intValue(), finalSweep.step.intValue());
                gr.x.addAll(Arrays.stream(isteps).mapToObj(x -> x).collect(Collectors.toList()));
                for (double dstep : isteps) {
                    gr.y.add(fct.apply(dstep));
                }
            }
        }
        return gr;
    }

    private MomStructure createMomStructure(NTxNode node, NTxNodeRendererContext rendererContext, NTxNodeBuilderContext builderContext) {
        //h will populate from node later
        MomStructure str = new MomStructure();
        String geometryId = rendererContext.evalExpression(node.getPropertyValue("geometry").orNull(), node).flatMap(NElement::asStringValue).orNull();
        if (geometryId == null) {
            return null;
        }
        NTxNode otherNode = rendererContext.findNodeByProperty("id", geometryId, node).orNull();
        if (otherNode == null || !Objects.equals(otherNode.type(), "scene3d")) {
            return null;
        }

        int modes = rendererContext.evalExpression(node.getPropertyValue("modes").orNull(), node).flatMap(NElement::asIntValue).orElse(1000);
        NObjectElement boundaries = rendererContext.evalExpression(node.getPropertyValue("boundaries").orNull(), node).flatMap(NElement::toObject).orNull();
        if (boundaries != null) {
            String lat = boundaries.getStringValue("lateral").orNull();
            str.setBorders(WallBorders.of(lat));
            BoxSpace bottom = deserializeBoxSpace(boundaries.get("bottom").orNull());
            if (bottom == null) {
                bottom = BoxSpace.shortCircuit(Material.PEC, 1E-10);
            }
            str.setFirstBoxSpace(bottom);
            BoxSpace top = deserializeBoxSpace(boundaries.get("top").orNull());
            if (top == null) {
                top = BoxSpace.shortCircuit(Material.PEC, 1E-10);
            }
            str.setSecondBoxSpace(top);
        }
        str.setModeFunctions(new BoxModeFunctions().setSize(modes));
        TestFunctionsBuilder b = TestFunctionsFactory.createBuilder();
        for (NTxNode child : otherNode.children()) {
            if("box".equals(child.type())){
                child.get
            }
            String material = rendererContext.evalExpression(child.getPropertyValue("material").orNull(), node).flatMap(NElement::asStringValue).orNull();
            if(!NBlankable.isBlank(material)){
                if(material.equalsIgnoreCase("pec")){

                }
            }
        }
        str.setTestFunctions(b.buildRooftops());
        return str;
    }

    static class Sweep {
        Number rangeFrom;
        Number rangeTo;
        Number step;
        Integer times;
        String target;
    }

    private Sweep deserializeSweep(NElement e) {
        if (e == null) {
            return null;
        }
        if (e.isNamedListContainer()) {
            Sweep s = new Sweep();
            s.target = e.asNamed().get().name().orElse("");
            for (NElement child : e.asListContainer().get().children()) {
                if (child.isNamedPair()) {
                    switch (child.asNamed().get().name().get()) {
                        case "range": {
                            NOptional<NTxInt2> d = NTxValue.of(child.asPair().get().value()).asInt2();
                            if (!d.isPresent()) {
                                NOptional<NTxDouble2> d2 = NTxValue.of(child.asPair().get().value()).asDouble2();
                                if (!d2.isPresent()) {
                                    return null;
                                }
                                s.rangeFrom = d2.get().getX();
                                s.rangeTo = d2.get().getY();
                            } else {
                                s.rangeFrom = d.get().getX();
                                s.rangeTo = d.get().getY();
                            }
                            break;
                        }
                        case "steps": {
                            NOptional<Integer> d = NTxValue.of(child.asPair().get().value()).asInt();
                            if (!d.isPresent()) {
                                NOptional<Double> d2 = NTxValue.of(child.asPair().get().value()).asDouble();
                                if (!d2.isPresent()) {
                                    return null;
                                }
                                s.step = d2.get();
                            } else {
                                s.step = d.get();
                            }
                            break;
                        }
                        case "times": {
                            NOptional<Integer> d = NTxValue.of(child.asPair().get().value()).asInt();
                            if (!d.isPresent()) {
                                return null;
                            }
                            s.times = d.get();
                            break;
                        }
                    }
                }
            }
            return s;
        }
        return null;
    }


    private BoxSpace deserializeBoxSpace(NElement e) {
        if (e == null) {
            return null;
        }
        if (e.isAnyString()) {
            switch (e.asStringValue().orElse("").toLowerCase()) {
                case "matchedload":
                case "matched-load":
                case "matched_load": {
                    return BoxSpace.matchedLoad();
                }
                case "nothing":
                case "none": {
                    return BoxSpace.nothing();
                }
                case "open": {
                    return BoxSpace.openCircuit(Material.VACUUM, 1E-10);
                }
                default: {
                    return BoxSpace.nothing();
                }

            }
            if (e.isNamedListContainer() && e.isListContainer()) {
                String name = e.asNamed().get().name().orNull();
                List<NElement> children = e.asListContainer().get().children();
                switch (NStringUtils.trim(name).toLowerCase()) {
                    case "matchedload":
                    case "matched-load":
                    case "matched_load": {
                        for (NElement child : children) {
                            if (child.isAnyString()) {
                                switch (child.asStringValue().orElse("").toLowerCase()) {
                                    case "pec": {
                                        return BoxSpace.matchedLoad(Material.PEC);
                                    }
                                    case "vacuum": {
                                        return BoxSpace.matchedLoad(Material.VACUUM);
                                    }
                                }
                                return BoxSpace.matchedLoad(Material.VACUUM);
                            }
                        }
                        return BoxSpace.matchedLoad();
                    }
                    case "nothing":
                    case "none": {
                        return BoxSpace.nothing();
                    }
                    case "open": {
                        return BoxSpace.openCircuit(Material.VACUUM, 1E-10);
                    }
                    default: {
                        return BoxSpace.nothing();
                    }

                }
            }
        }
        return BoxSpace.nothing();
    }

}
