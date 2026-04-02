package net.thevpc.scholar.hadruplot.console;

import net.thevpc.nuts.text.NTextFormat;

public interface PlotConfig {
    NTextFormat<Number> dblformat(String format);
    String getCacheFolder(String path);
}
