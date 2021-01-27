package net.thevpc.scholar.hadruwaves.mom.console.params;

import net.thevpc.scholar.hadruplot.console.params.AbstractCParam;
import net.thevpc.scholar.hadruwaves.mom.CircuitType;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

public class CircuitTypeParam extends AbstractCParam implements Cloneable {
    public CircuitTypeParam() {
        super("circuitType");
    }

    @Override
    public void configure(Object source, Object value) {
        ((MomStructure) source).setCircuitType((CircuitType) value);
    }
}