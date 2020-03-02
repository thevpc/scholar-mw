package net.vpc.scholar.hadruwaves.console.yaxis;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.MomParamFactory;

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
            int fnMax_start = structure.getModeFunctions().getSize();
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
