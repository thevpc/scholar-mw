package net.vpc.scholar.hadruplot;

import net.vpc.scholar.hadruplot.console.ConsoleActionParams;
import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.common.mon.ProgressMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface PlotEvaluator<T> {
    T computeValue(ConsoleAwareObject source, ProgressMonitor monitor, ConsoleActionParams p) ;
}
