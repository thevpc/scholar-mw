package net.thevpc.scholar.hadruplot.console.xlabels;

import net.thevpc.scholar.hadruplot.console.params.ParamSet;

public class IndexXLabel extends AbstractXLabel {

    public IndexXLabel() {
    }

    public Number getValue(int index, ParamSet x, Object structure) {
        return index;
    }
}
