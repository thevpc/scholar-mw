package net.thevpc.ntexup.extension.hadruwaves.configurers;

import net.thevpc.ntexup.extension.hadruwaves.MoMStrSimulationQuery;
import net.thevpc.ntexup.extension.mwsimulator.NTxStrSimulationQuery;
import net.thevpc.ntexup.extension.mwsimulator.NTxTargetConfigurer;

public class NTxHwFreqConfigurer implements NTxTargetConfigurer {
    @Override
    public void configure(NTxStrSimulationQuery m, Object a) {
        ((MoMStrSimulationQuery) m).str.setFrequency(((Number) a).doubleValue());
    }
}
