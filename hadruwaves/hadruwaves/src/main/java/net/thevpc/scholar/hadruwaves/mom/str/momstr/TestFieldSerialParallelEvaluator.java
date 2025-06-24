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
import net.thevpc.scholar.hadrumaths.util.*;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.str.TestFieldEvaluator;

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
                DoubleToVector[] _g = str.testFunctions().arr();

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
                            xCube[yi][xi] = xCube[yi][xi].plus(J[j].mul(fx[yi][xi]));
                            yCube[yi][xi] = yCube[yi][xi].plus(J[j].mul(fy[yi][xi]));
                        }
                    }
                }

                double[] z = new double[]{0};
                Domain domain = Domain.ofBounds(x[0], x[1], y[0], y[1], z[0], z[0]);
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
        return Tson.ofUplet(getClass().getSimpleName()).build();
    }
}
