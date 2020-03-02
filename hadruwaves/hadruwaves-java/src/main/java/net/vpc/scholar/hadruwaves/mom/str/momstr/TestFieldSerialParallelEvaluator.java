package net.vpc.scholar.hadruwaves.mom.str.momstr;

import net.vpc.common.mon.MonitoredAction;
import net.vpc.common.mon.ProgressMonitors;
import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.double2complex.CDiscrete;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.vpc.scholar.hadrumaths.util.*;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.str.TestFieldEvaluator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 août 2007 09:36:20
 */
public class TestFieldSerialParallelEvaluator implements TestFieldEvaluator {
    public static final TestFieldSerialParallelEvaluator INSTANCE = new TestFieldSerialParallelEvaluator();

    @Override
    public VDiscrete evaluate(final MomStructure str, final double[] x, final double[] y, ProgressMonitor monitor) {
        return Maths.invokeMonitoredAction(monitor, getClass().getSimpleName(), new MonitoredAction<VDiscrete>() {
            @Override
            public VDiscrete process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                ComplexMatrix Testcoeff = str.matrixX().monitor(monitor).evalMatrix();
                DoubleToVector[] _g = str.getTestFunctions().arr();

                Complex[] J = Testcoeff.getColumn(0).toArray();
                Complex[][] xCube = ArrayUtils.fill(new Complex[y.length][x.length], Maths.CZERO);
                Complex[][] yCube = ArrayUtils.fill(new Complex[y.length][x.length], Maths.CZERO);
                for (int j = 0; j < _g.length; j++) {
                    ProgressMonitors.setProgress(monitor, j, _g.length, getClass().getSimpleName());
//            monitor.setProgress(1.0 * j / _g.length);
                    Complex[][] fx = _g[j].getComponent(Axis.X).toDC().evalComplex(x, y);
                    Complex[][] fy = _g[j].getComponent(Axis.Y).toDC().evalComplex(x, y);
                    for (int xi = 0; xi < x.length; xi++) {
                        for (int yi = 0; yi < y.length; yi++) {
                            xCube[yi][xi] = xCube[yi][xi].add(J[j].mul(fx[yi][xi]));
                            yCube[yi][xi] = yCube[yi][xi].add(J[j].mul(fy[yi][xi]));
                        }
                    }
                }

                double[] z = new double[]{0};
                Domain domain = Domain.ofBounds(x[0], x[1], y[0], y[1], z[0], z[1]);
                return new VDiscrete(
                        CDiscrete.of(domain,new Complex[][][]{xCube}),
                        CDiscrete.of(domain,new Complex[][][]{yCube}),
                        CDiscrete.of(domain,ArrayUtils.fill(new Complex[1][y.length][x.length], Maths.CZERO)
                        )
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
