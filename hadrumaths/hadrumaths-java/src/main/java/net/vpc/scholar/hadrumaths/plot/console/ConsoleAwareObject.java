package net.vpc.scholar.hadrumaths.plot.console;

import net.vpc.scholar.hadrumaths.plot.console.params.ParamTarget;
import net.vpc.scholar.hadrumaths.util.log.TLog;
import net.vpc.scholar.hadrumaths.util.dump.Dumpable;

/**
 * Created by vpc on 3/15/15.
 */
public interface ConsoleAwareObject extends Dumpable {
    void setLog(TLog log);

    ConsoleAwareObject clone();

    void setTarget(ParamTarget target);
}
