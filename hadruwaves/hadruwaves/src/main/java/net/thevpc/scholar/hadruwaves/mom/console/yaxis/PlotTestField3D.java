package net.thevpc.scholar.hadruwaves.mom.console.yaxis;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.scholar.hadruplot.PlotHyperCube;

import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.console.yaxis.PlotAxisCubes;
import net.thevpc.scholar.hadruplot.console.params.XParamSet;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.scholar.hadruplot.console.PlotConfigManager;

public class PlotTestField3D extends PlotAxisCubes implements Cloneable {
    public PlotTestField3D(YType... type) {
        super("PlotTestField3D",type);
    }

    protected PlotHyperCube evalValue(ConsoleAwareObject o, ProgressMonitor monitor, ConsoleActionParams p) {
        MomStructure structure=(MomStructure) o;
        XParamSet xAxis = (XParamSet) p.getAxis().getX();
        double[] x = structure.toXForDomainCoeff(xAxis.getValues());
        double[] y = structure.toYForDomainCoeff(xAxis.getY());
        VDiscrete d = structure.testField().monitor(monitor).evalVDiscrete(x, y);
        return PlotConfigManager.getPlotHyperCubeResolvers().resolve(d);
    }
}