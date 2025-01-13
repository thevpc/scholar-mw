package net.thevpc.scholar.hadruwaves.console.yaxis;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.PlotMatrix;
import net.thevpc.scholar.hadrumaths.util.ArrayUtils;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.scholar.hadruplot.PlotType;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.scholar.hadruplot.console.params.XParamSet;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;

import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruplot.console.yaxis.PlotAxisSeries;

@Deprecated
public class PlotElectricFieldNoSources extends PlotAxisSeries implements Cloneable {
    private Axis axis;

    public PlotElectricFieldNoSources(Axis axis, YType... type) {
        super("EBaseNoSources" + axis, type, PlotType.HEATMAP);
        this.axis = axis;
    }

    public PlotElectricFieldNoSources(Axis axis, YType[] type, PlotType plotType) {
        super("EBaseNoSources" + axis, type, plotType);
        this.axis = axis;
        setPlotType(PlotType.HEATMAP);
    }
    @Override
    protected PlotMatrix evalValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p) {
        return evalMatrix((MomStructure) structure,monitor,p);
    }

    protected PlotMatrix evalMatrix(MomStructure structure, ProgressMonitor monitor, ConsoleActionParams p) {
        ProgressMonitor emonitor = ProgressMonitors.nonnull(monitor);
//        emonitor.startm(getClass().getSimpleName());
        XParamSet xAxis = (XParamSet) p.getAxis().getX();
        double[] y = structure.toYForDomainCoeff(xAxis.getY());
        double[] x = structure.toXForDomainCoeff(xAxis.getValues());
        double[] z = structure.toZForDomainCoeff(xAxis.getZ());
        Complex[][] s = structure.source().monitor(this).evalMatrix(axis, x, y, 0).getArray();
        VDiscrete E = structure.electricField().monitor(this).cartesian().evalVDiscrete(x, y, z);
        Complex[][] e = E.getComponentDiscrete(axis).getValues()[0];
        Complex[][] r= ArrayUtils.add(e, s);
//        emonitor.terminatem(getClass().getSimpleName());
        return new PlotMatrix(r, x, y);
    }

}