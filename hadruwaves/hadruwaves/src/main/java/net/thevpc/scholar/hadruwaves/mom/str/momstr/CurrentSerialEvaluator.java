package net.thevpc.scholar.hadruwaves.mom.str.momstr;

import net.thevpc.common.mon.MonitoredAction;
import net.thevpc.common.mon.VoidMonitoredAction;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.CDiscrete;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.scholar.hadrumaths.util.*;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.str.MWStructure;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.str.CurrentEvaluator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 août 2007 09:29:45
 */
public class CurrentSerialEvaluator implements CurrentEvaluator {
    public static final CurrentSerialEvaluator INSTANCE = new CurrentSerialEvaluator();

    @Override
    public VDiscrete evaluate(MWStructure structure, final double[] x, final double[] y, ProgressMonitor monitor) {
        final MomStructure str = (MomStructure) structure;
//        ProgressMonitor emonitor = ProgressMonitors.nonnull(monitor);
        final String monMessage = getClass().getSimpleName();
        return Maths.invokeMonitoredAction(monitor, monMessage, new MonitoredAction<VDiscrete>() {
            @Override
            public VDiscrete process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                ProgressMonitor[] mon = monitor.split(0.3, 0.2, 0.5);
                final ComplexMatrix sp = str.getTestModeScalarProducts(mon[0]);
                ComplexMatrix Testcoeff = str.matrixX().monitor(mon[1]).evalMatrix();
                final DoubleToVector[] _g = str.testFunctions().arr();

                final Complex[] J = Testcoeff.getColumn(0).toArray();
                final ModeInfo[] indexes = str.getModes();

                final MutableComplex[][] xCube = MutableComplex.createArray(Maths.CZERO, y.length, x.length);
                final MutableComplex[][] yCube = MutableComplex.createArray(Maths.CZERO, y.length, x.length);
                final ProgressMonitor mon2 = mon[2];
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
                            ComplexVector spn = sp.getColumn(indexIndex);
                            Complex[][] fx = index.fn.getComponent(Axis.X).toDC().evalComplex(x, y);
                            Complex[][] fy = index.fn.getComponent(Axis.Y).toDC().evalComplex(x, y);
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
                Domain domain = Domain.ofBounds(x[0], x[x.length-1], y[0], y[y.length-1], z[0], z[z.length-1]);
                return new VDiscrete(
                        CDiscrete.of(domain, new Complex[][][]{MutableComplex.toComplex(xCube)}),
                        CDiscrete.of(domain, new Complex[][][]{MutableComplex.toComplex(yCube)}),
                        CDiscrete.of(domain, ArrayUtils.fill(new Complex[1][y.length][x.length], Maths.CZERO))
                );
            }
        });
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
