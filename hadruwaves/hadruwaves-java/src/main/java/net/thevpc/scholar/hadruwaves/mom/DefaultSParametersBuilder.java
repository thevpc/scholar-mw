package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruwaves.str.MWStructure;
import net.thevpc.scholar.hadruwaves.builders.AbstractSParametersBuilder;
import net.thevpc.scholar.hadruwaves.mom.sources.PlanarSources;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class DefaultSParametersBuilder extends AbstractSParametersBuilder {



    public DefaultSParametersBuilder(MWStructure momStructure) {
        super(momStructure);
    }

    public ComplexMatrix evalMatrixImpl(ProgressMonitor evalMonitor) {
        MomStructure momStructure=(MomStructure) getStructure();
        ComplexMatrix z = momStructure.inputImpedance().monitor(getMonitor()).evalMatrix();
        ComplexMatrix z0 = null;
        switch (momStructure.getProjectType()) {
            case WAVE_GUIDE: {
                z0 = Maths.matrix(new Complex[][]{{momStructure.modeFunctions().getPropagatingModes()[0].impedance.impedanceValue()}});
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
