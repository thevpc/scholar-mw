package net.vpc.scholar.hadruwaves.mom.console.params;

import net.vpc.scholar.hadruplot.console.params.AbstractCParam;
import net.vpc.scholar.hadruwaves.WallBorders;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

public class ModeFunctionsParam extends AbstractCParam {
    public ModeFunctionsParam() {
        super("borders");
    }

    @Override
    public void configure(Object source, Object value) {
        ((MomStructure) source).setBorders((WallBorders) value);
    }
}
