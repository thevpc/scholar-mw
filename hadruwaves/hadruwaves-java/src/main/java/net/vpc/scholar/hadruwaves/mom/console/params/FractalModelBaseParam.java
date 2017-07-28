package net.vpc.scholar.hadruwaves.mom.console.params;

import net.vpc.scholar.hadrumaths.AbstractParam;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.str.zsfractalmodel.MomStructureFractalZop;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 14 juil. 2005 11:43:56
 */
public class FractalModelBaseParam extends AbstractParam implements Cloneable {

    public FractalModelBaseParam() {
        super("kminModel");
    }

    public void configure(Object source,Object value) {
        ((MomStructure) source).setParameterNotNull(MomStructureFractalZop.PARAM_MODEL_BASE_K_FACTOR,
                value);
    }

}
