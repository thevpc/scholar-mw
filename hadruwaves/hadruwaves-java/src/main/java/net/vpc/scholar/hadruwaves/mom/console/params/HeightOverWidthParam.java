package net.vpc.scholar.hadruwaves.mom.console.params;

import net.vpc.scholar.hadrumaths.AbstractParam;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

public class HeightOverWidthParam extends AbstractParam implements Cloneable {
    public HeightOverWidthParam() {
        super("b/a");
    }

    @Override
    public void configure(Object source, Object value) {
        ((MomStructure) source).setHeightFactor((Double)value);
    }
}
