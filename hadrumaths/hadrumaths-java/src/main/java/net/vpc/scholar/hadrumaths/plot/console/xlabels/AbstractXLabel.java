package net.vpc.scholar.hadrumaths.plot.console.xlabels;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 29 oct. 2006
 * Time: 15:52:33
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractXLabel implements XLabel {

    public AbstractXLabel() {
    }

    public String getName() {
        return getClass().getSimpleName();
    }

    public String toString() {
        return getClass().getSimpleName();
    }
}
