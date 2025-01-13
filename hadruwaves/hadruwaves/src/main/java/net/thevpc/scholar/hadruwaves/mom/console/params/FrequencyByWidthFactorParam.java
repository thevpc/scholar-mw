package net.thevpc.scholar.hadruwaves.mom.console.params;

import net.thevpc.scholar.hadruplot.console.params.AbstractCParam;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

import static net.thevpc.scholar.hadrumaths.Maths.C;

public class FrequencyByWidthFactorParam extends AbstractCParam implements Cloneable {
    public FrequencyByWidthFactorParam() {
        super("xdim/lamda");
    }


    public void configure(Object source,Object value) {
        MomStructure str = (MomStructure) source;
        str.setFrequency(((Double) value) * C / str.getDomain().xwidth());
//        p3Abstract.setAByLamdaFactor(getValueImpl());
//        p3Abstract.setB();
    }
}
