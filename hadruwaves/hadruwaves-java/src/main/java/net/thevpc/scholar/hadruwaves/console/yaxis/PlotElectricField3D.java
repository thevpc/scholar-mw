package net.thevpc.scholar.hadruwaves.console.yaxis;

import net.thevpc.scholar.hadruplot.console.params.XParamSet;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;

import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.console.yaxis.PlotAxisCubes;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruplot.PlotHyperCube;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.scholar.hadruplot.console.PlotConfigManager;

public class PlotElectricField3D extends PlotAxisCubes implements Cloneable {
    public PlotElectricField3D(YType... type) {
        super("ElectricField",type);
    }

    protected PlotHyperCube evalValue(ConsoleAwareObject o, ProgressMonitor monitor, ConsoleActionParams p) {
        MomStructure structure=(MomStructure) o;
        XParamSet xAxis = (XParamSet) p.getAxis().getX();
        if(xAxis==null){
            throw new IllegalArgumentException("XParamSet Required");
        }
        double[] x = structure.toXForDomainCoeff(xAxis.getValues());
        double[] z = structure.toZForDomainCoeff(xAxis.getZ());
        double[] y = structure.toYForDomainCoeff(xAxis.getY());

        VDiscrete d = structure.electricField().monitor(monitor).cartesian().evalVDiscrete(x, y, z);
        return PlotConfigManager.getPlotHyperCubeResolvers().resolve(d);
    }
}