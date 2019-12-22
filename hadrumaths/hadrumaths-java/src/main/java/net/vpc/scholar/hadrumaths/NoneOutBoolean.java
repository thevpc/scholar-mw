package net.vpc.scholar.hadrumaths;

public class NoneOutBoolean implements BooleanMarker {
    public static final BooleanMarker INSTANCE = new NoneOutBoolean();

    public void set(boolean t) {

    }

    public void set() {

    }

    public boolean isSet() {
        throw new IllegalArgumentException();
    }
}
