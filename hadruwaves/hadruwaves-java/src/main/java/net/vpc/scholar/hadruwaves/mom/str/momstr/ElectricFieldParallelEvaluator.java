package net.vpc.scholar.hadruwaves.mom.str.momstr;

import net.vpc.scholar.hadrumaths.symbolic.Discrete;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.util.EnhancedProgressMonitor;
import net.vpc.scholar.hadrumaths.util.MonitoredAction;
import net.vpc.scholar.hadruwaves.str.MWStructure;
import net.vpc.scholar.hadruwaves.str.ElectricFieldEvaluator;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadrumaths.util.ProgressMonitor;
import net.vpc.scholar.hadrumaths.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductCache;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 août 2007 09:15:31
 */
public class ElectricFieldParallelEvaluator implements ElectricFieldEvaluator {
    public static final ElectricFieldParallelEvaluator INSTANCE=new ElectricFieldParallelEvaluator();
    @Override
    public VDiscrete evaluate(MWStructure structure, double[] x, double[] y, double[] z, ProgressMonitor cmonitor) {
        EnhancedProgressMonitor monitor = ProgressMonitorFactory.enhance(cmonitor);
        MomStructure str=(MomStructure) structure;
        monitor = ProgressMonitorFactory.enhance(monitor);
        String clsName = getClass().getSimpleName();
        return Maths.invokeMonitoredAction(monitor, clsName, new MonitoredAction<VDiscrete>() {
            @Override
            public VDiscrete process(EnhancedProgressMonitor monitor, String messagePrefix) throws Exception {
                TMatrix<Complex> sp = str.getTestModeScalarProducts(ProgressMonitorFactory.none());
                Matrix Testcoeff = str.matrixX().monitor(monitor).computeMatrix();
                DoubleToVector[] _g = str.getTestFunctions().arr();

                Complex[] J = Testcoeff.getColumn(0).toArray();
                ModeInfo[] indexes = str.getModes();
                MutableComplex[][] xCube = MutableComplex.createArray(Maths.CZERO, y.length,x.length);
                MutableComplex[][] yCube = MutableComplex.createArray(Maths.CZERO, y.length,x.length);
                MutableComplex xtemp;
                MutableComplex ytemp;
                for (int i = 0; i < indexes.length; i++) {
                    ModeInfo index = indexes[i];
                    TVector<Complex> spc = sp.getColumn(index.index);

                    Complex[][] fx = index.fn.getComponent(Axis.X).toDC().computeComplex(x, y);
                    Complex[][] fy = index.fn.getComponent(Axis.Y).toDC().computeComplex(x, y);
                    ProgressMonitorFactory.setProgress(monitor, i, indexes.length, clsName);
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
