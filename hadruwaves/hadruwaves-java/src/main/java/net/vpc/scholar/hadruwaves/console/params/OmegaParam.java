package net.vpc.scholar.hadruwaves.console.params;

import net.vpc.scholar.hadruplot.console.params.AbstractCParam;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

public class OmegaParam extends AbstractCParam implements Cloneable {
    public OmegaParam() {
        super("Omega");
    }

    public void configure(Object str,Object value) {
        ((MomStructure)str).setFrequency((Double) value / (2.0 * Math.PI));
    }

}
