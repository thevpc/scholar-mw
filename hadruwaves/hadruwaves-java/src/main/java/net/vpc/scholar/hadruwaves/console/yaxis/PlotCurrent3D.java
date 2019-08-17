package net.vpc.scholar.hadruwaves.console.yaxis;

import net.vpc.scholar.hadruplot.console.params.XParamSet;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;

import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruplot.console.yaxis.PlotAxisCubes;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadruplot.console.ConsoleActionParams;
import net.vpc.common.mon.ProgressMonitor;

public class PlotCurrent3D extends PlotAxisCubes implements Cloneable {
    public PlotCurrent3D(YType... type) {
        super("Current",type);
    }

    protected VDiscrete computeValue(ConsoleAwareObject o, ProgressMonitor monitor, ConsoleActionParams p) {
        MomStructure structure=(MomStructure) o;
        XParamSet xAxis = (XParamSet) p.getAxis().getX();
        double[] x = structure.toXForDomainCoeff(xAxis.getValues());
        double[] y = structure.toYForDomainCoeff(xAxis.getY());
        return structure.current().monitor(monitor).computeVDiscrete(x, y);
    }
}