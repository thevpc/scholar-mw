package net.vpc.scholar.hadruwaves.mom.console.params;

import net.vpc.scholar.hadrumaths.AbstractParam;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

public class BoxXminFactorParam extends AbstractParam implements Cloneable {
    public BoxXminFactorParam() {
        super("xmin/a");
    }

    public void configure(Object source,Object value) {
        ((MomStructure) source).setXminFactor((Double) value);
    }
}
