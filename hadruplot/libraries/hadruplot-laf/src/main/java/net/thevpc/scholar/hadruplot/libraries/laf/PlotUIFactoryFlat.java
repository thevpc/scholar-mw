package net.thevpc.scholar.hadruplot.libraries.laf;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import net.thevpc.scholar.hadruplot.PlotUI;
import net.thevpc.scholar.hadruplot.PlotUIFactory;

import java.util.Arrays;
import java.util.List;

public class PlotUIFactoryFlat implements PlotUIFactory {
    @Override
    public List<PlotUI> findPlotUI() {
        return Arrays.asList(
                new PlotUIFlat("FlatLight", FlatLightLaf::new),
                new PlotUIFlat("FlatDark", FlatDarkLaf::new),
                new PlotUIFlat("FlatMacLight", FlatMacLightLaf::new),
                new PlotUIFlat("FlatMacDark", FlatMacDarkLaf::new)
        );
    }
}
