package net.vpc.scholar.hadruwaves.mom.console.params;

import net.vpc.scholar.hadruplot.console.params.AbstractCParam;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.sources.Sources;


public class SourcesParam extends AbstractCParam implements Cloneable {

    public SourcesParam() {
        super("Src");
    }

    @Override
    public void configure(Object source, Object value) {
        ((MomStructure) source).setSources((Sources)value);
    }

}
