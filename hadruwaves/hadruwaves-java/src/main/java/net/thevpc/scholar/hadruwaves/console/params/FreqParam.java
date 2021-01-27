package net.thevpc.scholar.hadruwaves.console.params;

import net.thevpc.scholar.hadruplot.console.params.AbstractCParam;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

public class FreqParam extends AbstractCParam implements Cloneable {

    public FreqParam() {
        super("freq");
    }

    @Override
    public void configure(Object source, Object value) {
        ((MomStructure) source).setFrequency((Double) value);
    }

}
