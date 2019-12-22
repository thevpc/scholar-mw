package net.vpc.scholar.hadruplot;

public interface PlotBuilderSupport {
    boolean xsamples(Object xvalue, PlotBuilder builder);

    boolean ysamples(Object xvalue, PlotBuilder builder);

    boolean zsamples(Object xvalue, PlotBuilder builder);
}
