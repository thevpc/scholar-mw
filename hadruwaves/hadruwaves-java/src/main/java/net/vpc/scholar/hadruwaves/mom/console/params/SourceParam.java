package net.vpc.scholar.hadruwaves.mom.console.params;

import net.vpc.scholar.hadrumaths.AbstractParam;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.sources.Source;


public class SourceParam extends AbstractParam implements Cloneable {

    public SourceParam() {
        super("Src");
    }

    @Override
    public void configure(Object source, Object value) {
        ((MomStructure) source).setSources((Source)value);
    }

}
