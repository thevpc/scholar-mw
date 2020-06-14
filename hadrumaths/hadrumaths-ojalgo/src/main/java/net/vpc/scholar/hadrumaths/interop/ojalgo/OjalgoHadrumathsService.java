package net.vpc.scholar.hadrumaths.interop.ojalgo;

import net.vpc.scholar.hadrumaths.HadrumathsService;
import net.vpc.scholar.hadrumaths.Maths;

public class OjalgoHadrumathsService implements HadrumathsService {
    @Override
    public void installService() {
        Maths.Config.registerMatrixFactory(OjalgoComplexMatrixFactory.INSTANCE);
    }
}
