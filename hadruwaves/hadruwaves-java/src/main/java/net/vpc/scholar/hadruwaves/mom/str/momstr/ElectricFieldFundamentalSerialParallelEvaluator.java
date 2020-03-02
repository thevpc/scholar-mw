package net.vpc.scholar.hadruwaves.mom.str.momstr;

import net.vpc.common.mon.MonitoredAction;
import net.vpc.common.mon.ProgressMonitors;
import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.double2complex.CDiscrete;
import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;

import static net.vpc.scholar.hadrumaths.Maths.exp;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.ProjectType;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.str.ElectricFieldFundamentalEvaluator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 août 2007 09:15:31
 */
public class ElectricFieldFundamentalSerialParallelEvaluator implements ElectricFieldFundamentalEvaluator {
    public static final ElectricFieldFundamentalSerialParallelEvaluator INSTANCE=new ElectricFieldFundamentalSerialParallelEvaluator();
    @Override
    public VDiscrete evaluate(MomStructure str, final double[] x, final double[] y, final double[] z, ProgressMonitor cmonitor) {
        ProgressMonitor monitor = ProgressMonitors.nonnull(cmonitor);

        final ModeInfo[] indexes = str.getModes(monitor);
        ModeInfo[] evan = str.getModeFunctions().getVanishingModes();
        ModeInfo[] prop = str.getModeFunctions().getPropagatingModes();
        if (str.getProjectType().equals(ProjectType.PLANAR_STRUCTURE)) {
            evan = indexes;
            prop = new ModeInfo[0];
        }
        if (str.getHintsManager().isHintRegularZnOperator()) {
            evan = indexes;
        }
        final ModeInfo[] finalProp = prop;
        final ModeInfo[] finalEvan = evan;
        return Maths.invokeMonitoredAction(monitor, getClass().getSimpleName(), new MonitoredAction<VDiscrete>() {
            @Override
            public VDiscrete process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                MutableComplex[][][] fx = MutableComplex.createArray(Maths.CZERO,z.length,y.length,x.length);
                MutableComplex[][][] fy = MutableComplex.createArray(Maths.CZERO,z.length,y.length,x.length);
                MutableComplex[][][] fz = MutableComplex.createArray(Maths.CZERO,z.length,y.length,x.length);
                double Z;
                Complex[][] xvals;
                Complex[][] yvals;
                ModeInfo mode;
                int y_length = y.length;
                int x_length = x.length;
                int z_length = z.length;
                int prop_length = finalProp.length;
                int indexes_length = indexes.length;
                int evan_length = finalEvan.length;
                String clsName = getClass().getSimpleName();

                for (int i = 0; i < prop_length; i++) {
                    mode = finalProp[i];
                    xvals = mode.fn.getComponent(Axis.X).toDC().evalComplex(x, y);
                    yvals = mode.fn.getComponent(Axis.Y).toDC().evalComplex(x, y);
                    monitor.setProgress((1.0 * (i + evan_length) / indexes_length), clsName);
                    for (int zi = 0; zi < z_length; zi++) {
                        Z = z[zi];
                        Complex gammaZ = exp((Z<0?mode.firstBoxSpaceGamma:mode.secondBoxSpaceGamma).mul(-Z));
//                Complex gammaZ =  MutableComplex.forComplex(mode.firstBoxSpaceGamma).mul(-Z).exp().toComplex();
                        for (int xi = 0; xi < x_length; xi++) {
                            for (int yi = 0; yi < y_length; yi++) {
                                fx[zi][yi][xi].addProduct((xvals[yi][xi]),(gammaZ));
                                fy[zi][yi][xi].addProduct((yvals[yi][xi]),(gammaZ));
                            }
                        }
                    }
                }
                Domain domain = Domain.ofBounds(x[0], x[1], y[0], y[1], z[0], z[1]);
                return new VDiscrete(CDiscrete.of(domain,MutableComplex.toComplex(fx)),
                        CDiscrete.of(domain,MutableComplex.toComplex(fy)),
                        CDiscrete.of(domain,MutableComplex.toComplex(fz)));
            }
        });
        }

    @Override
    public String toString() {
        return dump();
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.function(getClass().getSimpleName()).build();
    }

}
