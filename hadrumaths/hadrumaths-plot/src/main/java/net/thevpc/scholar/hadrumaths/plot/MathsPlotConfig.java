package net.thevpc.scholar.hadrumaths.plot;

import net.thevpc.nuts.text.NTextFormat;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruplot.console.PlotConfig;

public class MathsPlotConfig implements PlotConfig {
    public NTextFormat<Number> getPercentFormat() {
        return Maths.Config.getPercentFormat();
    }

    @Override
    public NTextFormat<Number> dblformat(String format) {
        return Maths.dblformat(format);
    }

    public String getCacheFolder(String folder) {
        return Maths.Config.getCacheFolder(folder);
    }
}
