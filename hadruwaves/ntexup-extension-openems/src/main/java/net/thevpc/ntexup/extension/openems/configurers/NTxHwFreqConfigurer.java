package net.thevpc.ntexup.extension.openems.configurers;

import net.thevpc.ntexup.extension.mwsimulator.NTxStrSimulationQuery;
import net.thevpc.ntexup.extension.mwsimulator.NTxTargetConfigurer;

public class NTxHwFreqConfigurer implements NTxTargetConfigurer {
    @Override
    public void configure(NTxStrSimulationQuery m, Object a) {
        double freq=((Number) a).doubleValue(); // store it some where
    }
}
