package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.scholar.hadruwaves.mom.DefaultPoyntingVectorCartesianBuilder;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public class DefaultPoyntingVectorBuilder extends AbstractPoyntingVectorBuilder {
    public DefaultPoyntingVectorBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public PoyntingVectorSphericalBuilder spherical() {
        return new DefaultPoyntingVectorSphericalBuilder(getStructure()).converge(getConvergenceEvaluator()).monitor(getMonitor());
    }

    @Override
    public PoyntingVectorCartesianBuilder cartesian() {
        return new DefaultPoyntingVectorCartesianBuilder(getStructure()).converge(getConvergenceEvaluator()).monitor(getMonitor());
    }
}
