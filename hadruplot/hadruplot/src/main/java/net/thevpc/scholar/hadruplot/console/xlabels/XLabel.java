package net.thevpc.scholar.hadruplot.console.xlabels;

import net.thevpc.scholar.hadruplot.console.params.ParamSet;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 29 oct. 2006
 * Time: 15:41:55
 * To change this template use File | Settings | File Templates.
 */
public interface XLabel {
    public Number getValue(int index, ParamSet x, Object structure);

    public String getName();
}
