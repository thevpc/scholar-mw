package net.thevpc.ntexup.extension.hadruwaves.configurers;

import net.thevpc.ntexup.extension.hadruwaves.MoMStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationPlan;
import net.thevpc.ntexup.extension.mwsimulator.NTxTargetConfigurer;

public class NTxHwFreqConfigurer implements NTxTargetConfigurer {
    @Override
    public void configure(NTxSimulationPlan m, Object a) {
        ((MoMStrNTxSimulationPlan) m).str.setFrequency(((Number) a).doubleValue());
    }
}
