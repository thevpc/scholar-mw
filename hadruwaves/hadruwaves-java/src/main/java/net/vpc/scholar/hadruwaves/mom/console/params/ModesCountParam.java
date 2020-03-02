package net.vpc.scholar.hadruwaves.mom.console.params;

import net.vpc.scholar.hadruplot.console.params.AbstractCParam;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

public class ModesCountParam extends AbstractCParam implements Cloneable{

    public ModesCountParam() {
        super("fn");
    }

    @Override
    public void configure(Object source, Object value) {
        ((MomStructure)source).getModeFunctions().setSize((Integer) value);
    }

}
