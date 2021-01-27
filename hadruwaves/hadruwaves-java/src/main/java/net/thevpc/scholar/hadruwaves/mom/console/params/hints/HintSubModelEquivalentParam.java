package net.thevpc.scholar.hadruwaves.mom.console.params.hints;

import net.thevpc.scholar.hadruplot.console.params.AbstractCParam;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.str.zsfractalmodel.MomStructureFractalZop;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 14 oct. 2006 12:13:57
 */
public class HintSubModelEquivalentParam extends AbstractCParam implements Cloneable{
    public static final String NAME="HintSubModelEquivalent";

    public HintSubModelEquivalentParam() {
        super(NAME);
    }

    @Override
    public void configure(Object source,Object value) {
        ((MomStructure) source).setParameterNotNull(MomStructureFractalZop.HINT_SUB_MODEL_EQUIVALENT,value);
    }
}
