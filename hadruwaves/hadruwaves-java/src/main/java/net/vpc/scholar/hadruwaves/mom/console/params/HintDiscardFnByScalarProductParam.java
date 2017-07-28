package net.vpc.scholar.hadruwaves.mom.console.params;

import net.vpc.scholar.hadrumaths.AbstractParam;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

public class HintDiscardFnByScalarProductParam extends AbstractParam implements Cloneable {

    public HintDiscardFnByScalarProductParam() {
        super("HintDiscardFnByScalarProduct");
    }


    @Override
    public void configure(Object source, Object value) {
        float ff=(Float)value;
        ((MomStructure) source).getHintsManager().setHintDiscardFnByScalarProduct(ff<=0?null:ff);
    }

}
