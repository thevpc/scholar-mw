package net.vpc.scholar.hadruwaves.mom.console.params;

import net.vpc.scholar.hadrumaths.AbstractParam;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 14 juil. 2005 11:44:11
 */
public class BoxWithParam extends AbstractParam implements Cloneable {
    public BoxWithParam() {
        super("boxWith");
    }
    public void configure(Object source,Object value) {
        ((MomStructure) source).setXdim((Double) value);
    }
}
