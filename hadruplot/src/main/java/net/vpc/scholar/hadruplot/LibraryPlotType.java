package net.vpc.scholar.hadruplot;

import net.vpc.common.strings.StringUtils;

public class LibraryPlotType {
    private PlotType type;
    private String library;

    public LibraryPlotType(PlotType type, String library) {
        this.type = type;
        this.library = library;
    }

    public PlotType getType() {
        return type;
    }

    public String getLibrary() {
        return library;
    }

    @Override
    public String toString() {
        return StringUtils.toCapitalized(type.name())
                + ((library == null) ? "" : (" (" + StringUtils.toCapitalized(library) + ")"));
    }
}
