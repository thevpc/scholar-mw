package net.vpc.scholar.hadruwaves.mom.console.params;

import net.vpc.scholar.hadrumaths.AbstractParam;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

import static net.vpc.scholar.hadrumaths.Maths.C;

public class FrequencyByWidthFactorParam extends AbstractParam implements Cloneable {
    public FrequencyByWidthFactorParam() {
        super("xdim/lamda");
    }


    public void configure(Object source,Object value) {
        MomStructure str = (MomStructure) source;
        str.setFrequency(((Double) value) * C / str.getXdim());
//        p3Abstract.setAByLamdaFactor(getValueImpl());
//        p3Abstract.setB();
    }
}
