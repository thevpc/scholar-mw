package net.vpc.scholar.hadruwaves.console.params;

import net.vpc.scholar.hadruplot.console.params.AbstractCParam;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

public class FreqParam extends AbstractCParam implements Cloneable {

    public FreqParam() {
        super("freq");
    }

    @Override
    public void configure(Object source, Object value) {
        ((MomStructure) source).setFrequency((Double) value);
    }

}
