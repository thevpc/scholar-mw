package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadruwaves.str.MWStructure;
import net.vpc.scholar.hadruwaves.builders.AbstractSParametersBuilder;
import net.vpc.scholar.hadruwaves.mom.sources.PlanarSources;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class DefaultSParametersBuilder extends AbstractSParametersBuilder {



    public DefaultSParametersBuilder(MWStructure momStructure) {
        super(momStructure);
    }

    public ComplexMatrix computeMatrixImpl() {
        MomStructure momStructure=(MomStructure) getStructure();
        ComplexMatrix z = momStructure.inputImpedance().monitor(getMonitor()).computeMatrix();
        ComplexMatrix z0 = null;
        switch (momStructure.getProjectType()) {
            case WAVE_GUIDE: {
                z0 = Maths.matrix(new Complex[][]{{momStructure.getModeFunctions().getPropagatingModes()[0].impedance.impedanceValue()}});
                break;
            }
            case PLANAR_STRUCTURE: {
                z0 = Maths.matrix(new Complex[][]{{((PlanarSources) momStructure.getSources()).getPlanarSources()[0].getCharacteristicImpedance()}});
                break;
            }
        }
        ComplexMatrix x = z.div(z0);

        ComplexMatrix id = Maths.identityMatrix(x);

        return (x.sub(id)).div(x.add(id));
    }


}
