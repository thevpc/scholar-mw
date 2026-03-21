package net.thevpc.ntexup.extension.mwsimulator;

import net.thevpc.nuts.elem.NElement;

public interface NTxSimulationResult {
    String name();

    NElement toPlotElement();
}
