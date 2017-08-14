package net.vpc.scholar.hadruwaves.mom.str.momstr;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.Discrete;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadrumaths.util.*;
import net.vpc.scholar.hadrumaths.util.EnhancedProgressMonitor;
import net.vpc.scholar.hadrumaths.util.ProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.str.TestFieldEvaluator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 août 2007 09:36:20
 */
public class TestFieldSerialParallelEvaluator implements TestFieldEvaluator {
    public static final TestFieldSerialParallelEvaluator INSTANCE = new TestFieldSerialParallelEvaluator();

    @Override
    public VDiscrete evaluate(MomStructure str, double[] x, double[] y, ProgressMonitor monitor) {
        return Maths.invokeMonitoredAction(monitor, getClass().getSimpleName(), new MonitoredAction<VDiscrete>() {
            @Override
            public VDiscrete process(EnhancedProgressMonitor monitor, String messagePrefix) throws Exception {
                Matrix Testcoeff = str.matrixX().monitor(monitor).computeMatrix();
                DoubleToVector[] _g = str.getTestFunctions().arr();

                Complex[] J = Testcoeff.getColumn(0).toArray();
                Complex[][] xCube = ArrayUtils.fill(new Complex[y.length][x.length], Maths.CZERO);
                Complex[][] yCube = ArrayUtils.fill(new Complex[y.length][x.length], Maths.CZERO);
                for (int j = 0; j < _g.length; j++) {
                    ProgressMonitorFactory.setProgress(monitor, j, _g.length, getClass().getSimpleName());
//            monitor.setProgress(1.0 * j / _g.length);
                    Complex[][] fx = _g[j].getComponent(Axis.X).toDC().computeComplex(x, y);
                    Complex[][] fy = _g[j].getComponent(Axis.Y).toDC().computeComplex(x, y);
                    for (int xi = 0; xi < x.length; xi++) {
                        for (int yi = 0; yi < y.length; yi++) {
                            xCube[yi][xi] = xCube[yi][xi].add(J[j].mul(fx[yi][xi]));
                            yCube[yi][xi] = yCube[yi][xi].add(J[j].mul(fy[yi][xi]));
                        }
                    }
                }

                double[] z = new double[]{0};
                return new VDiscrete(
                        Discrete.create(new Complex[][][]{xCube}, x, y, z),
                        Discrete.create(new Complex[][][]{yCube}, x, y, z),
                        Discrete.create(ArrayUtils.fill(new Complex[1][y.length][x.length], Maths.CZERO), x, y, z
                        )
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
