package net.vpc.scholar.hadrumaths;

public class NoneOutBoolean implements OutBoolean {
    public static final OutBoolean INSTANCE=new NoneOutBoolean();

    public boolean get() {
        throw new IllegalArgumentException();
    }

    public void set(boolean t) {

    }

    public void set() {

    }

    public void unset() {
        throw new IllegalArgumentException();
    }

    public boolean isSet() {
        throw new IllegalArgumentException();
    }
}
