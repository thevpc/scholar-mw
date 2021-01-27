package net.thevpc.scholar.hadruwaves.mom.str.momstr;

import net.thevpc.common.mon.MonitoredAction;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.CDiscrete;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.scholar.hadrumaths.util.ArrayUtils;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadruwaves.str.MWStructure;
import net.thevpc.scholar.hadruwaves.str.ElectricFieldEvaluator;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.scholar.hadrumaths.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 août 2007 09:15:31
 */
public class ElectricFieldParallelEvaluator implements ElectricFieldEvaluator {
    public static final ElectricFieldParallelEvaluator INSTANCE=new ElectricFieldParallelEvaluator();
    @Override
    public VDiscrete evaluate(MWStructure structure, final double[] x, final double[] y, double[] z, ProgressMonitor cmonitor) {
        ProgressMonitor monitor = ProgressMonitors.nonnull(cmonitor);
        final MomStructure str=(MomStructure) structure;
        monitor = ProgressMonitors.nonnull(monitor);
        final String clsName = getClass().getSimpleName();
        return Maths.invokeMonitoredAction(monitor, clsName, new MonitoredAction<VDiscrete>() {
            @Override
            public VDiscrete process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                ComplexMatrix sp = str.getTestModeScalarProducts(ProgressMonitors.none());
                ComplexMatrix Testcoeff = str.matrixX().monitor(monitor).evalMatrix();
                DoubleToVector[] _g = str.testFunctions().arr();

                Complex[] J = Testcoeff.getColumn(0).toArray();
                ModeInfo[] indexes = str.getModes();
                MutableComplex[][] xCube = MutableComplex.createArray(Maths.CZERO, y.length,x.length);
                MutableComplex[][] yCube = MutableComplex.createArray(Maths.CZERO, y.length,x.length);
                MutableComplex xtemp;
                MutableComplex ytemp;
                for (int i = 0; i < indexes.length; i++) {
                    ModeInfo index = indexes[i];
                    ComplexVector spc = sp.getColumn(index.index);

                    Complex[][] fx = index.fn.getComponent(Axis.X).toDC().evalComplex(x, y);
                    Complex[][] fy = index.fn.getComponent(Axis.Y).toDC().evalComplex(x, y);
                    ProgressMonitors.setProgress(monitor, i, indexes.length, clsName);
//            monitor.setProgress(1.0 * i / indexes.length);
                    for (int xi = 0; xi < x.length; xi++) {
                        for (int yi = 0; yi < y.length; yi++) {
                            xtemp = xCube[yi][xi];
                            ytemp = yCube[yi][xi];
                            for (int j = 0; j < _g.length; j++) {
                                Complex spcj = spc.get(j);
                                xtemp.addProduct(J[j], spcj,fx[yi][xi]);
                                ytemp.addProduct(J[j], spcj,fy[yi][xi]);
                            }
                        }
                    }
                }

        /*double[] */double[] z = new double[]{0};
                Domain domain = Domain.ofBounds(x[0], x[1], y[0], y[1], z[0], z[1]);
                return new VDiscrete(
                        CDiscrete.of(domain,new Complex[][][]{MutableComplex.toComplex(xCube)}),
                        CDiscrete.of(domain,new Complex[][][]{MutableComplex.toComplex(yCube)}),
                        CDiscrete.of(domain,ArrayUtils.fill(new Complex[1][y.length][x.length], Maths.CZERO))
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
        return Tson.function(getClass().getSimpleName()).build();
    }
}
