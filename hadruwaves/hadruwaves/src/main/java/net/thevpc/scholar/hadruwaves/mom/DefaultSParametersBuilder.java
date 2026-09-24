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
        int pCount = z.getRowCount();
        Complex[][] z0Arr = new Complex[pCount][pCount];
        for (int i = 0; i < pCount; i++) {
            for (int j = 0; j < pCount; j++) {
                z0Arr[i][j] = Complex.ZERO;
            }
        }
        switch (momStructure.getProjectType()) {
            case WAVE_GUIDE: {
                net.thevpc.scholar.hadruwaves.ModeInfo[] propModes = momStructure.modeFunctions().getPropagatingModes();
                for (int i = 0; i < pCount; i++) {
                    Complex z0Val = (propModes != null && i < propModes.length)
                            ? propModes[i].impedance.impedanceValue()
                            : (propModes != null && propModes.length > 0)
                                ? propModes[0].impedance.impedanceValue()
                                : Complex.of(50);
                    z0Arr[i][i] = z0Val;
                }
                break;
            }
            case PLANAR_STRUCTURE: {
                PlanarSources ps = (PlanarSources) momStructure.getSources();
                net.thevpc.scholar.hadruwaves.mom.sources.PlanarSource[] planarSources = (ps != null) ? ps.getPlanarSources() : null;
                for (int i = 0; i < pCount; i++) {
                    Complex z0Val = (planarSources != null && i < planarSources.length && planarSources[i].getCharacteristicImpedance() != null)
                            ? planarSources[i].getCharacteristicImpedance()
                            : (planarSources != null && planarSources.length > 0 && planarSources[0].getCharacteristicImpedance() != null)
                                ? planarSources[0].getCharacteristicImpedance()
                                : Complex.of(50);
                    z0Arr[i][i] = z0Val;
                }
                break;
            }
        }
        ComplexMatrix z0Mat = Maths.matrix(z0Arr);

        // General multi-port S-parameters:
        // S = (Z - Z0) * (Z + Z0)^(-1)
        ComplexMatrix zMinusZ0 = z.sub(z0Mat);
        ComplexMatrix zPlusZ0 = z.add(z0Mat);
        return zMinusZ0.mul(zPlusZ0.inv());
    }


}
