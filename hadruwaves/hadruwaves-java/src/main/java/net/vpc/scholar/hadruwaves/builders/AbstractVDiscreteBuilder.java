package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.MonitoredAction;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.convergence.ObjectEvaluator;
import net.vpc.scholar.hadruplot.AbsoluteSamples;
import net.vpc.scholar.hadruplot.Samples;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadruplot.console.ProgressTaskMonitor;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractVDiscreteBuilder extends AbstractValueBuilder implements CartesianFieldBuilder {
    @Override
    public CartesianFieldBuilder monitor(ProgressMonitor monitor) {
        return (CartesianFieldBuilder) super.monitor(monitor);
    }

    @Override
    public CartesianFieldBuilder monitor(ProgressTaskMonitor monitor) {
        return (CartesianFieldBuilder) super.monitor(monitor);
    }

    @Override
    public CartesianFieldBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (CartesianFieldBuilder) super.converge(convergenceEvaluator);
    }

    public AbstractVDiscreteBuilder(MWStructure structure) {
        super(structure);
    }

    protected abstract VDiscrete computeVDiscreteImpl(double[] x, double[] y, double[] z, ProgressMonitor monitor);

    private VDiscrete computeVDiscreteImplLog(final double[] x, final double[] y, final double[] z, ProgressMonitor monitor) {
        return Maths.invokeMonitoredAction(
                monitor, getClass().getName(),
                new MonitoredAction<VDiscrete>() {
                    @Override
                    public VDiscrete process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                        return computeVDiscreteImpl(x, y, z, monitor);
                    }
                }
        );
    }


    protected Vector computeVectorImpl(Axis axis, double[] x, double y, double z, ProgressMonitor monitor) {
        return computeVDiscreteImplLog(x, new double[]{y}, new double[]{z}, monitor).getComponent(axis).getVector(Axis.X, Axis.Y, 0, Axis.Z, 0);
    }

    protected Vector computeVectorImpl(Axis axis, double x, double[] y, double z, ProgressMonitor monitor) {
        return computeVDiscreteImplLog(new double[]{x}, y, new double[]{z}, monitor).getComponent(axis).getVector(Axis.Y, Axis.X, 0, Axis.Z, 0);
    }

    protected Vector computeVectorImpl(Axis axis, double x, double y, double[] z, ProgressMonitor monitor) {
        return computeVDiscreteImplLog(new double[]{x}, new double[]{y}, z, monitor).getComponent(axis).getVector(Axis.Z, Axis.X, 0, Axis.Y, 0);
    }

    public Vector computeVector(final Axis axis, final double[] x, final double y, final double z) {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return computeVectorImpl(axis, x, y, z, getMonitor());
        } else {
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public Vector evaluate(Object momStructure, ProgressMonitor monitor) {
                    return computeVectorImpl(axis, x, y, z, monitor);
                }
            }, getMonitor()));
        }
    }

    public Vector computeVector(final Axis axis, final double x, final double[] y, final double z) {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return computeVectorImpl(axis, x, y, z, getMonitor());
        } else {
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public Vector evaluate(Object momStructure, ProgressMonitor monitor) {
                    return computeVectorImpl(axis, x, y, z, monitor);
                }
            }, getMonitor()));
        }
    }

    public Vector computeVector(final Axis axis, final double x, final double y, final double[] z) {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return computeVectorImpl(axis, x, y, z, getMonitor());
        } else {
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public Vector evaluate(Object momStructure, ProgressMonitor monitor) {
                    return computeVectorImpl(axis, x, y, z, monitor);
                }
            }, getMonitor()));
        }
    }


    public Matrix computeMatrix(final Axis axis, Samples samples) {
        AbsoluteSamples a = ((MomStructure) getStructure()).getDomain().toAbsolute(samples);
        double[] x = a.getX();
        switch (a.getDimension()) {
            case 1: {
                return computeMatrix(axis, x, new double[]{0}, 0);
            }
            case 2: {
                double[] y = a.getY();
                if (y == null || y.length == 0) {
                    y = new double[]{0};
                }
                return computeMatrix(axis, x, y, 0);
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
                return computeMatrix(axis, x, y, z[0]);
            }
        }
        throw new IllegalArgumentException("Not Supported dimension, not a Matrix");
    }

    public Vector computeVector(final Axis axis, Samples samples) {
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
            return computeVector(axis, x, y[0], z[0]);
        }
        if (x.length <= 1 && y.length <= 1) {
            return computeVector(axis, x[0], y[0], z);
        }
        if (x.length <= 1 && z.length <= 1) {
            return computeVector(axis, x[0], y, z[0]);
        }
        throw new IllegalArgumentException("Not Supported count, not a Vector");
    }

    public Matrix computeMatrix(final Axis axis, final double[] x, final double[] y, final double z) {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return computeXYMatrixImpl(x, y, z, axis, getMonitor());
        } else {
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public Matrix evaluate(Object momStructure, ProgressMonitor monitor) {
                    return computeXYMatrixImpl(x, y, z, axis, monitor);
                }
            }, getMonitor()));
        }
    }

    public Matrix computeMatrix(final Axis axis, final double[] x, final double y, final double[] z) {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return computeXYMatrixImpl(x, y, z, axis, getMonitor());
        } else {
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public Matrix evaluate(Object momStructure, ProgressMonitor monitor) {
                    return computeXYMatrixImpl(x, y, z, axis, monitor);
                }
            }, getMonitor()));
        }
    }


    public Matrix computeMatrix(final Axis axis, final double x, final double[] y, final double[] z) {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return computeXYMatrixImpl(x, y, z, axis, getMonitor());
        } else {
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public Matrix evaluate(Object momStructure, ProgressMonitor monitor) {
                    return computeXYMatrixImpl(x, y, z, axis, monitor);
                }
            }, getMonitor()));
        }
    }


    public VDiscrete computeVDiscrete(Samples samples) {
        samples = ((MomStructure) getStructure()).getDomain().toAbsolute(samples);
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return computeVDiscreteImpl(samples, getMonitor());
        } else {
            final Samples finalSamples = samples;
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public VDiscrete evaluate(Object momStructure, ProgressMonitor monitor) {
                    return computeVDiscreteImpl(finalSamples, monitor);
                }
            }, getMonitor()));
        }
    }


    public VDiscrete computeVDiscrete(final double[] x, final double[] y, final double[] z) {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return computeVDiscreteImplLog(x, y, z, getMonitor());
        } else {
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public VDiscrete evaluate(Object momStructure, ProgressMonitor monitor) {
                    return computeVDiscreteImplLog(x, y, z, monitor);
                }
            }, getMonitor()));
        }
    }

    protected Matrix computeXYMatrixImpl(double[] x, double[] y, double z, Axis axis, ProgressMonitor monitor) {
        return computeVDiscreteImplLog(x, y, new double[]{z}, monitor).getComponent(axis).getMatrix(Axis.Z, 0);
    }

    protected Matrix computeXYMatrixImpl(double[] x, double y, double[] z, Axis axis, ProgressMonitor monitor) {
        return computeVDiscreteImplLog(x, new double[]{y}, z, monitor).getComponent(axis).getMatrix(Axis.Y, 0);
    }

    protected Matrix computeXYMatrixImpl(double x, double[] y, double[] z, Axis axis, ProgressMonitor monitor) {
        return computeVDiscreteImplLog(new double[]{x}, y, z, monitor).getComponent(axis).getMatrix(Axis.X, 0);
    }

    public VDiscrete computeVDiscreteImpl(Samples samples, ProgressMonitor monitor) {
        AbsoluteSamples a = ((MomStructure) getStructure()).getDomain().toAbsolute(samples);
        return computeVDiscreteImplLog(a.getX(), a.getY(), a.getZ(), monitor);
    }

    @Override
    public Expr expr() {
        return new CartesianFieldBuilderExprDV(this,getStructure().domain());
    }
    @Override
    public VDiscrete computeVDiscrete(double[] x, double[] y) {
        return computeVDiscrete(x,y,new double[]{0});
    }

}
