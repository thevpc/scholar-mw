package net.thevpc.ntexup.extension.openems.solvers;

import net.thevpc.ntexup.api.eval.NTxObj;
import net.thevpc.ntexup.api.eval.NTxObjFromMap;
import net.thevpc.ntexup.api.eval.NTxObjs;
import net.thevpc.ntexup.api.util.NTxNumberUtils;
import net.thevpc.ntexup.extension.mwsimulator.*;
import net.thevpc.ntexup.extension.openems.OpenEMSStrNTxSimulationPlan;
import net.thevpc.nuts.elem.NArrayElement;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NElements;
import net.thevpc.nuts.math.NDoubleComplex;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.text.NTextFormat;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadruplot.Plot;

import java.util.*;
import java.util.stream.Collectors;

public abstract class NTxOpenEMSComplexNTxSolver extends NTxSolverRunImpl {
    private NTxSweep sweep;
    private NTxSweepTarget sweepParam;

    public NTxOpenEMSComplexNTxSolver(String computeName, String solverName, String solverType, NTxSimulationPlan plan) {
        super(computeName, solverName, solverType, plan);
    }

    @Override
    public void addImpl(String paramName, NElement paramValue) {
        switch (paramName) {
            case "freq":
            case "frequency":
            case "frequencies": {
                sweepParam = NTxSweepTarget.FREQ;
                Number singleFreq = asNumber(paramValue);
                if (singleFreq != null) {
                    sweep = new NTxSweep();
                    sweep.rangeFrom = singleFreq;
                    sweep.rangeTo = singleFreq;
                    sweep.count = 1;
                } else {
                    sweep = NTxSweep.parse(paramValue).orNull();
                }
                break;
            }
        }
    }

    private static Number asNumber(NElement e) {
        if (e == null || e.isNull()) {
            return null;
        }
        return e.asNumber()
                .flatMap(x -> NTxNumberUtils.toSIUnit(x).get().asNumberValue())
                .orNull();
    }

    @Override
    public NElement toElement() {
        net.thevpc.nuts.elem.NObjectElementBuilder b = net.thevpc.nuts.elem.NElement.ofObjectBuilder()
                .name(outputName())
                .set("solver", solverType());
        if (sweepParam != null) {
            b.set("param", sweepParam.name());
            b.set("sweep", sweep.toElement());
        }
        return b.build();
    }

