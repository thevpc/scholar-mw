package net.thevpc.scholar.hadruwaves.mom.console.params;

import net.thevpc.scholar.hadruplot.console.params.AbstractCParam;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.TestFunctions;


public class TestFunctionsParam extends AbstractCParam implements Cloneable {

    public TestFunctionsParam() {
        super("gpType");
    }

    @Override
    public void configure(Object source, Object value) {
//        boolean show = !structure.getGpTestFunctionsTemplate().getClass().equals(getValue().getClass());

        ((MomStructure) source).setTestFunctions(((TestFunctions) value).clone());

//        if (show) {
//            AreaFunctionXPlot.showPlotReal("Gp : "+p3Abstract.getClass().getSimpleName()+" : "+p3Abstract.getGpTestFunctions().getClass().getSimpleName(), p3Abstract.getGpTestFunctions().gpCache(), p3Abstract.getDomain());
//        }
    }
}
