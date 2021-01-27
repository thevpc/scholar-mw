package net.thevpc.scholar.hadrumaths.plot.model;

import net.thevpc.scholar.hadrumaths.AxisVector;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.util.ArrayUtils;
import net.thevpc.scholar.hadruplot.CellPosition;
import net.thevpc.scholar.hadruplot.PlotDomain;
import net.thevpc.scholar.hadruplot.PlotSamples;
import net.thevpc.scholar.hadruplot.model.BasePlotModel;


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