    @Override
    public List<NTxSimulationResult> execute() {
        OpenEMSStrNTxSimulationPlan plan = (OpenEMSStrNTxSimulationPlan) plan();
        NTxChronometer chronometer = NTxChronometer.of();
        List<Number> complexValues = new ArrayList<>();
        Number[] x = new Number[0];

        NTxSweep activeSweep = sweep;
        if (activeSweep == null && plan.modelInfo != null && plan.modelInfo.frequency > 0) {
            activeSweep = new NTxSweep();
            activeSweep.rangeFrom = plan.modelInfo.frequency;
            activeSweep.rangeTo = plan.modelInfo.frequency;
            activeSweep.count = 1;
        }

        if (activeSweep != null) {
            NTextFormat<Number> ff = NTextFormat.ofFrequency("");
            plan.rendererContext().log(NMsg.ofC("------------------"));
            plan.rendererContext().log(NMsg.ofC("[%s] %s (freq over %s): ", outputName(), solverName(), activeSweep));
            plan.rendererContext().log(NMsg.ofC("------------------"));
            double[] dv = activeSweep.doubleValues();
            if (dv == null || dv.length == 0) {
                dv = new double[]{activeSweep.rangeFrom != null ? activeSweep.rangeFrom.doubleValue() : (plan.modelInfo != null ? plan.modelInfo.frequency : 2.4e9)};
            }
            for (double fr : dv) {
                plan.rendererContext().log(NMsg.ofC("use freq %s", ff.toText(fr)));
                Complex c = evalComplex(plan, fr);
                complexValues.add(NDoubleComplex.of(c.getReal(), c.getImag()).numberValue());
            }
            x = Arrays.stream(dv).boxed().toArray(Number[]::new);
        } else {
            plan.rendererContext().log(NMsg.ofC("------------------"));
            plan.rendererContext().log(NMsg.ofC("[%s] %s (single value): ", outputName(), solverName()));
            plan.rendererContext().log(NMsg.ofC("------------------"));
            double fr = plan.modelInfo != null && plan.modelInfo.frequency > 0 ? plan.modelInfo.frequency : 2.4e9;
            Complex c = evalComplex(plan, fr);
            complexValues.add(
                    NDoubleComplex.of(c.getReal(), c.getImag()).numberValue()
            );
            x = new Number[]{fr};
        }

        Number[] y = complexValues.toArray(new Number[0]);
        NArrayElement ye = NElement.ofArrayBuilder()
                .addAll(
                        Arrays.stream(y).map(xx -> {
                            if (xx instanceof Complex) {
                                return NElement.ofDoubleComplex(((Complex) xx).getReal(), ((Complex) xx).getImag());
                            }
                            return NElements.of().toElement(xx);
                        }).collect(Collectors.toList())
                ).build();

        NArrayElement yeAbs = NElement.ofArrayBuilder()
                .addAll(
                        Arrays.stream(y).map(xx -> {
                            double val = 0;
                            if (xx instanceof NDoubleComplex) {
                                val = ((NDoubleComplex) xx).absDouble();
                            } else if (xx instanceof Complex) {
                                val = ((Complex) xx).absDouble();
                            } else if (xx != null) {
                                val = xx.doubleValue();
                            }
                            return NElement.ofDouble(val);
                        }).collect(Collectors.toList())
                ).build();

        NArrayElement yeDb = NElement.ofArrayBuilder()
                .addAll(
                        Arrays.stream(y).map(xx -> {
                            double val = 0;
                            if (xx instanceof NDoubleComplex) {
                                val = ((NDoubleComplex) xx).absDouble();
                            } else if (xx instanceof Complex) {
                                val = ((Complex) xx).absDouble();
                            } else if (xx != null) {
                                val = xx.doubleValue();
                            }
                            double db = 20 * Math.log10(Math.max(1e-12, val));
                            return NElement.ofDouble(db);
                        }).collect(Collectors.toList())
                ).build();

        NTxObjFromMap curveMap = NTxObjs.map()
                .set("x", NTxObjs.elem(NElement.ofArray(Arrays.stream(x).map(NElement::ofNumber).toArray(NElement[]::new))))
                .set("y", NTxObjs.elem(yeAbs))
                .set("mag", NTxObjs.elem(yeAbs))
                .set("abs", NTxObjs.elem(yeAbs))
                .set("db", NTxObjs.elem(yeDb))
                .set("complex", NTxObjs.elem(ye));

        plan.rendererContext().compiledDocument().setGlobalObject(outputName(), curveMap);
        if (plan.name() != null && !plan.name().trim().isEmpty()) {
            String pName = plan.name().trim();
            plan.rendererContext().compiledDocument().setGlobalObject(pName + "." + outputName(), curveMap);
            plan.rendererContext().compiledDocument().setGlobalObject(pName + "." + outputName() + ".x", curveMap.get("x").orNull());
            plan.rendererContext().compiledDocument().setGlobalObject(pName + "." + outputName() + ".y", curveMap.get("y").orNull());
            plan.rendererContext().compiledDocument().setGlobalObject(pName + "." + outputName() + ".db", curveMap.get("db").orNull());
            plan.rendererContext().compiledDocument().setGlobalObject(pName + "." + outputName() + ".mag", curveMap.get("mag").orNull());
            NTxObj existing = plan.rendererContext().compiledDocument().getGlobalObject(pName).orNull();
            NTxObjFromMap pMap = (existing instanceof NTxObjFromMap) ? (NTxObjFromMap) existing : NTxObjs.map();
            pMap.set(outputName(), curveMap);
            plan.rendererContext().compiledDocument().setGlobalObject(pName, pMap);
        }

        if (plan.rendererContext().isAnimate()) {
            Plot.cd(fullPath()).title(fullName()).plot(y);
        }
        plan.rendererContext().log(NMsg.ofC("[%s] %s Finished in %s : ", outputName(), solverName(), chronometer.stop()));
        return Collections.singletonList(
                NTxSimulationResultFactory.createPlot2dCurve(outputName(), activeSweep, complexValues)
        );
    }

    protected abstract Complex evalComplex(OpenEMSStrNTxSimulationPlan plan, double freq);
}
