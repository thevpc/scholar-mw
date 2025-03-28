package net.thevpc.scholar.hadrumaths.interop.jblas;

import net.thevpc.scholar.hadrumaths.HadrumathsService;
import net.thevpc.scholar.hadrumaths.HadrumathsServiceDesc;
import net.thevpc.scholar.hadrumaths.Maths;

@HadrumathsServiceDesc(order = 200)
public class JBlasHadrumathsService implements HadrumathsService {
    @Override
    public void installService() {
        Maths.Config.registerMatrixFactory(JBlasComplexMatrixFactory.INSTANCE);
    }
}
