package net.thevpc.scholar.hadruplot;

import net.thevpc.nuts.util.NNameFormat;

public class LibraryPlotType {
    private PlotType type;
    private String library;

    public LibraryPlotType(PlotType type) {
        this(type,"default");
    }
    
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
        return NNameFormat.UPPER_CAMEL_CASE.format(type.name())
                + ((library == null) ? "" : (" (" + NNameFormat.UPPER_CAMEL_CASE.format(library) + ")"));
    }
}
