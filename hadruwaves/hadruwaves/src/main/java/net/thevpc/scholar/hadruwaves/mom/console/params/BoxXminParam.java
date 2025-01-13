package net.thevpc.scholar.hadruwaves.mom.console.params;

import net.thevpc.scholar.hadruplot.console.params.AbstractCParam;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

public class BoxXminParam extends AbstractCParam implements Cloneable {
    public BoxXminParam() {
        super("xmin");
    }

    public void configure(Object source,Object value) {
        MomStructure s = (MomStructure) source;
        s.setDomain(s.getDomain().replaceXmin((Double) value));
    }
}
