package net.thevpc.scholar.hadrumaths.plot;

import net.thevpc.common.util.DoubleFormat;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruplot.console.PlotConfig;

public class MathsPlotConfig implements PlotConfig {
    public DoubleFormat getPercentFormat() {
        return Maths.Config.getPercentFormat();
    }

    @Override
    public DoubleFormat dblformat(String format) {
        return Maths.dblformat(format);
    }

    public String getCacheFolder(String folder) {
        return Maths.Config.getCacheFolder(folder);
    }
}
