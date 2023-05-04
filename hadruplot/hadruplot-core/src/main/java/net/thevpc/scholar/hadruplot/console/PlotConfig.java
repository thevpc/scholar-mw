package net.thevpc.scholar.hadruplot.console;

import net.thevpc.common.util.DoubleFormat;

public interface PlotConfig {
    DoubleFormat dblformat(String format);
    String getCacheFolder(String path);
}
