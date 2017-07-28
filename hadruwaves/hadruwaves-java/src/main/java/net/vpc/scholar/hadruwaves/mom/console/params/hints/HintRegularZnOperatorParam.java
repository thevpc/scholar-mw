package net.vpc.scholar.hadruwaves.mom.console.params.hints;

import net.vpc.scholar.hadrumaths.AbstractParam;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 14 oct. 2006 12:13:57
 */
public class HintRegularZnOperatorParam extends AbstractParam implements Cloneable{
    public static final String NAME="HintRegularZnOperator";
    public HintRegularZnOperatorParam() {
        super(NAME);
    }

//    public HintRegularZnOperatorParamSet(Boolean value) {
//        super(NAME, value);
//    }


    @Override
    public void configure(Object source, Object value) {
        ((MomStructure) source).getHintsManager().setHintRegularZnOperator((Boolean) value);
    }
}