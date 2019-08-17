package net.vpc.scholar.hadruplot.console.xlabels;

import net.vpc.scholar.hadruplot.console.params.ParamSet;

public class IndexXLabel extends AbstractXLabel {

    public IndexXLabel() {
    }

    public Number getValue(int index, ParamSet x, Object structure) {
        return index;
    }
}
