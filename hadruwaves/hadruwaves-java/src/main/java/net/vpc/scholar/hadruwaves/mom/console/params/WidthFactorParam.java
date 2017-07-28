package net.vpc.scholar.hadruwaves.mom.console.params;

import net.vpc.scholar.hadrumaths.AbstractParam;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 14 juil. 2005 11:44:11
 */
public class WidthFactorParam extends AbstractParam implements Cloneable {

    public WidthFactorParam() {
        super("WidthFactor");
    }

    public void configure(Object source,Object value) {
        Double lamdaFactor = (Double) value;
        if (!Double.isNaN(lamdaFactor)) {
            ((MomStructure) source).setWidthFactor(lamdaFactor);
        }
    }
}
