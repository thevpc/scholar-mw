package net.thevpc.scholar.hadruwaves.mom.console.params;

import net.thevpc.scholar.hadruplot.console.params.AbstractCParam;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.sources.Sources;


public class SourcesParam extends AbstractCParam implements Cloneable {

    public SourcesParam() {
        super("Src");
    }

    @Override
    public void configure(Object source, Object value) {
        ((MomStructure) source).setSources((Sources)value);
    }

}
