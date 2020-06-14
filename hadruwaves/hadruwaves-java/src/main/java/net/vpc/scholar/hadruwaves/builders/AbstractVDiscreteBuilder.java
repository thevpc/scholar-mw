package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.plot.convergence.ObjectEvaluator;
import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.vpc.scholar.hadrumaths.AbsoluteSamples;
import net.vpc.scholar.hadrumaths.Samples;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractVDiscreteBuilder extends AbstractValueBuilder implements CartesianFieldBuilder {
    public AbstractVDiscreteBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public CartesianFieldBuilder monitor(ProgressMonitor monitor) {
        return (CartesianFieldBuilder) super.monitor(monitor);
    }

    @Override
    public CartesianFieldBuilder monitor(net.vpc.common.mon.ProgressMonitorFactory monitor) {
        return (CartesianFieldBuilder) super.monitor(monitor);
    }

    @Override
    public CartesianFieldBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (CartesianFieldBuilder) super.converge(convergenceEvaluator);
    }

    protected ComplexVector evalVectorImpl(Axis axis, double[] x, double y, double z, ProgressMonitor monitor) {
        return evalVDiscreteImplLog(x, new double[]{y}, new double[]{z}, monitor).getComponentDiscrete(axis).getVector(Axis.X, Axis.Y, 0, Axis.Z, 0);
    }

    protected ComplexVector evalVectorImpl(Axis axis, double x, double[] y, double z, ProgressMonitor monitor) {
        return evalVDiscreteImplLog(new double[]{x}, y, new double[]{z}, monitor).getComponentDiscrete(axis).getVector(Axis.Y, Axis.X, 0, Axis.Z, 0);
    }

    protected ComplexVector evalVectorImpl(Axis axis, double x, double y, double[] z, ProgressMonitor monitor) {
        return evalVDiscreteImplLog(new double[]{x}, new double[]{y}, z, monitor).getComponentDiscrete(axis).getVector(Axis.Z, Axis.X, 0, Axis.Y, 0);
    }

    protected abstract VDiscrete evalVDiscreteImpl(double[] x, double[] y, double[] z, ProgressMonitor monitor);


    private VDiscrete evalVDiscreteImplLog(final double[] x, final double[] y, final double[] z, ProgressMonitor monitor) {
        return evalVDiscreteImpl(x, y, z, monitor);
//        return Maths.invokeMonitoredAction(
//                monitor, getClass().getName(),
//                new MonitoredAction<VDiscrete>() {
//                    @Override
//                    public VDiscrete process(ProgressMonitor monitor, String messagePrefix) throws Exception {
//                        return computeVDiscreteImpl(x, y, z, monitor);
//                    }
//                }
//        );
    }


    public ComplexVector evalVector(final Axis axis, final double[] x, final double y, final double z) {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return evalVectorImpl(axis, x, y, z, getMonitor());
        } else {
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public ComplexVector evaluate(Object momStructure, ProgressMonitor monitor) {
                    return evalVectorImpl(axis, x, y, z, monitor);
                }
            }, getMonitor()));
        }
    }

    public ComplexVector evalVector(final Axis axis, final double x, final double[] y, final double z) {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return evalVectorImpl(axis, x, y, z, getMonitor());
        } else {
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public ComplexVector evaluate(Object momStructure, ProgressMonitor monitor) {
                    return evalVectorImpl(axis, x, y, z, monitor);
                }
            }, getMonitor()));
        }
    }

    public ComplexVector evalVector(final Axis axis, final double x, final double y, final double[] z) {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return evalVectorImpl(axis, x, y, z, getMonitor());
        } else {
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public ComplexVector evaluate(Object momStructure, ProgressMonitor monitor) {
                    return evalVectorImpl(axis, x, y, z, monitor);
                }
            }, getMonitor()));
        }
    }


    public ComplexMatrix evalMatrix(final Axis axis, Samples samples) {
        AbsoluteSamples a = ((MomStructure) getStructure()).getDomain().toAbsolute(samples);
        double[] x = a.getX();
        switch (a.getDimension()) {
            case 1: {
                return evalMatrix(axis, x, new double[]{0}, 0);
            }
            case 2: {
                double[] y = a.getY();
                if (y == null || y.length == 0) {
                    y = new double[]{0};
                }
                return evalMatrix(axis, x, y, 0);
            }
            case 3: {
                double[] y = a.getY();
                if (y == null || y.length == 0) {
                    y = new double[]{0};
                }
                double[] z = a.getZ();
                if (z == null || z.length == 0) {
                    z = new double[]{0};
                }
                return evalMatrix(axis, x, y, z[0]);
            }
        }
        throw new IllegalArgumentException("Not Supported dimension, not a Matrix");
    }

    public ComplexVector evalVector(final Axis axis, Samples samples) {
        Domain d = ((MomStructure) getStructure()).getDomain();
        AbsoluteSamples a = d.toAbsolute(samples);
        double[] z = a.getZ();
        if (z == null || z.length == 0) {
            z = new double[]{0};
        }
        double[] y = a.getY();
        if (y == null || y.length == 0) {
            y = new double[]{d.getCenterY()};
        }
        double[] x = a.getX();
        if (x == null || x.length == 0) {
            x = new double[]{d.getCenterX()};
        }

        if (z.length <= 1 && y.length <= 1) {
            return evalVector(axis, x, y[0], z[0]);
        }
        if (x.length <= 1 && y.length <= 1) {
            return evalVector(axis, x[0], y[0], z);
        }
        if (x.length <= 1 && z.length <= 1) {
            return evalVector(axis, x[0], y, z[0]);
        }
        throw new IllegalArgumentException("Not Supported count, not a Vector");
    }

    public ComplexMatrix evalMatrix(final Axis axis, final double[] x, final double[] y, final double z) {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return evalXYMatrixImpl(x, y, z, axis, getMonitor());
        } else {
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public ComplexMatrix evaluate(Object momStructure, ProgressMonitor monitor) {
                    return evalXYMatrixImpl(x, y, z, axis, monitor);
                }
            }, getMonitor()));
        }
    }

    public ComplexMatrix evalMatrix(final Axis axis, final double[] x, final double y, final double[] z) {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return evalXYMatrixImpl(x, y, z, axis, getMonitor());
        } else {
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public ComplexMatrix evaluate(Object momStructure, ProgressMonitor monitor) {
                    return evalXYMatrixImpl(x, y, z, axis, monitor);
                }
            }, getMonitor()));
        }
    }


    public ComplexMatrix evalMatrix(final Axis axis, final double x, final double[] y, final double[] z) {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return evalXYMatrixImpl(x, y, z, axis, getMonitor());
        } else {
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public ComplexMatrix evaluate(Object momStructure, ProgressMonitor monitor) {
                    return evalXYMatrixImpl(x, y, z, axis, monitor);
                }
            }, getMonitor()));
        }
    }


    public VDiscrete evalVDiscrete(Samples samples) {
        samples = ((MomStructure) getStructure()).getDomain().toAbsolute(samples);
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return evalVDiscreteImpl(samples, getMonitor());
        } else {
            final Samples finalSamples = samples;
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public VDiscrete evaluate(Object momStructure, ProgressMonitor monitor) {
                    return evalVDiscreteImpl(finalSamples, monitor);
                }
            }, getMonitor()));
        }
    }


    public VDiscrete evalVDiscrete(final double[] x, final double[] y, final double[] z) {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return evalVDiscreteImplLog(x, y, z, getMonitor());
        } else {
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public VDiscrete evaluate(Object momStructure, ProgressMonitor monitor) {
                    return evalVDiscreteImplLog(x, y, z, monitor);
                }
            }, getMonitor()));
        }
    }

    protected ComplexMatrix evalXYMatrixImpl(double[] x, double[] y, double z, Axis axis, ProgressMonitor monitor) {
        return evalVDiscreteImplLog(x, y, new double[]{z}, monitor).getComponentDiscrete(axis).getMatrix(Axis.Z, 0);
    }

    protected ComplexMatrix evalXYMatrixImpl(double[] x, double y, double[] z, Axis axis, ProgressMonitor monitor) {
        return evalVDiscreteImplLog(x, new double[]{y}, z, monitor).getComponentDiscrete(axis).getMatrix(Axis.Y, 0);
    }

    protected ComplexMatrix evalXYMatrixImpl(double x, double[] y, double[] z, Axis axis, ProgressMonitor monitor) {
        return evalVDiscreteImplLog(new double[]{x}, y, z, monitor).getComponentDiscrete(axis).getMatrix(Axis.X, 0);
    }

    public VDiscrete evalVDiscreteImpl(Samples samples, ProgressMonitor monitor) {
        AbsoluteSamples a = ((MomStructure) getStructure()).getDomain().toAbsolute(samples);
        return evalVDiscreteImplLog(a.getX(), a.getY(), a.getZ(), monitor);
    }

    @Override
    public Expr expr() {
        return new CartesianFieldBuilderExprDV(this, getStructure().domain());
    }

    @Override
    public VDiscrete evalVDiscrete(double[] x, double[] y) {
        return evalVDiscrete(x, y, new double[]{0});
    }

}
