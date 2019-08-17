package net.vpc.scholar.hadruwaves.mom.str.momstr;


import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.Discrete;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.MonitoredAction;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.ProjectType;
import net.vpc.scholar.hadruwaves.str.ElectricFieldEvaluator;
import net.vpc.scholar.hadruwaves.str.MWStructure;

import static net.vpc.scholar.hadrumaths.Maths.exp;
import static net.vpc.scholar.hadrumaths.Maths.invokeMonitoredAction;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 août 2007 09:15:31
 */
public class ElectricFieldSerialEvaluator implements ElectricFieldEvaluator {
    public static final ElectricFieldSerialEvaluator INSTANCE = new ElectricFieldSerialEvaluator();

//    public VDiscrete evaluate(MWStructure structure, double[] x, double[] y, double[] z, ProgressMonitor monitor) {
//        VDiscrete v2=null;
//        MomStructure str=(MomStructure) structure;
//        str.getTestFunctions().arr();
//        Matrix Testcoeff = str.matrixX().monitor(ProgressMonitorFactory.none()).computeMatrix();
//        Testcoeff.getColumn(0).toArray();
//        ModeInfo[] indexes = str.getModes();
//        ModeInfo[] evan = str.getModeFunctions().getVanishingModes();
//        ModeInfo[] prop = str.getModeFunctions().getPropagatingModes();
//        ScalarProductCache sp = str.getTestModeScalarProducts(ProgressMonitorFactory.none());
//        for (int i = 0; i < 4; i++) {
//            Chronometer cr1=chrono();
//            VDiscrete v1=evaluate__01(structure, x, y, z, ProgressMonitorFactory.none());
//            cr1.stop();
//            Chronometer cr2=chrono();
//            v2=evaluate__02(structure, x, y, z, ProgressMonitorFactory.none());
//            cr2.stop();
//            System.out.println(cr1+" vs "+cr2);
//        }
//        monitor.setProgress(1,null);
//        return v2;
//    }

