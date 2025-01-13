package net.thevpc.scholar.hadruwaves.mom.console.params;

import net.thevpc.scholar.hadrumaths.meshalgo.rect.GridPrecision;
import net.thevpc.scholar.hadruplot.console.params.AbstractCParam;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.str.zsfractalmodel.MomStructureFractalZop;

public class GridPrecisionParam extends AbstractCParam implements Cloneable {

    public GridPrecisionParam() {
        super("GpGridSubModel");
    }

    @Override
    public void configure(Object source,Object value) {
        GridPrecision currentValueIndex =(GridPrecision) value;
        ((MomStructure) source).setParameterNotNull(MomStructureFractalZop.PARAM_MODEL_SUB_GRID_PRECISION, currentValueIndex);
    }


}