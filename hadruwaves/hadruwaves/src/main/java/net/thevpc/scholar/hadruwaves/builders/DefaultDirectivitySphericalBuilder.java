package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

/**
 * @author taha.bensalah@gmail.com on 7/16/16.
 */
public class DefaultDirectivitySphericalBuilder extends AbstractDirectivitySphericalBuilder {
    public DefaultDirectivitySphericalBuilder(MWStructure structure) {
        super(structure);
    }

    @Override
    public ComplexMatrix evalMatrix(double[] theta, double[] phi, double r) {
        ComplexMatrix Poy = getStructure().poyntingVector().monitor(getMonitor()).spherical().evalModuleMatrix(theta, phi, r);
        return Poy.div(Poy.avg());
    }

}
