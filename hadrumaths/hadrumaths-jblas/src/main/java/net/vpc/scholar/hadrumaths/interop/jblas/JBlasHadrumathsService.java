package net.vpc.scholar.hadrumaths.interop.jblas;

import net.vpc.scholar.hadrumaths.HadrumathsService;
import net.vpc.scholar.hadrumaths.Maths;

public class JBlasHadrumathsService implements HadrumathsService {
    @Override
    public void installService() {
        Maths.Config.registerMatrixFactory(JBlasComplexMatrixFactory.INSTANCE);
    }
}
