package net.vpc.scholar.hadruwaves.mom.console.params;

import net.vpc.scholar.hadruplot.console.params.AbstractCParam;
import net.vpc.scholar.hadruwaves.mom.CircuitType;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

public class CircuitTypeParam extends AbstractCParam implements Cloneable {
    public CircuitTypeParam() {
        super("circuitType");
    }

    @Override
    public void configure(Object source, Object value) {
        ((MomStructure) source).setCircuitType((CircuitType) value);
    }
}