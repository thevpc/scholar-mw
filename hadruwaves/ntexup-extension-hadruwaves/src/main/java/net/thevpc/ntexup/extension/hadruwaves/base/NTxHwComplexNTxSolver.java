package net.thevpc.ntexup.extension.hadruwaves.base;

import net.thevpc.ntexup.api.eval.NTxObj;
import net.thevpc.ntexup.api.eval.NTxObjs;
import net.thevpc.ntexup.extension.hadruwaves.MoMStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.mwsimulator.*;
import net.thevpc.nuts.elem.NArrayElement;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NElements;
import net.thevpc.nuts.elem.NUpletElementBuilder;
import net.thevpc.nuts.math.NDoubleComplex;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.time.NChronometer;
import net.thevpc.nuts.util.NOptional;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

import java.util.*;
import java.util.stream.Collectors;

public abstract class NTxHwComplexNTxSolver extends NTxSolverRunImpl {
    private NTxSweep sweep;
    private NTxSweepTarget sweepParam;

    public NTxHwComplexNTxSolver(MoMStrNTxSimulationPlan plan, String computeName, String solverName, String solverType) {
        super(computeName, solverName, solverType, plan);
    }

    @Override
    public void addImpl(String paramName, NElement paramValue) {
        switch (paramName) {
            case "freq":
            case "frequency":
            case "frequencies": {
                sweepParam = NTxSweepTarget.FREQ;
                sweep = NTxSweep.parse(paramValue).orNull();
                break;
            }
        }
    }

    @Override
    public NElement toElement() {
        NUpletElementBuilder b = NElement.ofUpletBuilder(outputName());
        b.add(NElement.ofPair("solver", solverType()));
        if (sweepParam != null) {
            b.add(NElement.ofPair("param", sweepParam.name()));
            b.add(NElement.ofPair("sweep", sweep.toElement()));
        }
        return b.build();
    }

    @Override
    public List<NTxSimulationResult> execute() {
        MomStructure str = ((MoMStrNTxSimulationPlan) plan()).str;
        NChronometer chronometer = NChronometer.startNow();
        List<Number> complexValues = new ArrayList<>();
        Number[] x = new Number[0];
        if (sweep != null) {
            switch (sweepParam) {
                case FREQ: {
                    str.log().log(NMsg.ofC("------------------"));
                    str.log().log(NMsg.ofC("[%s] %s (%s over %s): ", outputName(), solverName(), sweepParam, sweep));
                    str.log().log(NMsg.ofC("------------------"));
                    double[] dv = sweep.doubleValues();
                    for (double fr : dv) {
                        str.setFrequency(fr);
                        Complex c = evalComplex(str);
                        complexValues.add(NDoubleComplex.of(c.getReal(), c.getImag()));
                    }
                    x = Arrays.stream(dv).boxed().toArray(Number[]::new);
                    break;
                }
                default: {
                    x=new Number[]{0.0};
                    complexValues.add(0.0);
                }
            }
        } else {
            str.log().log(NMsg.ofC("------------------"));
            str.log().log(NMsg.ofC("[%s] %s (single value): ", outputName(), solverName()));
            str.log().log(NMsg.ofC("------------------"));
            Complex c = evalComplex(str);
            complexValues.add(
                    NDoubleComplex.of(c.getReal(), c.getImag())
            );
            x = new Number[]{0.0};
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
        plan().rendererContext().compiledDocument().setGlobalObject(outputName(), NTxObjs
                .map()
                .set("x", NElement.ofArray(
                        Arrays.stream(x).map(xx->NElement.ofNumber(xx))
                ))
                .set("y", ye)
        );
        Plot.title(solverType() + "-" + outputName()).plot(y);
        str.log().log(NMsg.ofC("[%s] %s Finished in %s : ", outputName(), solverName(), chronometer.stop()));
        return Collections.singletonList(
                NTxSimulationResultFactory.createPlot2dCurve(outputName(), sweep, complexValues)
        );
    }

    protected abstract Complex evalComplex(MomStructure str);

}
