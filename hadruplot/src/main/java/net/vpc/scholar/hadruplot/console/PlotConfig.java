package net.vpc.scholar.hadruplot.console;

import net.vpc.common.util.DoubleFormat;

public interface PlotConfig {
    DoubleFormat dblformat(String format);
    String getCacheFolder(String path);
}
