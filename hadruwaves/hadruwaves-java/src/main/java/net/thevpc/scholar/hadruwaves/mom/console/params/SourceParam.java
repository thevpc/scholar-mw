package net.thevpc.scholar.hadruwaves.mom.console.params;

import net.thevpc.scholar.hadruplot.console.params.AbstractCParam;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.sources.Source;


public class SourceParam extends AbstractCParam implements Cloneable {

    public SourceParam() {
        super("Src");
    }

    @Override
    public void configure(Object source, Object value) {
        ((MomStructure) source).setSources((Source)value);
    }

}
