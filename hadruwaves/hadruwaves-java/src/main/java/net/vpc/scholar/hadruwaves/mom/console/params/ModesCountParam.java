package net.vpc.scholar.hadruwaves.mom.console.params;

import net.vpc.scholar.hadruplot.console.params.AbstractParam;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

public class ModesCountParam extends AbstractParam implements Cloneable{

    public ModesCountParam() {
        super("fn");
    }

    @Override
    public void configure(Object source, Object value) {
        ((MomStructure)source).setModeFunctionsCount((Integer) value);
    }

}
