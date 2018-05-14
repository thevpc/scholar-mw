package net.vpc.scholar.hadrumaths;

/**
 * Created by vpc on 4/17/14.
 */
public interface BooleanRef extends BooleanMarker {


    static BooleanRef create() {
        return new DefaultBooleanRef();
    }

    void set(boolean t);

    boolean get();

    void reset();

}
