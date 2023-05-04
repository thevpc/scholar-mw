package net.thevpc.scholar.hadruplot.console;

import net.thevpc.scholar.hadruplot.console.extension.PlotConsoleFileSupport;

import java.io.File;

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


    public ConsoleAction createLoadAction(File file) {
        return createLoadAction(new File[]{file});
    }

}
