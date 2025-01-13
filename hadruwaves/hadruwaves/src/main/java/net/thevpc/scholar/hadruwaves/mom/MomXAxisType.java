package net.thevpc.scholar.hadruwaves.mom;


import net.thevpc.scholar.hadruplot.console.params.XAxisType;

public class MomXAxisType extends XAxisType {
    public static final MomXAxisType ITERATE_A_FACTOR_BY_FREQ = new MomXAxisType("ITERATE_A_FACTOR_BY_FREQ");
    public static final MomXAxisType ITERATE_A_FACTOR_BY_A = new MomXAxisType("ITERATE_A_FACTOR_BY_A");
    public static final MomXAxisType ITERATE_IRIS_QUOTIENT = new MomXAxisType("ITERATE_IRIS_QUOTIENT");
    public static final MomXAxisType ITERATE_X = new MomXAxisType("ITERATE_X");


    protected MomXAxisType(String name) {
        super(name);
    }
}
