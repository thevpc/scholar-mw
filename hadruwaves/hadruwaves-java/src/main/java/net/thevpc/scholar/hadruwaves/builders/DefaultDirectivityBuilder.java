package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public class DefaultDirectivityBuilder extends AbstractDirectivityBuilder {
    public DefaultDirectivityBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public DirectivitySphericalBuilder spherical() {
        return new DefaultDirectivitySphericalBuilder(getStructure()).monitor(getMonitor()).converge(getConvergenceEvaluator());
    }
}
