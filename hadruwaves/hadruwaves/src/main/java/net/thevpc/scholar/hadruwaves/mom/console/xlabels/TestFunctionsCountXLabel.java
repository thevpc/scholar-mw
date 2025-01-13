package net.thevpc.scholar.hadruwaves.mom.console.xlabels;

import net.thevpc.scholar.hadruplot.console.params.ParamSet;
import net.thevpc.scholar.hadruplot.console.xlabels.AbstractXLabel;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 29 oct. 2006
 * Time: 15:52:26
 * To change this template use File | Settings | File Templates.
 */
public class TestFunctionsCountXLabel extends AbstractXLabel {

    public TestFunctionsCountXLabel() {
    }

    public Number getValue(int index, ParamSet x, Object structure) {
        return ((MomStructure )structure).testFunctions().count();
    }
}
