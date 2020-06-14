package net.vpc.scholar.hadrumaths;

public class DefaultOutBoolean implements BooleanMarker {
    public boolean value;

    public void set(boolean t) {
        value = t;
    }

    public void set() {
        value = true;
    }

    public boolean get() {
        return value;
    }

    public boolean isSet() {
        return value;
    }
}
