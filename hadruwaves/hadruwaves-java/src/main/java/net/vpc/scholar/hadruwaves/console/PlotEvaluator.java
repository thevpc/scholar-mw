package net.vpc.scholar.hadruwaves.console;

import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleAwareObject;
import net.vpc.common.util.mon.ProgressMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface PlotEvaluator<T> {
    T computeValue(ConsoleAwareObject source, ProgressMonitor monitor, ConsoleActionParams p) ;
}
