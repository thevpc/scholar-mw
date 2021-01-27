package net.thevpc.scholar.hadruwaves.mom.console.params;

import net.thevpc.scholar.hadruplot.console.params.AbstractCParam;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

public class ModesCountParam extends AbstractCParam implements Cloneable{

    public ModesCountParam() {
        super("fn");
    }

    @Override
    public void configure(Object source, Object value) {
        ((MomStructure)source).modeFunctions().setSize((Integer) value);
    }

}
