package net.vpc.scholar.hadruwaves.mom.console.params.hints;

import net.vpc.scholar.hadruplot.console.params.AbstractParam;
import net.vpc.scholar.hadruwaves.mom.HintAxisType;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 14 oct. 2006 12:13:57
 */
public class HintGpFnAxisParam extends AbstractParam implements Cloneable{
    public static final String NAME="HintGpFnAxis";
    public HintGpFnAxisParam() {
        super(NAME);
    }

    @Override
    public void configure(Object source, Object value) {
        ((MomStructure) source).getHintsManager().setHintAxisType((HintAxisType) value);
    }
}
