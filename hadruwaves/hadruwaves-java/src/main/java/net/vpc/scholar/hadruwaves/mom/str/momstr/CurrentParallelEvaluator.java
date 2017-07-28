package net.vpc.scholar.hadruwaves.mom.str.momstr;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductCache;
import net.vpc.scholar.hadrumaths.symbolic.Discrete;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadrumaths.util.EnhancedComputationMonitor;
import net.vpc.scholar.hadrumaths.util.MonitoredAction;
import net.vpc.scholar.hadruwaves.str.MWStructure;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.ProjectType;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.str.CurrentEvaluator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 août 2007 09:29:59
 */
public class CurrentParallelEvaluator implements CurrentEvaluator {
    public static final CurrentParallelEvaluator INSTANCE = new CurrentParallelEvaluator();

    @Override
    public VDiscrete evaluate(MWStructure structure, double[] x, double[] y, ComputationMonitor monitor) {
//        EnhancedComputationMonitor emonitor=ComputationMonitorFactory.enhance(monitor);
        MomStructure str=(MomStructure) structure;
        return Maths.invokeMonitoredAction(monitor, getClass().getSimpleName(), new MonitoredAction<VDiscrete>() {
            @Override
            public VDiscrete process(EnhancedComputationMonitor monitor, String messagePrefix) throws Exception {
                DoubleToVector[] _g = str.getTestFunctions().arr();

                Matrix Testcoeff = str.matrixX().monitor(monitor).computeMatrix();


                Complex[] J = Testcoeff.getColumn(0).toArray();

//        FnIndexes[] n_eva = isHint(HINT_REGULAR_ZN_OPERATOR) ? fnBaseFunctions.getModes() : fnBaseFunctions.getVanishingModes();
                ModeInfo[] indexes = str.getModes();
                ModeInfo[] evan = str.getModeFunctions().getVanishingModes();
                ModeInfo[] prop = str.getModeFunctions().getPropagatingModes();
                if (str.getProjectType().equals(ProjectType.PLANAR_STRUCTURE)) {
                    evan = indexes;
                    prop = new ModeInfo[0];
                }
                if (str.getHintsManager().isHintRegularZnOperator()) {
                    evan = indexes;
                }
                MutableComplex[][][] fx = MutableComplex.createArray(Complex.ZERO, 1,y.length,x.length);
                MutableComplex[][][] fy = MutableComplex.createArray(Complex.ZERO, 1,y.length,x.length);
                MutableComplex[][][] fz = MutableComplex.createArray(Complex.ZERO, 1,y.length,x.length);
                ScalarProductCache sp = str.getTestModeScalarProducts(ComputationMonitorFactory.none());
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
                    xvals = mode.fn.getComponent(Axis.X).toDC().computeComplex(x, y);
                    yvals = mode.fn.getComponent(Axis.Y).toDC().computeComplex(x, y);
                    ComputationMonitorFactory.setProgress(monitor, i, indexes_length, getClass().getSimpleName());
//            monitor.setProgress((1.0 * i / (indexes.length)));
//            System.out.println("progress = " + monitor.getProgressValue());
                    zmnGammaZ = /*Complex.ONE.*/mode.impedance;
                    Vector spc = sp.getColumn(indexIndex);
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
                    xvals = mode.fn.getComponent(Axis.X).toDC().computeComplex(x, y);
                    yvals = mode.fn.getComponent(Axis.Y).toDC().computeComplex(x, y);
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
                return new VDiscrete(Discrete.create(MutableComplex.toComplex(fx), x, y, z), Discrete.create(MutableComplex.toComplex(fy), x, y, z), Discrete.create(MutableComplex.toComplex(fz), x, y, z));
            }
        });

//        emonitor.setProgress(0, "Start {1}", getClass().getSimpleName());
//        this.invalidateCache();
        }

    @Override
    public String toString() {
        return getClass().getName();
    }


    @Override
    public String dump() {
        return getClass().getName();
    }
}
