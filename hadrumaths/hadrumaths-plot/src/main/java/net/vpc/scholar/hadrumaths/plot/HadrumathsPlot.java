package net.vpc.scholar.hadrumaths.plot;

import net.vpc.hadralang.stdlib.JExports;

@JExports({
        //import plot class
        "net.vpc.scholar.hadruplot.Plot",
        "net.vpc.scholar.hadrumaths.plot.internal.hl.HLPlot.*",
        "net.vpc.scholar.hadrumaths.Hadrumaths.**"
})
public final class HadrumathsPlot {
    private HadrumathsPlot() {
    }
}
