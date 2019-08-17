package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.convergence.ObjectEvaluator;
import net.vpc.scholar.hadruplot.console.ProgressTaskMonitor;
import net.vpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractFarFieldBuilder extends AbstractValueBuilder implements FarFieldBuilder {

    public AbstractFarFieldBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public FarFieldBuilder monitor(ProgressMonitor monitor) {
        return (FarFieldBuilder) super.monitor(monitor);
    }

    @Override
    public FarFieldBuilder monitor(ProgressTaskMonitor monitor) {
        return (FarFieldBuilder) super.monitor(monitor);
    }


    @Override
    public FarFieldBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (FarFieldBuilder) super.converge(convergenceEvaluator);
    }

    @Override
    public TMatrix<Complex> computeMatrix(final SAxis axis, final double[] theta, final double[] phi, final double r) {
        if (axis != null) {
            switch (axis) {
                case THETA:
                case PHI:{
                    ConvergenceEvaluator conv = getConvergenceEvaluator();
                    if (conv == null) {
                        return computeMatrices(theta, phi, r).get(axis);
                    } else {
                        return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                            @Override
                            public TMatrix<Complex> evaluate(Object momStructure, ProgressMonitor monitor) {
                                return computeMatrices(theta, phi, r).get(axis);
                            }
                        }, getMonitor()));
                    }
                }
            }
        }
        throw new IllegalArgumentException("Unsupported Spherical Axis " + axis);
    }

    public TVector<TMatrix<Complex>> computeMatrices(final double[] theta, final double[] phi, final double r) {
        ConvergenceEvaluator conv = getConvergenceEvaluator();
        if (conv == null) {
            return computeFarFieldThetaPhiImpl(theta, phi, r, getMonitor());
        } else {
            return storeConvergenceResult(conv.evaluate(getStructure(), new ObjectEvaluator() {
                @Override
                public TVector<TMatrix<Complex>> evaluate(Object momStructure, ProgressMonitor monitor) {
                    return computeFarFieldThetaPhiImpl(theta, phi, r, monitor);
                }
            }, getMonitor()));
        }
    }

    protected abstract TVector<TMatrix<Complex>> computeFarFieldThetaPhiImpl(double[] theta, double[] phi, final double r, ProgressMonitor monitor);
}
