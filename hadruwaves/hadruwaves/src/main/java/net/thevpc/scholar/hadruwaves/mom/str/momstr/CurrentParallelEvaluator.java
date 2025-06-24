package net.thevpc.scholar.hadruwaves.mom.str.momstr;

import net.thevpc.common.mon.MonitoredAction;
import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.CDiscrete;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.str.MWStructure;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.ProjectType;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.str.CurrentEvaluator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 août 2007 09:29:59
 */
public class CurrentParallelEvaluator implements CurrentEvaluator {
    public static final CurrentParallelEvaluator INSTANCE = new CurrentParallelEvaluator();

    @Override
    public VDiscrete evaluate(MWStructure structure, final double[] x, final double[] y, ProgressMonitor monitor) {
//        ProgressMonitor emonitor=ProgressMonitors.nonnull(monitor);
        final MomStructure str=(MomStructure) structure;
        return Maths.invokeMonitoredAction(monitor, getClass().getSimpleName(), new MonitoredAction<VDiscrete>() {
            @Override
            public VDiscrete process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                DoubleToVector[] _g = str.testFunctions().arr();

                ComplexMatrix Testcoeff = str.matrixX().monitor(monitor).evalMatrix();


                Complex[] J = Testcoeff.getColumn(0).toArray();

//        FnIndexes[] n_eva = isHint(HINT_REGULAR_ZN_OPERATOR) ? fnBaseFunctions.getModes() : fnBaseFunctions.getVanishingModes();
                ModeInfo[] indexes = str.getModes();
                ModeInfo[] evan = str.modeFunctions().getVanishingModes();
                ModeInfo[] prop = str.modeFunctions().getPropagatingModes();
                if (str.getProjectType().equals(ProjectType.PLANAR_STRUCTURE)) {
                    evan = indexes;
                    prop = new ModeInfo[0];
                }
                if (str.getHintsManager().isHintRegularZnOperator()) {
                    evan = indexes;
                }
                MutableComplex[][][] fx = MutableComplex.createArray(Maths.CZERO, 1,y.length,x.length);
                MutableComplex[][][] fy = MutableComplex.createArray(Maths.CZERO, 1,y.length,x.length);
                MutableComplex[][][] fz = MutableComplex.createArray(Maths.CZERO, 1,y.length,x.length);
                ComplexMatrix sp = str.getTestModeScalarProducts(ProgressMonitors.none());
                MutableComplex tempx;
                MutableComplex tempy;
                Complex zmnGammaZ;
                //double Z;
                Complex[][] xvals;
                Complex[][] yvals;
                ModeInfo mode;
                int indexes_length = indexes.length;
                int x_length = x.length;
                int y_length = y.length;
                int g_length = _g.length;
                int evan_length = evan.length;
                int prop_length = prop.length;
                for (int i = 0; i < evan_length; i++) {
                    mode = evan[i];
                    int indexIndex = mode.index;
                    xvals = mode.fn.getComponent(Axis.X).toDC().evalComplex(x, y);
                    yvals = mode.fn.getComponent(Axis.Y).toDC().evalComplex(x, y);
                    ProgressMonitors.setProgress(monitor, i, indexes_length, getClass().getSimpleName());
//            monitor.setProgress((1.0 * i / (indexes.length)));
//            System.out.println("progress = " + monitor.getProgressValue());
                    zmnGammaZ = /*Complex.ONE.*/mode.impedance.impedanceValue();
                    ComplexVector spc = sp.getColumn(indexIndex);
                    for (int yi = 0; yi < y_length; yi++) {
                        for (int xi = 0; xi < x_length; xi++) {
                            tempx = fx[0][yi][xi];
                            tempy = fy[0][yi][xi];
                            for (int gi = 0; gi < g_length; gi++) {
                                Complex val = J[gi].div(zmnGammaZ).mul(spc.get(gi));
                                tempx.addProduct(val,xvals[yi][xi]);
                                tempy.addProduct(val,yvals[yi][xi]);
                            }
                        }
                    }
                }
                for (int i = 0; i < prop_length; i++) {
                    mode = prop[i];
                    xvals = mode.fn.getComponent(Axis.X).toDC().evalComplex(x, y);
                    yvals = mode.fn.getComponent(Axis.Y).toDC().evalComplex(x, y);
//            ComputationMonitorUtils.setProgress(monitor,q,n,gfps.length,max);
                    monitor.setProgress((1.0 * (i + evan_length) / indexes_length), getClass().getSimpleName());
                    for (int xi = 0; xi < x_length; xi++) {
                        for (int yi = 0; yi < y_length; yi++) {
                            fx[0][yi][xi].add((xvals[yi][xi]));
                            fy[0][yi][xi].add((yvals[yi][xi]));
                        }
                    }
                }
                double[] z = new double[]{0};
                Domain domain = Domain.ofBounds(x[0], x[1], y[0], y[1], z[0], z[1]);
                return new VDiscrete(CDiscrete.of(domain,MutableComplex.toComplex(fx)),
                        CDiscrete.of(domain,MutableComplex.toComplex(fy)),
                        CDiscrete.of(domain,MutableComplex.toComplex(fz))
                );
            }
        });

//        emonitor.setProgress(0, "Start {1}", getClass().getSimpleName());
//        this.invalidateCache();
        }

    @Override
    public String toString() {
        return dump();
    }


    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.ofUplet(getClass().getSimpleName()).build();
    }
}
