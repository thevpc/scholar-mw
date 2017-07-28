package net.vpc.scholar.hadruwaves.mom.console.params;

import net.vpc.scholar.hadrumaths.AbstractParam;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 14 juil. 2005 11:43:56
 */
public class FractalScaleParam extends AbstractParam implements Cloneable{

    public FractalScaleParam() {
        super("k");
    }

    public void configure(Object source,Object value) {
        ((MomStructure) source).setK((Integer) value);
    }

}
