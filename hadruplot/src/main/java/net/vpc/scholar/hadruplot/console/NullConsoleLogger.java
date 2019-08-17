package net.vpc.scholar.hadruplot.console;

public class NullConsoleLogger implements ConsoleLogger{
    public static final NullConsoleLogger INSTANCE=new NullConsoleLogger();
    @Override
    public void trace(Object msg) {

    }

    @Override
    public void error(Object msg) {

    }

    @Override
    public void warning(Object msg) {

    }

    @Override
    public void debug(Object msg) {

    }
}
