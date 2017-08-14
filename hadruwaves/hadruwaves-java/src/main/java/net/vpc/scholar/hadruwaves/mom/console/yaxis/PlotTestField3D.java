package net.vpc.scholar.hadruwaves.mom.console.yaxis;

import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;

import net.vpc.scholar.hadrumaths.plot.console.ConsoleAwareObject;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.PlotAxisCubes;
import net.vpc.scholar.hadrumaths.plot.console.params.XParamSet;
import net.vpc.scholar.hadrumaths.util.ProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;

public class PlotTestField3D extends PlotAxisCubes implements Cloneable {
    public PlotTestField3D(YType... type) {
        super("PlotTestField3D",type);
    }

    protected VDiscrete computeValue(ConsoleAwareObject o, ProgressMonitor monitor, ConsoleActionParams p) {
        MomStructure structure=(MomStructure) o;
        XParamSet xAxis = (XParamSet) p.getAxis().getX();
        double[] x = structure.toXForDomainCoeff(xAxis.getValues());
        double[] y = structure.toYForDomainCoeff(xAxis.getY());
        return structure.testField().monitor(monitor).computeVDiscrete(x, y);
    }
}