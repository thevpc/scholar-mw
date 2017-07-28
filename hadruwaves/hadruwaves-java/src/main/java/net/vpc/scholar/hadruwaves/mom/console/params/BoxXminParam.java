package net.vpc.scholar.hadruwaves.mom.console.params;

import net.vpc.scholar.hadrumaths.AbstractParam;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

public class BoxXminParam extends AbstractParam implements Cloneable {
    public BoxXminParam() {
        super("xmin");
    }

    public void configure(Object source,Object value) {
        ((MomStructure) source).setXmin((Double) value);
    }
}
