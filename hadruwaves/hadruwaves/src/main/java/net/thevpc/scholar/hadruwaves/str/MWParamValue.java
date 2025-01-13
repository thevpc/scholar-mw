package net.thevpc.scholar.hadruwaves.str;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */
public class MWParamValue {

    MWParamType type;
    Object value;

    private MWParamValue(MWParamType type, Object value) {
        this.type = type;
        this.value = value;
    }
}
