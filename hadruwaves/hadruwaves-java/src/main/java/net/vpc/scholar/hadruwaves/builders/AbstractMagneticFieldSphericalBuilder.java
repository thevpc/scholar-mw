package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.MonitoredAction;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.Vector;
import net.vpc.scholar.hadrumaths.cache.CacheKey;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.convergence.ConvergenceEvaluator;
import net.vpc.common.mon.TaskMonitorManager;
import net.vpc.scholar.hadruwaves.ApertureType;
import net.vpc.scholar.hadruwaves.mom.StrSubCacheSupport;
import net.vpc.scholar.hadruwaves.str.MWStructure;

import static net.vpc.scholar.hadrumaths.Maths.invokeMonitoredAction;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractMagneticFieldSphericalBuilder extends AbstractValueBuilder implements MagneticFieldSphericalBuilder {
    private final ApertureType apertureType;

    public AbstractMagneticFieldSphericalBuilder(MWStructure momStructure, ApertureType apertureType) {
        super(momStructure);
        this.apertureType = apertureType==null?ApertureType.PEC:ApertureType.PEC;
    }

    public ApertureType getApertureType() {
        return apertureType;
    }

    @Override
    public MagneticFieldSphericalBuilder monitor(ProgressMonitor monitor) {
        return (MagneticFieldSphericalBuilder) super.monitor(monitor);
    }

    @Override
    public MagneticFieldSphericalBuilder monitor(TaskMonitorManager monitor) {
        return (MagneticFieldSphericalBuilder) super.monitor(monitor);
    }


    @Override
    public MagneticFieldSphericalBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (MagneticFieldSphericalBuilder) super.converge(convergenceEvaluator);
    }


    @Override
    public ComplexMatrix evalModuleMatrix(double[] theta, double[] phi, final double r) {
        return new StrSubCacheSupport<ComplexMatrix>(getStructure(), "magnetic-field-module", CacheKey.obj("magnetic-field-module"
                , "theta", theta, "phi", phi, "r", r
                , "apertureType", getApertureType()
        ), getMonitor()) {
            @Override
            public ComplexMatrix eval(ObjectCache momCache) {
                return invokeMonitoredAction(getMonitor(), getClass().getSimpleName(), new MonitoredAction<ComplexMatrix>() {
                    @Override
                    public ComplexMatrix process(final ProgressMonitor monitor, String messagePrefix) throws Exception {
                        Vector<ComplexMatrix> thetaPhi = getStructure().magneticField().spherical().monitor(monitor)
                                .evalMatrix(theta, phi, r);
                        ComplexMatrix Etheta=thetaPhi.get(Axis.THETA);
                        ComplexMatrix Ephi=thetaPhi.get(Axis.PHI);
                        return (Etheta.abs().dotsqr().add(Ephi.abs().dotsqr())).dotsqrt();
                    }
                });
            }
        }.evalCached();
    }

    @Override
    public ComplexMatrix evalModuleMatrix(double[] theta, double phi, double[] r) {
        return new StrSubCacheSupport<ComplexMatrix>(getStructure(), "magnetic-field-module", CacheKey.obj("magnetic-field-module"
                , "theta", theta, "phi", phi, "r", r
                , "apertureType", getApertureType()
        ), getMonitor()) {
            @Override
            public ComplexMatrix eval(ObjectCache momCache) {
                return invokeMonitoredAction(getMonitor(), getClass().getSimpleName(), new MonitoredAction<ComplexMatrix>() {
                    @Override
                    public ComplexMatrix process(final ProgressMonitor monitor, String messagePrefix) throws Exception {
                        Vector<ComplexMatrix> thetaPhi = getStructure().magneticField().spherical().monitor(monitor)
                                .evalMatrix(theta, phi, r);
                        ComplexMatrix Etheta=thetaPhi.get(Axis.THETA);
                        ComplexMatrix Ephi=thetaPhi.get(Axis.PHI);
                        return (Etheta.abs().dotsqr().add(Ephi.abs().dotsqr())).dotsqrt();
                    }
                });
            }
        }.evalCached();
    }

    @Override
    public ComplexMatrix evalModuleMatrix(double theta, double[] phi, double[] r) {
        return new StrSubCacheSupport<ComplexMatrix>(getStructure(), "magnetic-field-module", CacheKey.obj("magnetic-field-module"
                , "theta", theta, "phi", phi, "r", r
                , "apertureType", getApertureType()
        ), getMonitor()) {
            @Override
            public ComplexMatrix eval(ObjectCache momCache) {
                return invokeMonitoredAction(getMonitor(), getClass().getSimpleName(), new MonitoredAction<ComplexMatrix>() {
                    @Override
                    public ComplexMatrix process(final ProgressMonitor monitor, String messagePrefix) throws Exception {
                        Vector<ComplexMatrix> thetaPhi = getStructure().magneticField().spherical().monitor(monitor)
                                .evalMatrix(theta, phi, r);
                        ComplexMatrix Etheta=thetaPhi.get(Axis.THETA);
                        ComplexMatrix Ephi=thetaPhi.get(Axis.PHI);
                        return (Etheta.abs().dotsqr().add(Ephi.abs().dotsqr())).dotsqrt();
                    }
                });
            }
        }.evalCached();
    }
}
