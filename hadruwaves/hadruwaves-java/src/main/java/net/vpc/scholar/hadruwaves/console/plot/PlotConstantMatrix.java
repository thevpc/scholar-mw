package net.vpc.scholar.hadruwaves.console.plot;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.ComputationMonitorFactory;
import net.vpc.scholar.hadrumaths.plot.PlotType;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleAwareObject;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.NamedMatrix;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadrumaths.util.EnhancedComputationMonitor;
import net.vpc.scholar.hadruwaves.mom.console.yaxis.PlotAxisSeries;

public class PlotConstantMatrix extends PlotAxisSeries implements Cloneable {
    private Axis axis;
    private NamedMatrix value;
    public PlotConstantMatrix(String name, NamedMatrix value, PlotType plotType) {
        super(name, new YType[]{YType.REFERENCE}, plotType);
        this.value=value;
    }


    @Override
    protected NamedMatrix computeValue(ConsoleAwareObject structure, ComputationMonitor monitor, ConsoleActionParams p) {
        EnhancedComputationMonitor m= ComputationMonitorFactory.enhance(monitor);
        String name = getName();
        m.start(name+", starting...");
        m.terminate(name+", terminated...");
        return value;
    }

}
