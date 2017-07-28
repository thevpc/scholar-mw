package net.vpc.scholar.hadrumaths.plot.console.xlabels;

import net.vpc.scholar.hadrumaths.plot.console.params.ParamSet;

public class IndexXLabel extends AbstractXLabel{

    public IndexXLabel() {
    }

    public Number getValue(int index, ParamSet x, Object structure) {
        return index;
    }
}
