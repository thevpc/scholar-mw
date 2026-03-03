package net.thevpc.ntexup.extension.mwsimulator;

import net.thevpc.ntexup.api.document.elem2d.NTxBounds2D;
import net.thevpc.ntexup.api.document.elem2d.NTxDouble2;
import net.thevpc.ntexup.api.document.elem2d.NTxInt2;
import net.thevpc.ntexup.api.document.node.NTxNode;
import net.thevpc.ntexup.api.eval.NTxValue;
import net.thevpc.ntexup.api.renderer.NTxNodeRendererContext;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NElements;
import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.nuts.util.NOptional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NTxMwSimulationUtils {

    public static void fillStrSimulationQuery(NTxStrSimulationQuery query, NTxNodeRendererContext rendererContext) {
        NTxNode node = rendererContext.node();
        query.sweep = rendererContext.evalExpression(node.getPropertyValue("sweep").orNull(), node).map(x -> NTxMwSimulationUtils.deserializeSweep(x)).orNull();
        query.compute = NTxMwResultType.parse(rendererContext.evalExpression(node.getPropertyValue("compute").orNull(), node).flatMap(NElement::asStringValue).orNull()).orElse(NTxMwResultType.S11);
    }

    public static NTxSweep deserializeSweep(NElement e) {
        if (e == null) {
            return null;
        }
        if (e.isNamedListContainer()) {
            NTxSweep s = new NTxSweep();
            s.target = NTxSweepTarget.parse(e.asNamed().get().name().orElse("")).orElse(NTxSweepTarget.FREQ);
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


    public static <T> GoodResult<T> doSweep(String computeName, NTxSweep finalSweep, Function<Object, T> fct) {
        GoodResult<T> gr = new GoodResult<>();
        gr.xName = finalSweep.target.name();
        gr.yName = computeName;
        gr.x = new ArrayList<>();
        gr.y = new ArrayList<>();
        if (finalSweep.step != null) {
            if (finalSweep.rangeFrom instanceof Double || finalSweep.rangeTo instanceof Double || finalSweep.step instanceof Double) {
                double[] dsteps = dsteps(finalSweep.rangeFrom.doubleValue(), finalSweep.rangeTo.doubleValue(), finalSweep.step.doubleValue());
                gr.x.addAll(Arrays.stream(dsteps).mapToObj(x -> x).collect(Collectors.toList()));
                for (double dstep : dsteps) {
                    gr.y.add(fct.apply(dstep));
                }
            } else {
                int[] isteps = isteps(finalSweep.rangeFrom.intValue(), finalSweep.rangeTo.intValue(), finalSweep.step.intValue());
                gr.x.addAll(Arrays.stream(isteps).mapToObj(x -> x).collect(Collectors.toList()));
                for (double dstep : isteps) {
                    gr.y.add(fct.apply(dstep));
                }
            }
        }
        return gr;
    }

    public static int[] isteps(int min, int max, int step) {
        if (max < min) {
            return new int[0];
        }
        int times = Math.abs((max - min) / step) + 1;
        int[] d = new int[times];
        for (int i = 0; i < d.length; i++) {
            d[i] = min + i * step;
        }
        return d;
    }

    public static double[] dsteps(double min, double max, double step) {
        if (step >= 0) {
            if (max < min) {
                return new double[0];
            }
            int times = (int) Math.abs((max - min) / step) + 1;
            double[] d = new double[times];
            for (int i = 0; i < d.length; i++) {
                d[i] = min + i * step;
            }
            return d;
        } else {
            if (min < max) {
                return new double[0];
            }
            int times = (int) Math.abs((max - min) / step) + 1;
            double[] d = new double[times];
            for (int i = 0; i < d.length; i++) {
                d[i] = min + i * step;
            }
            return d;
        }
    }


    public static void doRender(NTxNodeRendererContext rendererContext, NTxStrSimulationQuery q) {
        if (q == null) {
            return;
        }
        NTxSimulationRunner t = rendererContext.engine().computeIfAbsent(NTxSimulationRunner.class.getName(), s -> new NTxSimulationRunner()).get();
        NTxSimulationRunningProcess r = t.add(() -> NTxMwSimulationUtils.doSweep(q.computeName, q.sweep, a -> {
            q.applier.configure(q, (Number) a);
            return q.computer.solve(q);
        }), q);
        if (r.isDone()) {
            GoodResult<Number> result = (GoodResult) r.getResult();
            NElement plot2d = toPlot2d(result, q);
            rendererContext.renderDetachedNode(plot2d, NTxBounds2D.ofFull());
        }
    }

    public static NObjectElementBuilder createDefaultQueryHashObject(NTxStrSimulationQuery query) {
        NObjectElementBuilder b = NElement.ofObjectBuilder();
        b.add("resultType", query.compute.name());
        b.add("sweep", NElements.of().toElement(query.sweep));
        return b;
    }


    static <T> NElement toPlot2d(GoodResult<T> result, NTxStrSimulationQuery q) {
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
}
