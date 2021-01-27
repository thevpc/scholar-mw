package net.thevpc.scholar.hadruwaves.mom.console.params.hints;

import net.thevpc.scholar.hadruplot.console.params.AbstractCParam;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 14 oct. 2006 12:13:57
 */
public class HintBMatrixSparsifyParam extends AbstractCParam implements Cloneable{
    public static final String NAME="HintBMatrixSparsify";
    public HintBMatrixSparsifyParam() {
        super(NAME);
    }

    public void configure(Object source,Object value) {
        ((MomStructure) source).getHintsManager().setHintBMatrixSparcify((Float) value);
    }
}
