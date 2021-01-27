package net.thevpc.scholar.hadrumaths;

public class NoneOutBoolean implements BooleanMarker {
    public static final BooleanMarker INSTANCE = new NoneOutBoolean();

    public void set(boolean t) {

    }

    public void set() {

    }

    public boolean get() {
        throw new IllegalArgumentException("NoneOutBoolean is readonly. You should not call get");
    }
}
