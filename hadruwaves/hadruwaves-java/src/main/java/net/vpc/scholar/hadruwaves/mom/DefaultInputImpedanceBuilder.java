package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadruwaves.str.MWStructure;
import net.vpc.scholar.hadruwaves.builders.AbstractInputImpedanceBuilder;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
class DefaultInputImpedanceBuilder extends AbstractInputImpedanceBuilder {


    public DefaultInputImpedanceBuilder(MWStructure momStructure) {
        super(momStructure);
    }

    public ComplexMatrix computeMatrixImpl() {
        MomStructure momStructure = (MomStructure)getStructure();
        return new ZinMatrixStrCacheSupport(momStructure, getMonitor()).get();
    }


}
