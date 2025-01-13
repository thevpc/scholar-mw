package net.thevpc.scholar.hadruwaves.mom.console.params;

import net.thevpc.scholar.hadruplot.console.params.AbstractCParam;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 14 juil. 2005 11:43:56
 */
public class FractalScaleParam extends AbstractCParam implements Cloneable{

    public FractalScaleParam() {
        super("k");
    }

    public void configure(Object source,Object value) {
        ((MomStructure) source).setFractalScale((Integer) value);
    }

}
