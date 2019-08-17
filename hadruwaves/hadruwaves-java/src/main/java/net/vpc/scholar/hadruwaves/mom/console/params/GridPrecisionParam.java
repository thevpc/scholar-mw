package net.vpc.scholar.hadruwaves.mom.console.params;

import net.vpc.scholar.hadrumaths.meshalgo.rect.GridPrecision;
import net.vpc.scholar.hadruplot.console.params.AbstractParam;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.str.zsfractalmodel.MomStructureFractalZop;

public class GridPrecisionParam extends AbstractParam implements Cloneable {

    public GridPrecisionParam() {
        super("GpGridSubModel");
    }

    @Override
    public void configure(Object source,Object value) {
        GridPrecision currentValueIndex =(GridPrecision) value;
        ((MomStructure) source).setParameterNotNull(MomStructureFractalZop.PARAM_MODEL_SUB_GRID_PRECISION, currentValueIndex);
    }


}