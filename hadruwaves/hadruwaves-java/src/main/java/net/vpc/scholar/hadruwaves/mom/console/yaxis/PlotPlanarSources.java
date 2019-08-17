package net.vpc.scholar.hadruwaves.mom.console.yaxis;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadruplot.PlotType;
import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruplot.console.params.XParamSet;
import net.vpc.scholar.hadruplot.PlotMatrix;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruplot.console.ConsoleActionParams;
import net.vpc.scholar.hadruplot.console.yaxis.PlotAxisSeries;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

public class PlotPlanarSources extends PlotAxisSeries implements Cloneable {
    private Axis axis;

    public PlotPlanarSources(Axis axis, YType... type) {
        super("PlanarSources" + axis, type, PlotType.HEATMAP);
        this.axis = axis;
    }
    @Override
    protected PlotMatrix computeValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p) {
        return computeMatrix((MomStructure) structure,monitor,p);
    }

    public PlotPlanarSources(Axis axis, YType[] type, PlotType plotType) {
        super("PlanarSources" + axis, type, plotType);
        this.axis = axis;
        setPlotType(PlotType.HEATMAP);
    }

    protected PlotMatrix computeMatrix(MomStructure structure, ProgressMonitor monitor, ConsoleActionParams p) {
        XParamSet xAxis = (XParamSet) p.getAxis().getX();
        double[] y = structure.toYForDomainCoeff(xAxis.getY());
        double[] x = structure.toXForDomainCoeff(xAxis.getValues());
        Complex[][] c = structure.source().monitor(monitor).computeMatrix(axis, x, y, 0).getArray();
        return new PlotMatrix(c, x, y);
    }

}