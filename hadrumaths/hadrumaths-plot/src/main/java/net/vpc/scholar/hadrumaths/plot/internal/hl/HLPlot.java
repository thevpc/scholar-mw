package net.vpc.scholar.hadrumaths.plot.internal.hl;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.plot.PlotDomainFromDomain;
import net.vpc.scholar.hadruplot.PlotDomain;

public final class HLPlot {
    private HLPlot() {
    }

    public static PlotDomain newPlotDomain(Domain domain){
        return new PlotDomainFromDomain(domain);
    }
}
