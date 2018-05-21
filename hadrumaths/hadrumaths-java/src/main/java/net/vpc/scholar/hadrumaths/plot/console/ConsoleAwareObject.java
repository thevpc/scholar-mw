package net.vpc.scholar.hadrumaths.plot.console;

import net.vpc.scholar.hadrumaths.plot.console.params.ParamTarget;
import net.vpc.scholar.hadrumaths.util.TLog;
import net.vpc.scholar.hadrumaths.dump.Dumpable;

/**
 * Created by vpc on 3/15/15.
 */
public interface ConsoleAwareObject extends Dumpable {
    public void setLog(TLog log);

    public ConsoleAwareObject clone();

    public void setTarget(ParamTarget target);
}
