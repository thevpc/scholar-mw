package net.vpc.scholar.hadruwaves.mom.str.momstr;

import net.vpc.common.util.mon.MonitoredAction;
import net.vpc.common.util.mon.VoidMonitoredAction;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.Discrete;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadrumaths.util.*;
import net.vpc.common.util.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.str.MWStructure;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.str.CurrentEvaluator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 août 2007 09:29:45
 */
public class CurrentSerialEvaluator implements CurrentEvaluator {
    public static final CurrentSerialEvaluator INSTANCE = new CurrentSerialEvaluator();

    @Override
    public VDiscrete evaluate(MWStructure structure, double[] x, double[] y, ProgressMonitor monitor) {
        MomStructure str = (MomStructure) structure;
//        ProgressMonitor emonitor = ProgressMonitorFactory.nonnull(monitor);
        String monMessage = getClass().getSimpleName();
        return Maths.invokeMonitoredAction(monitor, monMessage, new MonitoredAction<VDiscrete>() {
            @Override
            public VDiscrete process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                ProgressMonitor[] mon = monitor.split(new double[]{0.3, 0.2,0.5});
                TMatrix<Complex> sp = str.getTestModeScalarProducts(mon[0]);
                Matrix Testcoeff = str.matrixX().monitor(mon[1]).computeMatrix();
                DoubleToVector[] _g = str.getTestFunctions().arr();

                Complex[] J = Testcoeff.getColumn(0).toArray();
                ModeInfo[] indexes = str.getModes();

                MutableComplex[][] xCube = MutableComplex.createArray(Maths.CZERO, y.length, x.length);
                MutableComplex[][] yCube = MutableComplex.createArray(Maths.CZERO, y.length, x.length);
                ProgressMonitor mon2 = mon[2];
                Maths.invokeMonitoredAction(mon[2], monMessage, new VoidMonitoredAction() {
                    @Override
                    public void invoke(ProgressMonitor monitor, String messagePrefix) throws Exception {
                        MutableComplex xtemp;
                        MutableComplex ytemp;
                        int g_length = _g.length;
                        int indexes_length = indexes.length;
                        int x_length = x.length;
                        int y_length = y.length;
                        for (int i = 0; i < indexes_length; i++) {
                            ModeInfo index = indexes[i];
                            int indexIndex = index.index;
                            TVector<Complex> spn = sp.getColumn(indexIndex);
                            Complex[][] fx = index.fn.getComponent(Axis.X).toDC().computeComplex(x, y);
                            Complex[][] fy = index.fn.getComponent(Axis.Y).toDC().computeComplex(x, y);
                            mon2.setProgress(i, indexes_length, monMessage);
                            for (int yi = 0; yi < y_length; yi++) {
                                MutableComplex[] xCube_yi = xCube[yi];
                                MutableComplex[] yCube_yi = yCube[yi];
                                Complex[] fx_yi = fx[yi];
                                Complex[] fy_yi = fy[yi];
                                for (int xi = 0; xi < x_length; xi++) {
                                    xtemp = xCube_yi[xi];
                                    ytemp = yCube_yi[xi];
                                    for (int j = 0; j < g_length; j++) {
                                        Complex spnj = spn.get(j);
                                        Complex Ji = J[j];
                                        xtemp.addProduct(Ji, spnj, (fx_yi[xi]));
                                        ytemp.addProduct(Ji, spnj, (fy_yi[xi]));
                                    }

                                }
                            }
                        }
                    }
                });
                double[] z = new double[]{0};
                return new VDiscrete(
                        Discrete.create(new Complex[][][]{MutableComplex.toComplex(xCube)}, x, y, z),
                        Discrete.create(new Complex[][][]{MutableComplex.toComplex(yCube)}, x, y, z),
                        Discrete.create(ArrayUtils.fill(new Complex[1][y.length][x.length], Maths.CZERO), x, y, z)
                );
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
