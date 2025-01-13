package net.thevpc.scholar.hadruwaves.mom.console.params;

import net.thevpc.scholar.hadruplot.console.params.AbstractCParam;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

public class BoxYminParam extends AbstractCParam implements Cloneable {
    public BoxYminParam() {
        super("ymin");
    }

    public void configure(Object source,Object value) {
        MomStructure s = (MomStructure) source;
        s.setDomain(s.getDomain().replaceYmin((Double) value));
    }
}
