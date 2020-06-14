package net.vpc.scholar.hadruplot.console.extension;

import net.vpc.scholar.hadruplot.console.ConsoleAction;

import java.io.File;

public interface PlotConsoleFileSupport {

    String getFileExtension();

    String getFileDesc();

    ConsoleAction createLoadAction(File file) ;

    ConsoleAction createLoadAction(File[] files);
}
