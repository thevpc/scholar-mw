package net.vpc.scholar.hadrumaths;

class DefaultBooleanRef implements BooleanRef {
    public boolean value;

    public void set(boolean t) {
        value = t;
    }

    public void set() {
        value = true;
    }

    public void reset() {
        value = false;
    }

    public boolean get() {
        return value;
    }
}
