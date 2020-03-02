package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.cache.CacheKey;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.ApertureType;
import net.vpc.scholar.hadruwaves.builders.MagneticFieldCartesianBuilder;
import net.vpc.scholar.hadruwaves.builders.MagneticFieldSphericalBuilder;
import net.vpc.scholar.hadruwaves.str.MWStructure;
import net.vpc.scholar.hadruwaves.builders.AbstractMagneticFieldBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class DefaultMagneticFieldBuilder extends AbstractMagneticFieldBuilder {

    public DefaultMagneticFieldBuilder(MWStructure momStructure) {
        super(momStructure);
    }

    @Override
    public MagneticFieldCartesianBuilder cartesian() {
        return new DefaultMagneticFieldCartesianBuilder(getStructure()).converge(getConvergenceEvaluator()).monitor(getMonitor());
    }

    @Override
    public MagneticFieldSphericalBuilder spherical() {
        return new DefaultMagneticFieldSphericalBuilder(getStructure(), ApertureType.PEC).converge(getConvergenceEvaluator()).monitor(getMonitor());
    }
}
