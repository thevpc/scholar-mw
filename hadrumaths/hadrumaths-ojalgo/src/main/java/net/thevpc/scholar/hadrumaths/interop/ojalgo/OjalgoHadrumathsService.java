package net.thevpc.scholar.hadrumaths.interop.ojalgo;

import net.thevpc.scholar.hadrumaths.HadrumathsService;
import net.thevpc.scholar.hadrumaths.Maths;

public class OjalgoHadrumathsService implements HadrumathsService {
    @Override
    public void installService() {
        Maths.Config.registerMatrixFactory(OjalgoComplexMatrixFactory.INSTANCE);
    }
}
