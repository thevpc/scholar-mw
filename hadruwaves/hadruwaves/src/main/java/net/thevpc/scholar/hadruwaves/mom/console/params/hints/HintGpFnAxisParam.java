package net.thevpc.scholar.hadruwaves.mom.console.params.hints;

import net.thevpc.scholar.hadruplot.console.params.AbstractCParam;
import net.thevpc.scholar.hadruwaves.mom.HintAxisType;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 14 oct. 2006 12:13:57
 */
public class HintGpFnAxisParam extends AbstractCParam implements Cloneable{
    public static final String NAME="HintGpFnAxis";
    public HintGpFnAxisParam() {
        super(NAME);
    }

    @Override
    public void configure(Object source, Object value) {
        ((MomStructure) source).getHintsManager().setHintAxisType((HintAxisType) value);
    }
}
