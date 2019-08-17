package net.vpc.scholar.hadrumaths.plot;

import net.vpc.common.util.DoubleFormat;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadruplot.console.PlotConfig;

public class MathsPlotConfig implements PlotConfig {
    public DoubleFormat getPercentFormat() {
        return Maths.Config.getPercentFormat();
    }

    public String getCacheFolder(String folder) {
        return Maths.Config.getCacheFolder(folder);
    }

    @Override
    public DoubleFormat dblformat(String format) {
        return Maths.dblformat(format);
    }
}
