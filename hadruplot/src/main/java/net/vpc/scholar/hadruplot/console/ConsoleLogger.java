package net.vpc.scholar.hadruplot.console;

public interface ConsoleLogger {

    void trace(Object msg);

    void error(Object msg);

    void warning(Object msg);

    void debug(Object msg);

}
