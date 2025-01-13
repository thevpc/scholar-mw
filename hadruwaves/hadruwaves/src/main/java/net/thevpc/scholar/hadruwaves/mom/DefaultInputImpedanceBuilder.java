package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadruwaves.str.MWStructure;
import net.thevpc.scholar.hadruwaves.builders.AbstractInputImpedanceBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class DefaultInputImpedanceBuilder extends AbstractInputImpedanceBuilder {


    public DefaultInputImpedanceBuilder(MWStructure momStructure) {
        super(momStructure);
    }

    public ComplexMatrix evalMatrixImpl(ProgressMonitor evalMonitor) {
        MomStructure momStructure = (MomStructure)getStructure();
        return new ZinMatrixStrCacheSupport(momStructure, getMonitor()).get();
    }


}
