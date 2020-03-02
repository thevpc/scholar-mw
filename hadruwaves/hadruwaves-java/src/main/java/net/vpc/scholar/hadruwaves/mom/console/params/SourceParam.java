package net.vpc.scholar.hadruwaves.mom.console.params;

import net.vpc.scholar.hadruplot.console.params.AbstractCParam;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.sources.Source;


public class SourceParam extends AbstractCParam implements Cloneable {

    public SourceParam() {
        super("Src");
    }

    @Override
    public void configure(Object source, Object value) {
        ((MomStructure) source).setSources((Source)value);
    }

}
