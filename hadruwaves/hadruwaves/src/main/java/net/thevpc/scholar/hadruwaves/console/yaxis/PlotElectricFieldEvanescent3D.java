package net.thevpc.scholar.hadruwaves.console.yaxis;

import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;

import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.console.yaxis.PlotAxisCubes;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruplot.PlotHyperCube;
import net.thevpc.scholar.hadruwaves.mom.ElectricFieldPart;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.scholar.hadruplot.console.PlotConfigManager;
import net.thevpc.scholar.hadruplot.console.params.XParamSet;

public class PlotElectricFieldEvanescent3D extends PlotAxisCubes implements Cloneable {

    public PlotElectricFieldEvanescent3D(YType... type) {
        super("ElectricFieldEvanescent", type);
    }

    protected PlotHyperCube evalValue(ConsoleAwareObject o, ProgressMonitor monitor, ConsoleActionParams p) {
        MomStructure structure = (MomStructure) o;
        XParamSet xAxis = (XParamSet) p.getAxis().getX();
        double[] x = structure.toXForDomainCoeff(xAxis.getValues());
        double[] z = structure.toZForDomainCoeff(xAxis.getZ());
        double[] y = structure.toYForDomainCoeff(xAxis.getY());

        VDiscrete d = structure.electricField().electricPart(ElectricFieldPart.EVANESCENT).monitor(monitor)
                .cartesian().evalVDiscrete(x, y, z);
        return PlotConfigManager.getPlotHyperCubeResolvers().resolve(d);
    }
}
