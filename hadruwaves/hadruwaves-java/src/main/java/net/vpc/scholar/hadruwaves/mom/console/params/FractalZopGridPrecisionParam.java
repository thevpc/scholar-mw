package net.vpc.scholar.hadruwaves.mom.console.params;

import net.vpc.scholar.hadruplot.console.params.AbstractCParam;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.str.zsfractalmodel.MomStructureFractalZop;

public class FractalZopGridPrecisionParam extends AbstractCParam implements Cloneable {

    public FractalZopGridPrecisionParam() {
        super("FractalZopGridPrecision");
    }

    @Override
    public void configure(Object source,Object value) {
        ((MomStructure) source).setParameterNotNull(MomStructureFractalZop.PARAM_MODEL_GRID_PRECISION, value);
    }

}
