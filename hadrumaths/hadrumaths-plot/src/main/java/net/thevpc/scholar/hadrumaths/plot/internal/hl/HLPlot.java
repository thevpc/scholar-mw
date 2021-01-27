package net.thevpc.scholar.hadrumaths.plot.internal.hl;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.plot.PlotDomainFromDomain;
import net.thevpc.scholar.hadruplot.PlotDomain;

public final class HLPlot {
    private HLPlot() {
    }

    public static PlotDomain newPlotDomain(Domain domain){
        return new PlotDomainFromDomain(domain);
    }
}