    public VDiscrete evaluate(MWStructure structure, final double[] x, final double[] y, final double[] z, ProgressMonitor monitor) {
        final MomStructure str = (MomStructure) structure;
        return Maths.invokeMonitoredAction(monitor, getClass().getSimpleName(), new MonitoredAction<VDiscrete>() {
            @Override
            public VDiscrete process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                ProgressMonitor[] mon = monitor.split(new double[]{2, 8});
//        this.invalidateCache();
                final DoubleToVector[] _g = str.getTestFunctions().arr();

                Matrix Testcoeff = str.matrixX().monitor(mon[0]).computeMatrix();


                final Complex[] J = Testcoeff.getColumn(0).toArray();
                if (J.length != _g.length) {
                    throw new IllegalStateException();
                }
//        FnIndexes[] n_eva = isHint(HINT_REGULAR_ZN_OPERATOR) ? fnBaseFunctions.getModes() : fnBaseFunctions.getVanishingModes();
                final ModeInfo[] indexes = str.getModes();
                ModeInfo[] evan = str.getModeFunctions().getVanishingModes();
                ModeInfo[] prop = str.getModeFunctions().getPropagatingModes();
                if (str.getProjectType().equals(ProjectType.PLANAR_STRUCTURE)) {
                    evan = indexes;
                    prop = new ModeInfo[0];
                }
                if (str.getHintsManager().isHintRegularZnOperator()) {
                    evan = indexes;
                }
                final ProgressMonitor mon1 = mon[1];
                final ModeInfo[] finalProp = prop;
                final ModeInfo[] finalEvan = evan;
                return Maths.invokeMonitoredAction(mon1, "Loop 1", new MonitoredAction<VDiscrete>() {
                    @Override
                    public VDiscrete process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                        MutableComplex[][][] fx = MutableComplex.createArray(Maths.CZERO, z.length, y.length, x.length);
                        MutableComplex[][][] fy = MutableComplex.createArray(Maths.CZERO, z.length, y.length, x.length);
                        MutableComplex[][][] fz = MutableComplex.createArray(Maths.CZERO, z.length, y.length, x.length);
                        TMatrix<Complex> sp = str.getTestModeScalarProducts(ProgressMonitorFactory.none());
                        MutableComplex tempx;
                        MutableComplex tempy;
                        Complex zmnGammaZ;
                        double Z;
                        Complex[][] xvals;
                        Complex[][] yvals;
                        ModeInfo mode;
                        String monText = getClass().getSimpleName();
                        int indexes_length = indexes.length;
                        int evan_length = finalEvan.length;
                        int z_length = z.length;
                        int y_length = y.length;
                        int x_length = x.length;
                        int g_length = _g.length;
                        for (int i = 0; i < evan_length; i++) {
                            mode = finalEvan[i];
                            TVector<Complex> spn = sp.getColumn(mode.index);
                            xvals = mode.fn.getComponent(Axis.X).toDC().computeComplex(x, y);
                            yvals = mode.fn.getComponent(Axis.Y).toDC().computeComplex(x, y);
                            mon1.setProgress(i, indexes_length, monText);
                            for (int zi = 0; zi < z_length; zi++) {
                                Z = z[zi];
                                //TODO Check if Z is always abs
                                if(Z < 0){
                                    zmnGammaZ = mode.firstBoxSpaceGamma.mul(Z).exp().mul(mode.impedance.impedanceValue());
                                }else{
                                    zmnGammaZ = mode.secondBoxSpaceGamma.mul(-Z).exp().mul(mode.impedance.impedanceValue());
                                }

                                for (int yi = 0; yi < y_length; yi++) {
                                    for (int xi = 0; xi < x_length; xi++) {
                                        tempx = fx[zi][yi][xi];
                                        tempy = fy[zi][yi][xi];
                                        for (int gi = 0; gi < g_length; gi++) {
                                            MutableComplex val = MutableComplex.forComplex(J[gi]);
                                            val.mul(zmnGammaZ);
                                            val.mul(spn.get(gi));
                                            tempx.addProduct(val, xvals[yi][xi]);
                                            tempy.addProduct(val, yvals[yi][xi]);
                                        }
                                    }
                                }
                            }
                        }
                        for (int i = 0; i < finalProp.length; i++) {
                            mode = finalProp[i];
                            xvals = mode.fn.getComponent(Axis.X).toDC().computeComplex(x, y);
                            yvals = mode.fn.getComponent(Axis.Y).toDC().computeComplex(x, y);
                            mon1.setProgress((1.0 * (i + evan_length) / indexes_length), monText);
                            for (int zi = 0; zi < z_length; zi++) {
                                Z = z[zi];
                                Complex gammaZ = (Z < 0 ? mode.firstBoxSpaceGamma : mode.secondBoxSpaceGamma).mul(-Z).exp();
                                for (int xi = 0; xi < x_length; xi++) {
                                    for (int yi = 0; yi < y_length; yi++) {
                                        fx[zi][yi][xi].addProduct(xvals[yi][xi], gammaZ);
                                        fy[zi][yi][xi].addProduct(yvals[yi][xi], gammaZ);
                                    }
                                }
                            }
                        }
                        return new VDiscrete(Discrete.create(MutableComplex.toComplex(fx), x, y, z), Discrete.create(MutableComplex.toComplex(fy), x, y, z), Discrete.create(MutableComplex.toComplex(fz), x, y, z));
                    }
                });
            }
        });
    }

    @Deprecated
    private VDiscrete evaluate__02(MWStructure structure, final double[] x, final double[] y, final double[] z, ProgressMonitor monitor) {
        final MomStructure str = (MomStructure) structure;

        return Maths.invokeMonitoredAction(monitor, getClass().getSimpleName(), new MonitoredAction<VDiscrete>() {
            @Override
            public VDiscrete process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                ProgressMonitor[] mon = monitor.split(new double[]{2, 8});
//        this.invalidateCache();
                final DoubleToVector[] _g = str.getTestFunctions().arr();

                Matrix Testcoeff = str.matrixX().monitor(mon[0]).computeMatrix();


                final Complex[] J = Testcoeff.getColumn(0).toArray();
                if (J.length != _g.length) {
                    throw new IllegalStateException();
                }
//        FnIndexes[] n_eva = isHint(HINT_REGULAR_ZN_OPERATOR) ? fnBaseFunctions.getModes() : fnBaseFunctions.getVanishingModes();
                final ModeInfo[] indexes = str.getModes();
                ModeInfo[] evan = str.getModeFunctions().getVanishingModes();
                ModeInfo[] prop = str.getModeFunctions().getPropagatingModes();
                if (str.getProjectType().equals(ProjectType.PLANAR_STRUCTURE)) {
                    evan = indexes;
                    prop = new ModeInfo[0];
                }
                if (str.getHintsManager().isHintRegularZnOperator()) {
                    evan = indexes;
                }
                final MutableComplex[][][] fx = new MutableComplex[z.length][y.length][x.length];
                final MutableComplex[][][] fy = new MutableComplex[z.length][y.length][x.length];
                final MutableComplex[][][] fz = new MutableComplex[z.length][y.length][x.length];
                final TMatrix<Complex> sp = str.getTestModeScalarProducts(ProgressMonitorFactory.none());
                final ModeInfo[] finalEvan = evan;
                final ModeInfo[] finalProp = prop;
                return invokeMonitoredAction(mon[1], "???", new MonitoredAction<VDiscrete>() {
                    @Override
                    public VDiscrete process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                        MutableComplex tempx;
                        MutableComplex tempy;
                        for (int zi = 0; zi < z.length; zi++) {
                            for (int yi = 0; yi < y.length; yi++) {
                                for (int xi = 0; xi < x.length; xi++) {
                                    fx[zi][yi][xi] = new MutableComplex();
                                    fy[zi][yi][xi] = new MutableComplex();
                                    fz[zi][yi][xi] = new MutableComplex();
                                }
                            }
                        }
                        Complex zmnGammaZ;
                        double Z;
                        Complex[][] xvals;
                        Complex[][] yvals;
                        ModeInfo mode;
                        String monText = getClass().getSimpleName();
                        for (int i = 0; i < finalEvan.length; i++) {
                            mode = finalEvan[i];
                            TVector<Complex> spc = sp.getColumn(mode.index);
                            xvals = mode.fn.getComponent(Axis.X).toDC().computeComplex(x, y);
                            yvals = mode.fn.getComponent(Axis.Y).toDC().computeComplex(x, y);
                            monitor.setProgress(i, indexes.length, monText);
//            monitor.setProgress((1.0 * i / (indexes.length)));
//            System.out.println("progress = " + monitor.getProgressValue());
                            for (int zi = 0; zi < z.length; zi++) {
                                Z = z[zi];
                                zmnGammaZ = /*Complex.ONE.*/mode.impedance.impedanceValue().mul(exp((Z < 0 ? mode.firstBoxSpaceGamma : mode.secondBoxSpaceGamma).mul(-Z)));
                                for (int yi = 0; yi < y.length; yi++) {
                                    for (int xi = 0; xi < x.length; xi++) {
                                        tempx = (fx[zi][yi][xi]);
                                        tempy = (fy[zi][yi][xi]);
                                        for (int gi = 0; gi < _g.length; gi++) {
                                            Complex val = J[gi].mul(zmnGammaZ).mul(spc.get(gi));
                                            tempx.add(val.mul(xvals[yi][xi]));
                                            tempy.add(val.mul(yvals[yi][xi]));
                                        }
                                    }
                                }
                            }
                        }
                        for (int i = 0; i < finalProp.length; i++) {
                            mode = finalProp[i];
                            xvals = mode.fn.getComponent(Axis.X).toDC().computeComplex(x, y);
                            yvals = mode.fn.getComponent(Axis.Y).toDC().computeComplex(x, y);
//            ComputationMonitorUtils.setProgress(monitor,q,n,gfps.length,max);
                            monitor.setProgress((1.0 * (i + finalEvan.length) / (indexes.length)), monText);
                            for (int zi = 0; zi < z.length; zi++) {
                                Z = z[zi];
                                Complex gammaZ = exp((Z < 0 ? mode.firstBoxSpaceGamma : mode.secondBoxSpaceGamma).mul(-Z));
                                for (int xi = 0; xi < x.length; xi++) {
                                    for (int yi = 0; yi < y.length; yi++) {
                                        fx[zi][yi][xi].add((xvals[yi][xi]).mul(gammaZ));
                                        fy[zi][yi][xi].add((yvals[yi][xi]).mul(gammaZ));
                                    }
                                }
                            }
                        }
                        return new VDiscrete(Discrete.create(MutableComplex.toImmutable(fx), x, y, z), Discrete.create(MutableComplex.toImmutable(fy), x, y, z), Discrete.create(MutableComplex.toImmutable(fz), x, y, z));
                    }
                });


            }
        });
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
