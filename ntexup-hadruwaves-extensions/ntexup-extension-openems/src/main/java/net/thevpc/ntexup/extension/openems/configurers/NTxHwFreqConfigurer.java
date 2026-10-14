package net.thevpc.ntexup.extension.openems.configurers;

import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationPlan;
import net.thevpc.ntexup.extension.mwsimulator.NTxTargetConfigurer;

public class NTxHwFreqConfigurer implements NTxTargetConfigurer {
    @Override
    public void configure(NTxSimulationPlan m, Object a) {
        double freq=((Number) a).doubleValue(); // store it some where
    }
}
