package net.vpc.scholar.hadruwaves.console.yaxis;

import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;

import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruplot.console.yaxis.PlotAxisCubes;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.ElectricFieldPart;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadruplot.console.ConsoleActionParams;
import net.vpc.scholar.hadruplot.console.params.XParamSet;

public class PlotElectricFieldEvanescent3D extends PlotAxisCubes implements Cloneable {
    public PlotElectricFieldEvanescent3D(YType... type) {
        super("ElectricFieldEvanescent",type);
    }

    protected VDiscrete evalValue(ConsoleAwareObject o, ProgressMonitor monitor, ConsoleActionParams p) {
        MomStructure structure=(MomStructure) o;
        XParamSet xAxis = (XParamSet) p.getAxis().getX();
        double[] x = structure.toXForDomainCoeff(xAxis.getValues());
        double[] z = structure.toZForDomainCoeff(xAxis.getZ());
        double[] y = structure.toYForDomainCoeff(xAxis.getY());

        return structure.electricField().electricPart(ElectricFieldPart.EVANESCENT).monitor(monitor)
                .cartesian().evalVDiscrete(x, y, z);
    }
}