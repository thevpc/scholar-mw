package net.vpc.scholar.hadrumaths.plot.model;

import net.vpc.scholar.hadrumaths.AxisVector;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;
import net.vpc.scholar.hadruplot.CellPosition;
import net.vpc.scholar.hadruplot.PlotDomain;
import net.vpc.scholar.hadruplot.PlotSamples;
import net.vpc.scholar.hadruplot.model.BasePlotModel;


/**
 * Created by vpc on 6/4/17.
 */
public class AxisVectorPlotModel extends BasePlotModel {

    private AxisVector<?> vector;


    public AxisVectorPlotModel(AxisVector<?> vector) {
        this.vector=vector;
    }

    public AxisVector<?> getVector() {
        return vector;
    }
}
