package net.vpc.scholar.hadruplot.console;

import net.vpc.scholar.hadruplot.console.params.ParamTarget;

/**
 * Created by vpc on 3/15/15.
 */
public interface ConsoleAwareObject /*extends Dumpable*/ {
    ConsoleAwareObject setLog(ConsoleLogger log);

    ConsoleAwareObject clone();

    ParamTarget getTarget();

    ConsoleAwareObject setTarget(ParamTarget target);

    String dump();
}
