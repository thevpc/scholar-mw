package net.vpc.scholar.hadrumaths.plot.console;

public abstract class AbstractPlotConsoleFileSupport implements PlotConsoleFileSupport {
    private String extension;
    private String description;

    public AbstractPlotConsoleFileSupport(String extension, String description) {
        this.extension = extension;
        this.description = description;
    }

    @Override
    public String getFileExtension() {
        return extension;
    }

    @Override
    public String getFileDesc() {
        return description;
    }

}
