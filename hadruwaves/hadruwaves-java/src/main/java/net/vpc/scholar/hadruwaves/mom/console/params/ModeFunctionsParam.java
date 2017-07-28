package net.vpc.scholar.hadruwaves.mom.console.params;

import net.vpc.scholar.hadrumaths.AbstractParam;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

public class ModeFunctionsParam extends AbstractParam {
    public ModeFunctionsParam() {
        super("fnType");
    }

    @Override
    public void configure(Object source, Object value) {
        ((MomStructure) source).setModeFunctions((ModeFunctions) value);
    }
}
