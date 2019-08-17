package net.vpc.scholar.hadrumaths;

/**
 * Created by vpc on 4/17/14.
 */
public interface BooleanMarker {


    static BooleanMarker create() {
        return new DefaultOutBoolean();
    }

    static BooleanRef ref() {
        return BooleanRefs.create();
    }

    static BooleanMarker none() {
        return NoneOutBoolean.INSTANCE;
    }


    void set();


}
