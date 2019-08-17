package net.vpc.scholar.hadruwaves.mom.console.params.hints;

import net.vpc.scholar.hadruplot.console.params.AbstractParam;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 14 oct. 2006 12:13:57
 */
public class HintAMatrixSparsifyParam extends AbstractParam implements Cloneable {

    public static final String NAME = "HintAMatrixSparsify";

    public HintAMatrixSparsifyParam() {
        super(NAME);
    }

    @Override
    public void configure(Object source, Object value) {
        ((MomStructure) source).getHintsManager().setHintAMatrixSparcify((Float) value);
    }
}
