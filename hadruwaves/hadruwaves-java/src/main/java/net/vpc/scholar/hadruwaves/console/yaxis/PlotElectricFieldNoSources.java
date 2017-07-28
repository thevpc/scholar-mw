package net.vpc.scholar.hadruwaves.console.yaxis;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.ComputationMonitorFactory;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleAwareObject;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.NamedMatrix;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadrumaths.plot.PlotType;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;
import net.vpc.scholar.hadrumaths.plot.console.params.XParamSet;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.util.EnhancedComputationMonitor;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.console.yaxis.PlotAxisSeries;

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
    protected NamedMatrix computeValue(ConsoleAwareObject structure, ComputationMonitor monitor, ConsoleActionParams p) {
        return computeMatrix((MomStructure) structure,monitor,p);
    }

    protected NamedMatrix computeMatrix(MomStructure structure, ComputationMonitor monitor, ConsoleActionParams p) {
        EnhancedComputationMonitor emonitor = ComputationMonitorFactory.enhance(monitor);
//        emonitor.startm(getClass().getSimpleName());
        XParamSet xAxis = (XParamSet) p.getAxis().getX();
        double[] y = structure.toYForDomainCoeff(xAxis.getY());
        double[] x = structure.toXForDomainCoeff(xAxis.getValues());
        double[] z = structure.toZForDomainCoeff(xAxis.getZ());
        Complex[][] s = structure.source().monitor(this).computeMatrix(axis, x, y, 0).getArray();
        VDiscrete E = structure.electricField().monitor(this).computeVDiscrete(x, y, z);
        Complex[][] e = E.getComponent(axis).getValues()[0];
        Complex[][] r= ArrayUtils.add(e, s);
//        emonitor.terminatem(getClass().getSimpleName());
        return new NamedMatrix(r, x, y);
    }

}