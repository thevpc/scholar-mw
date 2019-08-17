package net.vpc.scholar.hadruwaves.console.params;

import net.vpc.scholar.hadruplot.console.params.AbstractParam;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

public class FreqParam extends AbstractParam implements Cloneable {

    public FreqParam() {
        super("freq");
    }

    @Override
    public void configure(Object source, Object value) {
        ((MomStructure) source).setFrequency((Double) value);
    }

}
