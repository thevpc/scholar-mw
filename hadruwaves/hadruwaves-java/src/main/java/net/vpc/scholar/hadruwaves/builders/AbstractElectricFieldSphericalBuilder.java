package net.vpc.scholar.hadruwaves.builders;

import net.vpc.common.mon.MonitoredAction;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.Vector;
import net.vpc.scholar.hadrumaths.cache.CacheKey;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadruwaves.ApertureType;
import net.vpc.scholar.hadruwaves.mom.ElectricFieldPart;
import net.vpc.scholar.hadruwaves.mom.StrSubCacheSupport;
import net.vpc.scholar.hadruwaves.str.MWStructure;

import static net.vpc.scholar.hadrumaths.Maths.invokeMonitoredAction;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public abstract class AbstractElectricFieldSphericalBuilder extends AbstractValueBuilder implements ElectricFieldSphericalBuilder {
    private final ElectricFieldPart electricFieldPart;
    private final ApertureType apertureType;

    public AbstractElectricFieldSphericalBuilder(MWStructure momStructure, ElectricFieldPart part,ApertureType apertureType) {
        super(momStructure);
        this.electricFieldPart = part==null?ElectricFieldPart.FULL : part;
        this.apertureType = apertureType==null?ApertureType.PEC:ApertureType.PEC;
    }

    public ElectricFieldPart getElectricFieldPart() {
        return electricFieldPart;
    }

    public ApertureType getApertureType() {
        return apertureType;
    }

    @Override
    public ElectricFieldSphericalBuilder monitor(ProgressMonitor monitor) {
        return (ElectricFieldSphericalBuilder) super.monitor(monitor);
    }

    @Override
    public ElectricFieldSphericalBuilder monitor(net.vpc.common.mon.ProgressMonitorFactory monitor) {
        return (ElectricFieldSphericalBuilder) super.monitor(monitor);
    }


    @Override
    public ElectricFieldSphericalBuilder converge(ConvergenceEvaluator convergenceEvaluator) {
        return (ElectricFieldSphericalBuilder) super.converge(convergenceEvaluator);
    }


    @Override
    public ComplexMatrix evalModuleMatrix(double[] theta, double[] phi, final double r) {
        return new StrSubCacheSupport<ComplexMatrix>(getStructure(), "electric-field-module", CacheKey.obj("electric-field-module"
                , "theta", theta, "phi", phi, "r", r
                , "apertureType", getApertureType(), "fieldPart", getElectricFieldPart()
        ), getMonitor()) {
            @Override
            public ComplexMatrix eval(ObjectCache momCache, ProgressMonitor cacheMonitor) {
                return invokeMonitoredAction(cacheMonitor, getClass().getSimpleName(), new MonitoredAction<ComplexMatrix>() {
                    @Override
                    public ComplexMatrix process(final ProgressMonitor monitor, String messagePrefix) throws Exception {
                        Vector<ComplexMatrix> thetaPhi = getStructure().electricField().spherical().monitor(monitor)
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
        return new StrSubCacheSupport<ComplexMatrix>(getStructure(), "electric-field-module", CacheKey.obj("electric-field-module"
                , "theta", theta, "phi", phi, "r", r
                , "apertureType", getApertureType(), "fieldPart", getElectricFieldPart()
        ), getMonitor()) {
            @Override
            public ComplexMatrix eval(ObjectCache momCache, ProgressMonitor cacheMonitor) {
                return invokeMonitoredAction(cacheMonitor, getClass().getSimpleName(), new MonitoredAction<ComplexMatrix>() {
                    @Override
                    public ComplexMatrix process(final ProgressMonitor monitor, String messagePrefix) throws Exception {
                        Vector<ComplexMatrix> thetaPhi = getStructure().electricField().spherical().monitor(monitor)
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
        return new StrSubCacheSupport<ComplexMatrix>(getStructure(), "electric-field-module", CacheKey.obj("electric-field-module"
                , "theta", theta, "phi", phi, "r", r
                , "apertureType", getApertureType(), "fieldPart", getElectricFieldPart()
        ), getMonitor()) {
            @Override
            public ComplexMatrix eval(ObjectCache momCache, ProgressMonitor cacheMonitor) {
                return invokeMonitoredAction(cacheMonitor, getClass().getSimpleName(), new MonitoredAction<ComplexMatrix>() {
                    @Override
                    public ComplexMatrix process(final ProgressMonitor monitor, String messagePrefix) throws Exception {
                        Vector<ComplexMatrix> thetaPhi = getStructure().electricField().spherical().monitor(monitor)
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
