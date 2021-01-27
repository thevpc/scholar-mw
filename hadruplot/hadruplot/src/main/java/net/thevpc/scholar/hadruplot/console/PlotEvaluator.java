package net.thevpc.scholar.hadruplot.console;

import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.common.mon.ProgressMonitor;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public interface PlotEvaluator<T> {
    T evalValue(ConsoleAwareObject source, ProgressMonitor monitor, ConsoleActionParams p) ;
}
