package net.thevpc.ntexup.extension.mwsimulator;

import net.thevpc.nuts.elem.NElement;

import java.util.List;

public class NTxSimulationResultFactory {
    public static NTxSimulationResult createPlot2dCurve(
            String computeName,
            NTxSweep sweep,
            List<Number> values
    ){
        return new NTxSimulationResult() {
            @Override
            public String name() {
                return computeName;
            }

            @Override
            public NElement toPlotElement() {
                return NTxMwSimulationUtils.toPlot2dCurve(sweep,values);
            }
        };
    }
}
