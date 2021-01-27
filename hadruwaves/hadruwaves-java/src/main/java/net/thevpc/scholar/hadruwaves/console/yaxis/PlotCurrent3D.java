package net.thevpc.scholar.hadruwaves.console.yaxis;

import net.thevpc.scholar.hadruplot.console.params.XParamSet;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;

import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.console.yaxis.PlotAxisCubes;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruplot.PlotHyperCube;
import net.thevpc.scholar.hadruplot.console.PlotConfigManager;

public class PlotCurrent3D extends PlotAxisCubes implements Cloneable {
    public PlotCurrent3D(YType... type) {
        super("Current",type);
    }

    protected PlotHyperCube evalValue(ConsoleAwareObject o, ProgressMonitor monitor, ConsoleActionParams p) {
        MomStructure structure=(MomStructure) o;
        XParamSet xAxis = (XParamSet) p.getAxis().getX();
        double[] x = structure.toXForDomainCoeff(xAxis.getValues());
        double[] y = structure.toYForDomainCoeff(xAxis.getY());
        VDiscrete d = structure.current().monitor(monitor).evalVDiscrete(x, y);
        return PlotConfigManager.getPlotHyperCubeResolvers().resolve(d);
    }
}