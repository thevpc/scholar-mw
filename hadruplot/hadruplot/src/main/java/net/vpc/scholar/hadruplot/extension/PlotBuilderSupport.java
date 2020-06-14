package net.vpc.scholar.hadruplot.extension;

import net.vpc.scholar.hadruplot.PlotBuilder;

public interface PlotBuilderSupport {
    boolean xsamples(Object xvalue, PlotBuilder builder);

    boolean ysamples(Object xvalue, PlotBuilder builder);

    boolean zsamples(Object xvalue, PlotBuilder builder);
}
