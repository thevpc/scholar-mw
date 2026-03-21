package net.thevpc.ntexup.extension.hadruwaves.base;

import net.thevpc.ntexup.extension.hadruwaves.MoMStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.mwsimulator.*;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NUpletElementBuilder;
import net.thevpc.nuts.math.NDoubleComplex;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.time.NChronometer;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class NTxHwComplexNTxSolver extends NTxSolverRunImpl {
    private NTxSweep sweep;
    private NTxSweepTarget sweepParam;

    public NTxHwComplexNTxSolver(MoMStrNTxSimulationPlan moMStrSimulationQuery, String computeName, String solverName, String solverType) {
        super(computeName, solverName, solverType,moMStrSimulationQuery);
    }

    @Override
    public void addImpl(String paramName, NElement paramValue) {
        switch (paramName){
            case "freq":
            case "frequency":
            case "frequencies":{
                sweepParam=NTxSweepTarget.FREQ;
                sweep=NTxSweep.parse(paramValue).orNull();
                break;
            }
        }
    }

    @Override
    public NElement toElement() {
        NUpletElementBuilder b = NElement.ofUpletBuilder(outputName());
        b.add(NElement.ofPair("solver",solverType()));
        if(sweepParam !=null){
            b.add(NElement.ofPair("param", sweepParam.name()));
            b.add(NElement.ofPair("sweep", sweep.toElement()));
        }
        return b.build();
    }

    @Override
    public List<NTxSimulationResult> execute() {
        MomStructure str = ((MoMStrNTxSimulationPlan) query()).str;
        NChronometer chronometer = NChronometer.startNow();
        List<Number> complexValues = new ArrayList<>();
        if (sweep != null) {
            switch (sweepParam) {
                case FREQ:
                {
                    str.log().log(NMsg.ofC("------------------"));
                    str.log().log(NMsg.ofC("[%s] %s (%s over %s): ", outputName(), solverName(),sweepParam,sweep));
                    str.log().log(NMsg.ofC("------------------"));
                    for (double fr : sweep.doubleValues()) {
                        str.setFrequency(fr);
                        Complex c = evalComplex(str);
                        complexValues.add(NDoubleComplex.of(c.getReal(), c.getImag()));
                    }
                    break;
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
        }
        Plot.title(solverType()+"-"+ outputName()).plot(complexValues.toArray(new Number[0]));
        str.log().log(NMsg.ofC("[%s] %s Finished in %s : ", outputName(), solverName(),chronometer.stop()));
        return Arrays.asList(
                NTxSimulationResultFactory.createPlot2dCurve(outputName(), sweep, complexValues)
        );
    }

    protected abstract Complex evalComplex(MomStructure str);

}
