package net.thevpc.ntexup.extension.mwsimulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NTxSimulationResultsImpl implements NTxSimulationResults {
    private List<NTxSimulationResult> results;

    public NTxSimulationResultsImpl(List<NTxSimulationResult> results) {
        this.results = new ArrayList<>(results);
    }

    @Override
    public List<NTxSimulationResult> list() {
        return Collections.unmodifiableList(results);
    }
}
