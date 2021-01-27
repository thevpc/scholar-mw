package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.scholar.hadrumaths.cache.CacheKey;
import net.thevpc.scholar.hadrumaths.cache.ObjectCache;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.ApertureType;
import net.thevpc.scholar.hadruwaves.builders.MagneticFieldCartesianBuilder;
import net.thevpc.scholar.hadruwaves.builders.MagneticFieldSphericalBuilder;
import net.thevpc.scholar.hadruwaves.str.MWStructure;
import net.thevpc.scholar.hadruwaves.builders.AbstractMagneticFieldBuilder;

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
