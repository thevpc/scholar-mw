package net.vpc.scholar.hadruwaves.mom.console.params;

import net.vpc.scholar.hadrumaths.AbstractParam;
import net.vpc.scholar.hadruwaves.mom.CircuitType;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

public class CircuitTypeParam extends AbstractParam implements Cloneable {
    public CircuitTypeParam() {
        super("circuitType");
    }

    @Override
    public void configure(Object source, Object value) {
        ((MomStructure) source).setCircuitType((CircuitType) value);
    }
}