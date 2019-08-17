package net.vpc.scholar.hadruwaves.mom.console.params;

import net.vpc.scholar.hadruplot.console.params.AbstractParam;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.TestFunctions;


public class TestFunctionsParam extends AbstractParam implements Cloneable {

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
