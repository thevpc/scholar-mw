package net.thevpc.scholar.hadruwaves.mom.console.params;

import net.thevpc.scholar.hadruplot.console.params.AbstractCParam;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.str.zsfractalmodel.MomStructureFractalZop;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 14 juil. 2005 11:43:56
 */
public class FractalModelBaseParam extends AbstractCParam implements Cloneable {

    public FractalModelBaseParam() {
        super("kminModel");
    }

    public void configure(Object source,Object value) {
        ((MomStructure) source).setParameterNotNull(MomStructureFractalZop.PARAM_MODEL_BASE_K_FACTOR,
                value);
    }

}
