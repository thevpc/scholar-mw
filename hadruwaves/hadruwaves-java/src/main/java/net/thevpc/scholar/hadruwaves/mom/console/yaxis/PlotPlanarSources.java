package net.thevpc.scholar.hadruwaves.mom.console.yaxis;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadruplot.PlotType;
import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.console.params.XParamSet;
import net.thevpc.scholar.hadruplot.PlotMatrix;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.scholar.hadruplot.console.yaxis.PlotAxisSeries;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

public class PlotPlanarSources extends PlotAxisSeries implements Cloneable {
    private Axis axis;

    public PlotPlanarSources(Axis axis, YType... type) {
        super("PlanarSources" + axis, type, PlotType.HEATMAP);
        this.axis = axis;
    }
    @Override
    protected PlotMatrix evalValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p) {
        return evalMatrix((MomStructure) structure,monitor,p);
    }

    public PlotPlanarSources(Axis axis, YType[] type, PlotType plotType) {
        super("PlanarSources" + axis, type, plotType);
        this.axis = axis;
        setPlotType(PlotType.HEATMAP);
    }

    protected PlotMatrix evalMatrix(MomStructure structure, ProgressMonitor monitor, ConsoleActionParams p) {
        XParamSet xAxis = (XParamSet) p.getAxis().getX();
        double[] y = structure.toYForDomainCoeff(xAxis.getY());
        double[] x = structure.toXForDomainCoeff(xAxis.getValues());
        Complex[][] c = structure.source().monitor(monitor).evalMatrix(axis, x, y, 0).getArray();
        return new PlotMatrix(c, x, y);
    }

}