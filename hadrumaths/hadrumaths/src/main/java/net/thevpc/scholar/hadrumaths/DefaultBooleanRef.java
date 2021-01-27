package net.thevpc.scholar.hadrumaths;

class DefaultBooleanRef implements BooleanRef {
    public boolean value;

    public void set(boolean t) {
        value = t;
    }

    public boolean get() {
        return value;
    }

    public void reset() {
        value = false;
    }

    public void set() {
        value = true;
    }
}
