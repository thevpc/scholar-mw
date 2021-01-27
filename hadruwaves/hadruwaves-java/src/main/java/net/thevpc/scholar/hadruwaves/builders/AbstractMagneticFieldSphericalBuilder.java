package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.common.mon.MonitoredAction;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.Vector;
import net.thevpc.scholar.hadrumaths.cache.CacheKey;
import net.thevpc.scholar.hadrumaths.cache.ObjectCache;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.scholar.hadruwaves.ApertureType;
import net.thevpc.scholar.hadruwaves.mom.StrSubCacheSupport;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

import static net.thevpc.scholar.hadrumaths.Maths.invokeMonitoredAction;

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
    public MagneticFieldSphericalBuilder monitor(net.thevpc.common.mon.ProgressMonitorFactory monitor) {
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
            public ComplexMatrix eval(ObjectCache momCache, ProgressMonitor cacheMonitor) {
                return invokeMonitoredAction(cacheMonitor, getClass().getSimpleName(), new MonitoredAction<ComplexMatrix>() {
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
            public ComplexMatrix eval(ObjectCache momCache, ProgressMonitor cacheMonitor) {
                return invokeMonitoredAction(cacheMonitor, getClass().getSimpleName(), new MonitoredAction<ComplexMatrix>() {
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
            public ComplexMatrix eval(ObjectCache momCache, ProgressMonitor cacheMonitor) {
                return invokeMonitoredAction(cacheMonitor, getClass().getSimpleName(), new MonitoredAction<ComplexMatrix>() {
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
