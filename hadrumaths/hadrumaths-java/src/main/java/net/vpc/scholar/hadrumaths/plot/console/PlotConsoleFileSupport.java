package net.vpc.scholar.hadrumaths.plot.console;

import java.io.File;

public interface PlotConsoleFileSupport {

    String getFileExtension();

    String getFileDesc();

    default ConsoleAction createLoadAction(File file) {
        return createLoadAction(new File[]{file});
    }

    ConsoleAction createLoadAction(File[] files);
}
