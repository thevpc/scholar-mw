package net.thevpc.scholar.hadruplot.extension;

import net.thevpc.scholar.hadruplot.PlotBuilder;

public interface PlotBuilderSupport {
    boolean xsamples(Object xvalue, PlotBuilder builder);

    boolean ysamples(Object xvalue, PlotBuilder builder);

    boolean zsamples(Object xvalue, PlotBuilder builder);
}
