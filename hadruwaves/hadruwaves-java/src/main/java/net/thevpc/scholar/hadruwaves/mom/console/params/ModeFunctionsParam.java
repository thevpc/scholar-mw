package net.thevpc.scholar.hadruwaves.mom.console.params;

import net.thevpc.scholar.hadruplot.console.params.AbstractCParam;
import net.thevpc.scholar.hadruwaves.WallBorders;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

public class ModeFunctionsParam extends AbstractCParam {
    public ModeFunctionsParam() {
        super("borders");
    }

    @Override
    public void configure(Object source, Object value) {
        ((MomStructure) source).setBorders((WallBorders) value);
    }
}
