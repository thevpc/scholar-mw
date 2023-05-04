package net.thevpc.scholar.hadruplot.containers;

import net.thevpc.scholar.hadruplot.extension.PlotWindowContainerFactory;

public class PlotWindowContainerFactories {
    public static PlotWindowContainerFactory create(String name) {
        if (name == null) {
            name = "";
        }
        name = name.trim().toLowerCase();
        switch (name) {
            case "tab":
            case "tabs":
            case "tabbed":
                return TabbedPlotWindowContainerFactory.INSTANCE;
            case "win":
            case "wins":
            case "frame":
            case "frames":
            case "window":
            case "windows":
                return DefaultPlotWindowContainerFactory.INSTANCE;
            case "panel":
            case "grid":
                return PanelPlotWindowContainerFactory.INSTANCE;
            case "list":
                return ListCardPlotContainer::new;
            case "tree":
                return TreeCardPlotContainer::new;
        }
        return create("grid");
    }
}
