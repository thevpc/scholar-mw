package net.vpc.scholar.hadruwaves.console.yaxis;

import net.vpc.scholar.hadrumaths.plot.console.params.XParamSet;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;

import net.vpc.scholar.hadrumaths.plot.console.ConsoleAwareObject;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.PlotAxisCubes;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;

public class PlotMagneticField3D extends PlotAxisCubes implements Cloneable {
    public PlotMagneticField3D(YType... type) {
        super("MagneticField",type);
    }

    protected VDiscrete computeValue(ConsoleAwareObject o, ComputationMonitor monitor, ConsoleActionParams p) {
        MomStructure structure=(MomStructure) o;
        XParamSet xAxis = (XParamSet) p.getAxis().getX();
        double[] x = structure.toXForDomainCoeff(xAxis.getValues());
        double[] z = structure.toZForDomainCoeff(xAxis.getZ());
        double[] y = structure.toYForDomainCoeff(xAxis.getY());

        return structure.magneticField().monitor(monitor).computeVDiscrete(x, y, z);
    }
}