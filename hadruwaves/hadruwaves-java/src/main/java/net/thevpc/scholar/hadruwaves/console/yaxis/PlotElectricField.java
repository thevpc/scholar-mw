package net.thevpc.scholar.hadruwaves.console.yaxis;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.MomParamFactory;

@Deprecated
public class PlotElectricField extends PlotElectricFieldAbstract implements Cloneable {
    public PlotElectricField(Axis axis, YType... type) {
        super("EBase" + axis, axis, type);
    }

    public PlotElectricField(double epsilon, int threshold, int fnstep, Axis axis, YType... type) {
        super(epsilon, threshold, fnstep, "EBase" + axis, axis, type);
    }

    protected Complex[][][] resolveE(MomStructure structure, double[] x, double[] y, double[] z, ProgressMonitor monitor) {
        VDiscrete E = null;
        if (isConvergenceFn()) {
            int fnMax_start = structure.modeFunctions().getSize();
            int[] fnMax_all = Maths.isteps(fnMax_start, fnMax_start + getFnstep() * 10000, getFnstep());
            ConvergenceEvaluator momStructureConvergenceEvaluator = ConvergenceEvaluator.of(MomParamFactory.params.modesCount(), fnMax_all)
                    .setThreshold(getEpsilon()).setStabilityIterations(getThreshold()).setMaxIterations(1000);
            E = structure.electricField().monitor(monitor).converge(
                    momStructureConvergenceEvaluator
            ).cartesian().evalVDiscrete(x, y, z);
        } else {
            E = structure.electricField().monitor(monitor).cartesian().evalVDiscrete(x, y, z);
        }
        return E.getComponentDiscrete(getAxis()).getValues();
    }
}
